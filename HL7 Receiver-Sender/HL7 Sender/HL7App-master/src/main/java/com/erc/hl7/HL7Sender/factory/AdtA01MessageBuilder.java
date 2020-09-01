package com.erc.hl7.HL7Sender.factory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v23.datatype.CE;
import ca.uhn.hl7v2.model.v23.datatype.CX;
import ca.uhn.hl7v2.model.v23.datatype.PL;
import ca.uhn.hl7v2.model.v23.datatype.XAD;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.datatype.XPN;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.*;
 
public class AdtA01MessageBuilder {
	private ADT_A01 _adtMessage;
	private ORU_R01 _oruR01Message;

	/*
	 * You can pass in a domain object as a parameter when integrating with data
	 * from your application here and I will leave that to you to explore on your
	 * own. I will use fictional data here for illustration
	 */

	public ADT_A01 build() throws HL7Exception, IOException {
		String currentDateTimeString = getCurrentTimeStamp();
		_adtMessage = new ADT_A01();
		_adtMessage.initQuickstart("ADT", "A01", "P");
		createMshSegment(currentDateTimeString);
		createEvnSegment(currentDateTimeString);
		createPidSegment();
		createPv1Segment();	
		return _adtMessage;
	}
	
	private void createMshSegment(String currentDateTimeString) throws DataTypeException {
		MSH mshSegment = _adtMessage.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getSendingApplication().getNamespaceID().setValue("Our System");
		mshSegment.getSendingFacility().getNamespaceID().setValue("Our Facility");
		mshSegment.getReceivingApplication().getNamespaceID().setValue("Their Remote System");
		mshSegment.getReceivingFacility().getNamespaceID().setValue("Their Remote Facility");
		mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue(currentDateTimeString);
		mshSegment.getMessageControlID().setValue(getSequenceNumber());
		mshSegment.getVersionID().setValue("2.6");
	}

	private void createEvnSegment(String currentDateTimeString) throws DataTypeException {
		EVN evn = _adtMessage.getEVN();
		evn.getEventTypeCode().setValue("A01");
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(currentDateTimeString);
	}

	private void createPidSegment() throws DataTypeException {
		PID pid = _adtMessage.getPID();
		//_adtMessage.getPID().getSetIDPatientID().setValue("65656");
		//_adtMessage.getPID().getPid1_SetIDPatientID().setValue("56565");
		//pid.getSetIDPatientID().setValue("157");
		
		CX patient = pid.getPatientIDInternalID(0);
		patient.getID().setValue("M111111");
		
		XPN patientName = pid.getPatientName(0);
		patientName.getFamilyName().setValue("sekerci");
		patientName.getGivenName().setValue("sefa");
		pid.getPid4_AlternatePatientID().getCx1_ID().setValue("999999");
		//pid.getPid1_SetIDPatientID().setValue("45454545");
		//pid.getPid2_PatientIDExternalID().getCx1_ID().setValue("1111111111");
		pid.getPid2_PatientIDExternalID().getCx1_ID().setValue("888888888");
	}

	private void createPv1Segment() throws DataTypeException {
		PV1 pv1 = _adtMessage.getPV1();
		pv1.getPatientClass().setValue("O"); // to represent an 'Outpatient'
		PL assignedPatientLocation = pv1.getAssignedPatientLocation();
		assignedPatientLocation.getFacility().getNamespaceID().setValue("Some Treatment Facility Name");
		assignedPatientLocation.getPointOfCare().setValue("Some Point of Care");
		pv1.getAdmissionType().setValue("ALERT");
		XCN referringDoctor = pv1.getReferringDoctor(0);
		referringDoctor.getIDNumber().setValue("99999999");
		referringDoctor.getFamilyName().setValue("Smith");
		referringDoctor.getGivenName().setValue("Jack");
		referringDoctor.getIdentifierTypeCode().setValue("456789");
		pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getCurrentTimeStamp());
	}

	private String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	private String getSequenceNumber() {
		String facilityNumberPrefix = "1234"; // some arbitrary prefix for the facility
		return facilityNumberPrefix.concat(getCurrentTimeStamp());
	}

}