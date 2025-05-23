package com.bot.marketing.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

//Entity used mainly for Firebase operations
public class Company {

	private String businessId;
	private List<String> name;
	private String source;
	private String email;
	private String link;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Instant createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Instant send;
	private boolean operational;
	private String area;
	private String industry;
	

	public Company(String businessId, List<String> name, String source, String email, String link, Instant createdAt, Instant send, boolean operational, String area, String industry) {
		super();
		this.businessId = businessId;
		this.name = name;
		this.source = source;
		this.email = email;
		this.link = link;
		this.createdAt = createdAt;
		this.send = send;
		this.operational = operational;
		this.area = area;
		this.industry = industry;
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
	
	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant isSend() {
		return send;
	}

	public void setSend(Instant send) {
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
	
	public String getIndustry() {
		return industry;
	}
	
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	
	
	@Override
	public String toString() {
	    return "Company{" +
	            "businessId='" + businessId + '\'' +
	            ", name='" + name + '\'' +
	            ", source='" + source + '\'' +
	            ", email='" + email + '\'' +
	            ", link='" + link + '\'' +
	            ", createdAt='" + createdAt + '\'' +
	            ", send='" + send + '\'' +
	            ", operational='" + operational + '\'' +
	            ", area='" + area + '\'' +
	            ", industry='" + industry + '\'' +
	            '}';
	}
}
