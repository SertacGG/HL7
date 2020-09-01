package com.erc.integration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MINDRAYMESSAGE")
public class MindrayMessage {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "PATIENTID")
	private String patientid;
	@Column(name = "FIRSTNAME")
	private String firstname;
	@Column(name = "LASTNAME")
	private String lastname;

	@Column(name = "TEMP")
	private String temp;
	@Column(name = "SAT_O2")
	private String sat_o2;
	@Column(name = "PULS_RATE")
	private String puls_rate;
	@Column(name = "HEART_RATE")
	private String heart_rate;

	@Column(name = "HL7MESSAGE")
	private String hL7Message;

	@Column(name = "CREATE_DATE")
	private Date create_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String gethL7Message() {
		return hL7Message;
	}

	public void sethL7Message(String hL7Message) {
		this.hL7Message = hL7Message;
	}

	public String getPatientid() {
		return patientid;
	}

	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSat_o2() {
		return sat_o2;
	}

	public void setSat_o2(String sat_o2) {
		this.sat_o2 = sat_o2;
	}

	public String getPuls_rate() {
		return puls_rate;
	}

	public void setPuls_rate(String puls_rate) {
		this.puls_rate = puls_rate;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHeart_rate() {
		return heart_rate;
	}

	public void setHeart_rate(String heart_rate) {
		this.heart_rate = heart_rate;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
