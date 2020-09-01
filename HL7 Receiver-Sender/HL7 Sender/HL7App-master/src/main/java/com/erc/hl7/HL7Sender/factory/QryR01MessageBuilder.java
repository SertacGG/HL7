package com.erc.hl7.HL7Sender.factory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v23.datatype.CX;
import ca.uhn.hl7v2.model.v23.datatype.PL;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.datatype.XPN;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.message.QRY_R02;
import ca.uhn.hl7v2.model.v23.segment.EVN;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v23.segment.PV1;
import ca.uhn.hl7v2.model.v23.segment.QRD;
import ca.uhn.hl7v2.model.v23.segment.QRF;

public class QryR01MessageBuilder {
	private QRY_R02 _qryMessage;

	public QRY_R02 build() throws HL7Exception, IOException {
		String currentDateTimeString = getCurrentTimeStamp();
		_qryMessage = new QRY_R02();
		_qryMessage.initQuickstart("QRY", "R01", "P");
		createMshSegment(currentDateTimeString);
		createQrdSegment();
		createQrfSegment();
		return _qryMessage;
	}

	private void createMshSegment(String currentDateTimeString)
			throws DataTypeException {
		MSH mshSegment = _qryMessage.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getSendingApplication().getNamespaceID() .setValue("Our System");
		mshSegment.getSendingFacility().getNamespaceID() .setValue("Our Facility");
		mshSegment.getReceivingApplication().getNamespaceID() .setValue("Their Remote System");
		mshSegment.getReceivingFacility().getNamespaceID() .setValue("Their Remote Facility");
		mshSegment.getDateTimeOfMessage().getTimeOfAnEvent() .setValue(currentDateTimeString);
		mshSegment.getMessageControlID().setValue(getSequenceNumber());
		mshSegment.getVersionID().setValue("2.6");
	}

	private void createQrfSegment() {
		QRD qrdSegment = _qryMessage.getQRD();
	}

	private void createQrdSegment() {
		QRF qrfSegment = _qryMessage.getQRF();
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
