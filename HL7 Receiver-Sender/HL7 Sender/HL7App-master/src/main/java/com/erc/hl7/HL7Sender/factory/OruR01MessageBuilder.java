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
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;
import ca.uhn.hl7v2.model.v23.segment.OBR;

public class OruR01MessageBuilder {

	private ORU_R01 _oruR01Message;

	public ORU_R01 build() throws HL7Exception, IOException {
		String currentDateTimeString = getCurrentTimeStamp();
		_oruR01Message = new ORU_R01();
		// you can use the context class's newMessage method to instantiate a message if you want
		_oruR01Message.initQuickstart("ORU", "R01", "P");

		CreateMshSegment(currentDateTimeString);
		CreatePidSegment();
		CreatePv1Segment();
		CreateObrSegment();
		CreateObxSegment();
		return _oruR01Message;
	}

	private void CreateMshSegment(String currentDateTimeString)
			throws DataTypeException {
		MSH mshSegment = _oruR01Message.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getSendingApplication().getNamespaceID()
				.setValue("Our System");
		mshSegment.getSendingFacility().getNamespaceID()
				.setValue("Our Facility");
		mshSegment.getReceivingApplication().getNamespaceID()
				.setValue("Their Remote System");
		mshSegment.getReceivingFacility().getNamespaceID()
				.setValue("Their Remote Facility");
		mshSegment.getDateTimeOfMessage().getTimeOfAnEvent()
				.setValue(currentDateTimeString);
		mshSegment.getMessageControlID().setValue(getSequenceNumber());
		mshSegment.getVersionID().setValue("2.3");
	}

	private void CreatePidSegment() throws DataTypeException {
		PID pid = _oruR01Message.getRESPONSE().getPATIENT().getPID();
		// _adtMessage.getPID().getSetIDPatientID().setValue("65656");
		// _adtMessage.getPID().getPid1_SetIDPatientID().setValue("56565");
		// pid.getSetIDPatientID().setValue("157");

		CX patient = pid.getPatientIDInternalID(0);
		patient.getID().setValue("M56468");

		XPN patientName = pid.getPatientName(0);
		patientName.getFamilyName().setValue("Mouse");
		patientName.getGivenName().setValue("Mickey");
		// pid.getPid1_SetIDPatientID().setValue("45454545");
		XAD patientAddress = pid.getPatientAddress(0);
		patientAddress.getStreetAddress().setValue("123 Main Street");
		patientAddress.getCity().setValue("Lake Buena Vista");
		patientAddress.getStateOrProvince().setValue("FL");
		patientAddress.getCountry().setValue("USA");
	}

	private void CreatePv1Segment() throws DataTypeException {
		PV1 pv1 = _oruR01Message.getRESPONSE().getPATIENT().getVISIT().getPV1();
		pv1.getPatientClass().setValue("O"); // to represent an 'Outpatient'
		PL assignedPatientLocation = pv1.getAssignedPatientLocation();
		assignedPatientLocation.getFacility().getNamespaceID()
				.setValue("Some Treatment Facility Name");
		assignedPatientLocation.getPointOfCare().setValue("Some Point of Care");
		pv1.getAdmissionType().setValue("ALERT");
		XCN referringDoctor = pv1.getReferringDoctor(0);
		referringDoctor.getIDNumber().setValue("99999999");
		referringDoctor.getFamilyName().setValue("Smith");
		referringDoctor.getGivenName().setValue("Jack");
		referringDoctor.getIdentifierTypeCode().setValue("456789");
		pv1.getAdmitDateTime().getTimeOfAnEvent()
				.setValue(getCurrentTimeStamp());
	}

	private void CreateObrSegment() throws DataTypeException {
		OBR obr = _oruR01Message.getRESPONSE().getORDER_OBSERVATION().getOBR();
		obr.getSetIDObservationRequest().setValue("1"); // Set ID - OBX
		obr.getObservationEndDateTime().getTimeOfAnEvent().setValue(getCurrentTimeStamp());
		
		CE obrUni = obr.getUniversalServiceIdentifier();
		obr.getUniversalServiceIdentifier().getIdentifier().setValue("88304");
		obrUni.getText().setValue("monitoring of patient");
		obrUni.getIdentifier().setValue("182777000");
		obrUni.getNameOfCodingSystem().setValue("SCT");
		
	}

	private void CreateObxSegment() throws DataTypeException {
		OBX obx = _oruR01Message.getRESPONSE().getORDER_OBSERVATION().getOBSERVATION(0).getOBX();
		obx.getObservationIdentifier().getText().setValue("69965^MDC_DEV_MON_PHYSIO_MULTI_PARAM_MDS^MDC");// 69965^MDC_DEV_MON_PHYSIO_MULTI_PARAM_MDS^MDC -> Patient Monitor
		obx.getObservResultStatus().setValue("X");; //observation result status
		
	}

	private String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	private String getSequenceNumber() {
		String facilityNumberPrefix = "1234"; // some arbitrary prefix for the
												// facility
		return facilityNumberPrefix.concat(getCurrentTimeStamp());
	}

}
