package com.erc.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v26.datatype.XPN;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

public class HL7Listener {

	static final char END_OF_BLOCK = '\u001c';
	static final char START_OF_BLOCK = '\u000b';
	static final char CARRIAGE_RETURN = 13;
	static final char LINE_FEED = 13;
	private static final int END_OF_TRANSMISSION = -1;
	public static final int PORT = 14000;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws HL7Exception {
		
		try (Socket serverSocket = new Socket("192.168.1.222", PORT)) {

			while (true) {
				InputStream input = serverSocket.getInputStream();
				OutputStream outputStream = serverSocket.getOutputStream();
				if(input !=null) {
					String parsedHL7Message = HL7Listener.getFromSocket(input);

					if (parsedHL7Message != null) {
						System.out.println("parsedHL7Message: " + parsedHL7Message);
						Parser p = new GenericParser();
						Message msg = null;
						Message ack = null;
						msg = p.parse(parsedHL7Message);
						
						if(parsedHL7Message.contains("OBX")){
							
							ORU_R01 oruMessage = (ORU_R01) msg;
							MSH msh = oruMessage.getMSH();
							
							PID pid = oruMessage.getPATIENT_RESULT().getPATIENT().getPID();
							
							MindrayMessage messageDto = new MindrayMessage();
							messageDto.sethL7Message(parsedHL7Message);
							
							XPN x = pid.getPatientName(0);
							messageDto.setPatientid(pid.getPid3_PatientIdentifierList(0).getCx1_IDNumber().getValue());
							messageDto.setLastname(x.getFamilyName().getFn1_Surname().getValue());
							messageDto.setFirstname(x.getGivenName().getValue());
						
							int indis =1;
							for (ORU_R01_PATIENT_RESULT response : oruMessage.getPATIENT_RESULTAll()) 
							{			
								for (ORU_R01_ORDER_OBSERVATION orderObservation : response.getORDER_OBSERVATIONAll())
								{			
									  OBR obr = orderObservation.getOBR();
									  String fillerOrderNumber = obr.getObr3_FillerOrderNumber().encode();
									  
									  for (ORU_R01_OBSERVATION observation : orderObservation.getOBSERVATIONAll()) {
										      OBX obx = observation.getOBX();
										      String type = obx.getObx3_ObservationIdentifier().getCwe2_Text().getValue();
										      String type1 = obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue();
										      String status = obx.getObservationResultStatus().getValue();
										      Varies[] variesArr = obx.getObservationValue();
										      if(variesArr != null && variesArr.length > 0)
										      {
										    	  Varies vari0 = variesArr[0];
										    	  Type typVarr = vari0.getData();
										    	  System.out.println(indis++ +" "+ type +" "+ typVarr.toString());
										    	  if(type.equals("MDC_TEMP")){
										    		  messageDto.setTemp(typVarr.toString());
										    	  }
										    	  else if(type.equals("MDC_PULS_OXIM_SAT_O2")){
										    		  messageDto.setSat_o2(typVarr.toString());
										    	  }
										    	  else if(type.equals("MDC_PULS_OXIM_PULS_RATE")){
										    		  messageDto.setPuls_rate(typVarr.toString());
										    	  }
										    	  else if(type.equals("MDC_ECG_HEART_RATE")){
										    		  messageDto.setHeart_rate(typVarr.toString());
										    	  }
										      }
									  }
								}
							}
							System.out.println(messageDto.toString());
							try (Session session = HibernateUtil.getSessionFactory().openSession()) {
								Transaction transaction = session.getTransaction();
								transaction.begin();
								
								
								SQLQuery sqlQuery = session.createSQLQuery("select SEQMINDRAY.nextval value from dual");
								sqlQuery.addScalar("value", LongType.INSTANCE);
								Long result = (Long) sqlQuery.uniqueResult();
								System.out.println(result);
								messageDto.setId(result);

								messageDto.setCreate_date(new Date());
								
								session.save(messageDto);
								transaction.commit();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}	
						
						ack = msg.generateACK();
						// ack = processMessage(adt);
						String buildAcknowledgmentMessage = p.encode(ack);
						HL7Listener.sendToSocket(outputStream,
								buildAcknowledgmentMessage);
					}
				}

			}
		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static String getFromSocket(InputStream anInputStream)
			throws IOException {

		boolean end_of_message = false;
		StringBuffer parsedMessage = new StringBuffer();

		int characterReceived = 0;

		try {
			characterReceived = anInputStream.read();
		} catch (SocketException e) {
			System.err.println(" Unable to read from socket stream. "
					+ "Connection may have been closed: " + e.getMessage());
			return null;
		}

		if (characterReceived == END_OF_TRANSMISSION) {
			return null;
		}

		if (characterReceived != START_OF_BLOCK) {
			throw new RuntimeException(
					"Start of block character has not been received");
		}

		while (!end_of_message) {
			characterReceived = anInputStream.read();

			if (characterReceived == END_OF_TRANSMISSION) {
				throw new RuntimeException(
						"Message terminated without end of message character");
			}

			if (characterReceived == END_OF_BLOCK) {
				characterReceived = anInputStream.read();

				if (characterReceived != CARRIAGE_RETURN) {
					throw new RuntimeException(
							"End of message character must be followed by a carriage return character");
				}
				end_of_message = true;
			} else {
				parsedMessage.append((char) characterReceived);
			}
		}

		return parsedMessage.toString();
	}

	public static void sendToSocket(OutputStream out, String s) {
		StringBuffer transmitHL7Ack = new StringBuffer();
		transmitHL7Ack.append(START_OF_BLOCK);
		transmitHL7Ack.append(s);
		transmitHL7Ack.append(CARRIAGE_RETURN);
		transmitHL7Ack.append(END_OF_BLOCK);
		transmitHL7Ack.append(CARRIAGE_RETURN);

		try {
			out.write(transmitHL7Ack.toString().getBytes(), 0, transmitHL7Ack
					.toString().getBytes().length);
			System.err
					.println("MLLPServerConnectionHandler --> SendToSocket(): "
							+ s);
		} catch (IOException e) {
			System.err
					.println("MLLPServerConnectionHandler --> SendToSocket() Error: "
							+ e.getMessage());
		}
	}
	
}
