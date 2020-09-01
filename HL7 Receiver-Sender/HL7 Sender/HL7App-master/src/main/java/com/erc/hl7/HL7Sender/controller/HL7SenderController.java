package com.erc.hl7.HL7Sender.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erc.hl7.HL7Sender.HL7SendingResponse;
import com.erc.hl7.HL7Sender.factory.AdtMessageFactory;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.model.v23.message.*;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.message.ADT_A08;
import ca.uhn.hl7v2.model.v23.message.ADT_A11;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

@RestController
public class HL7SenderController {

	private static final int PORT_NUMBER = 3402;// change this to whatever your
												// port number is

	// In HAPI, almost all things revolve around a context object
	private static HapiContext context = new DefaultHapiContext();

	@GetMapping("/sendadt")
	public HL7SendingResponse greeting(
			@RequestParam(name = "name", defaultValue = "John") String name,
			@RequestParam(name = "surname", defaultValue = "Doe") String surname) {
		Connection connection = null;
		try {

			// create the HL7 message
			// this AdtMessageFactory class is not from HAPI but my own wrapper
			// check my GitHub page or see my earlier article for reference

			ADT_A01 adtMessage = AdtMessageFactory.createMessage("A01");
			// create a new MLLP client over the specified port
			connection = context.newClient("192.168.1.222", PORT_NUMBER, false);

			// The initiator which will be used to transmit our message
			Initiator initiator = connection.getInitiator();

			// send the previously created HL7 message over the connection
			// established
			Parser parser = context.getPipeParser();
			System.out.println("Sending message:" + "\n"
					+ parser.encode(adtMessage));
			Message response = initiator.sendAndReceive(adtMessage);

			// display the message response received from the remote party
			String responseString = parser.encode(response);
			System.out.println("Received response:\n" + responseString);

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			connection.close();
		}

		return new HL7SendingResponse(name, surname);
	}

	@GetMapping("/canceladt")
	public boolean cancelAdt(
			@RequestParam(name = "name", defaultValue = "John") String name,
			@RequestParam(name = "surname", defaultValue = "Doe") String surname,
			@RequestParam(name = "id", defaultValue = "M56468") String id) {

		try {

			// create the HL7 message
			// this AdtMessageFactory class is not from HAPI but my own wrapper
			// check my GitHub page or see my earlier article for reference

			ADT_A11 adtMessage = AdtMessageFactory.createCancelMessage("A11");
			// create a new MLLP client over the specified port
			Connection connection = context.newClient("192.168.1.222",
					PORT_NUMBER, false);

			// The initiator which will be used to transmit our message
			Initiator initiator = connection.getInitiator();

			// send the previously created HL7 message over the connection
			// established
			Parser parser = context.getPipeParser();
			System.out.println("Sending message:" + "\n"
					+ parser.encode(adtMessage));
			Message response = initiator.sendAndReceive(adtMessage);

			// display the message response received from the remote party
			String responseString = parser.encode(response);
			System.out.println("Received response:\n" + responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@GetMapping("/updateadt")
	public boolean updateAdt(
			@RequestParam(name = "name", defaultValue = "John") String name,
			@RequestParam(name = "surname", defaultValue = "Doe") String surname,
			@RequestParam(name = "id", defaultValue = "M56468") String id) {

		try {

			// create the HL7 message
			// this AdtMessageFactory class is not from HAPI but my own wrapper
			// check my GitHub page or see my earlier article for reference

			ADT_A08 adtMessage = AdtMessageFactory.createUpdateMessage("A08");
			// create a new MLLP client over the specified port
			Connection connection = context.newClient("192.168.1.222",
					PORT_NUMBER, false);

			// The initiator which will be used to transmit our message
			Initiator initiator = connection.getInitiator();

			// send the previously created HL7 message over the connection
			// established
			Parser parser = context.getPipeParser();
			System.out.println("Sending message:" + "\n"
					+ parser.encode(adtMessage));
			Message response = initiator.sendAndReceive(adtMessage);

			// display the message response received from the remote party
			String responseString = parser.encode(response);
			System.out.println("Received response:\n" + responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@GetMapping("/test")
	public boolean test() {
		
		try {
		
			String msg1 = "MSH|^~\\&|Mindray|ADTServerDemo|||20200831132030+0800||ADT^A01|3|P|2.6|\r"
					+ "EVN|A01|20200831132030+0800|\r"
					+ "PID|||M0831_00001||Guler^Sertac^12345678912||20200831|F||2028-9|^^^^||||||||||||||||||||||||^|^||^|^\r"
					+ "PV1||I|ICU^^410^ADT Server^^^^^^sertac|&&0|||^|^||||||||||N||||||||||||||||||||||||^|^|20200831000000+0800||||||^||^\r"
					+ "OBR||||69952^MDC_DEV_MON_PT_PHYSIO_MULTI_PARAM^MDC|||00000000000|\r"
					+ "OBX||NM|68060^MDC_ATTR_PT_HEI||169.0||||||F\r"
					+ "OBX||NM|68063^MDC_ATTR_PT_WEI||59.0||||||F\r"
					+ "OBX||ST|2302^MNDRY_ATTR_PT_B||AB||||||F\r"
					+ "OBX||ST|30459^MNDRY_ATTR_PT_E||ON||||||F\r";
			
			String initialString = msg;
			InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
			Hl7InputStreamMessageIterator  messageIterator = new Hl7InputStreamMessageIterator(targetStream);
			System.out.println("Request:\n" + initialString);
			Connection connection = context.newClient("192.168.1.222",3402, false);
			while (messageIterator.hasNext()) {
				try {
					Message nextMessage = messageIterator.next();
					Message messageResponse = connection.getInitiator().sendAndReceive(nextMessage);
					System.out.println("Response received from server was " + messageResponse.encode());
				} catch (IOException e) {
					e.printStackTrace(); //in real-life, you need to handle these exceptions 
				
				}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
		
	}

	private String getSequenceNumber() {
		String facilityNumberPrefix = "1234"; // some arbitrary prefix for the
												// // facility
		return facilityNumberPrefix.concat(getCurrentTimeStamp());
	}

	private String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

}
