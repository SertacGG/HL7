package com.erc.hl7.HL7Sender.factory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v23.datatype.CX;
import ca.uhn.hl7v2.model.v23.datatype.PL;
import ca.uhn.hl7v2.model.v23.datatype.XAD;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.datatype.XPN;
import ca.uhn.hl7v2.model.v23.message.ADT_A11;
import ca.uhn.hl7v2.model.v23.segment.EVN;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;

public class AdtA11MessageBuilder {
	private ADT_A11 _adtMessage;
	
	public ADT_A11 build() throws HL7Exception, IOException {
		String currentDateTimeString = getCurrentTimeStamp();
		_adtMessage = new ADT_A11();
		_adtMessage.initQuickstart("ADT", "A11", "P");
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
		mshSegment.getVersionID().setValue("2.3");
	}
	
	private void createEvnSegment(String currentDateTimeString) throws DataTypeException {
		EVN evn = _adtMessage.getEVN();
		evn.getEventTypeCode().setValue("A11");
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(currentDateTimeString);
	}

	private void createPidSegment() throws DataTypeException {
		PID pid = _adtMessage.getPID();
		
		CX patient = pid.getPatientIDInternalID(0);
		patient.getID().setValue("M0820_00003");
		
		XPN patientName = pid.getPatientName(0);
		patientName.getFamilyName().setValue("");
		patientName.getGivenName().setValue("");
		XAD patientAddress = pid.getPatientAddress(0);
		patientAddress.getStreetAddress().setValue(""); 
		patientAddress.getCity().setValue("");
		patientAddress.getStateOrProvince().setValue("");
		patientAddress.getCountry().setValue("");
	}

	private void createPv1Segment() throws DataTypeException {
		PV1 pv1 = _adtMessage.getPV1();
		pv1.getPatientClass().setValue("I");
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
