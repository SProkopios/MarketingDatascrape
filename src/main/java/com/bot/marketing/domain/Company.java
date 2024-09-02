package com.bot.marketing.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

//Entity used mainly for Firebase operations
public class Company {

	private String businessId;
	private List<String> name;
	private String source;
	private String email;
	private String link;
	private String personName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date send;
	private boolean operational;
	private String area;
	

	public Company(String businessId, List<String> name, String source, String email, String link, String personName, Date send, boolean operational, String area) {
		super();
		this.businessId = businessId;
		this.name = name;
		this.source = source;
		this.email = email;
		this.link = link;
		this.personName = personName;
		this.send = send;
		this.operational = operational;
		this.area = area;
	}
	
	public Company () {};
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Date isSend() {
		return send;
	}

	public void setSend(Date send) {
		this.send = send;
	}

	public boolean isOperational() {
		return operational;
	}

	public void setOperational(boolean operational) {
		this.operational = operational;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
	@Override
	public String toString() {
	    return "Company{" +
	            "businessId='" + businessId + '\'' +
	            ", name='" + name + '\'' +
	            ", source='" + source + '\'' +
	            ", email='" + email + '\'' +
	            ", link='" + link + '\'' +
	            ", personName='" + personName + '\'' +
	            ", send='" + send + '\'' +
	            ", operational='" + operational + '\'' +
	            ", area='" + area + '\'' +
	            '}';
	}
}
