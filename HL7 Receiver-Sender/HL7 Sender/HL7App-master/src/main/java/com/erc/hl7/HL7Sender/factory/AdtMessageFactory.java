package com.erc.hl7.HL7Sender.factory;

import java.io.IOException;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v23.message.*;

public class AdtMessageFactory {

	static int PORT_NUMBER = 3402;
	private static HapiContext context = new DefaultHapiContext();

	public static ADT_A01 createMessage(String messageType) throws HL7Exception, IOException {

		try {
			// This patterns enables you to build other message types

			ADT_A01 adtMessage = new AdtA01MessageBuilder().build();
			return adtMessage;
		} catch (HL7Exception | IOException e) {

			// if other types of ADT messages are needed, then implement your builders here
		}
		throw new RuntimeException(
				String.format("%s message type is not supported yet. Extend this if you need to", messageType));

	}
	
	public static ADT_A11 createCancelMessage(String messageType) throws HL7Exception, IOException {

		try {
			ADT_A11 adtMessage = new AdtA11MessageBuilder().build();
			return adtMessage;
		} catch (HL7Exception | IOException e) {

			// if other types of ADT messages are needed, then implement your builders here
		}
		throw new RuntimeException(
				String.format("%s message type is not supported yet. Extend this if you need to", messageType));

	}

	public static ADT_A08 createUpdateMessage(String messageType) throws HL7Exception, IOException {

		try {
			ADT_A08 adtMessage = new AdtA08MessageBuilder().build();
			return adtMessage;
		} catch (HL7Exception | IOException e) {

			// if other types of ADT messages are needed, then implement your builders here
		}
		throw new RuntimeException(
				String.format("%s message type is not supported yet. Extend this if you need to", messageType));

	}
	
	/*public static ORU_R01 sendPatientMonitorMessage(String messageType) throws HL7Exception, IOException {

		try {
			// This patterns enables you to build other message types

			ORU_R01 adtMessage = new OruR01MessageBuilder().build();
			return adtMessage;

		} catch (HL7Exception | IOException e) {

			// if other types of ADT messages are needed, then implement your builders here
		}
		throw new RuntimeException(
				String.format("%s message type is not supported yet. Extend this if you need to", messageType));

	}*/
	
	public static void main(String... strings) {
		try {
			AdtMessageFactory.createMessage("A01");
		} catch (HL7Exception | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}