package com.bot.marketing.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "company")
public class Company {

	@Id
	@Column(name="businessId")
	private String businessId;
	@Column(name="name")
	private String name;
	@Column(name="lahde")
	private String lahde;
	@Column(name="email")
	private String email;
	@Column(name="hlonimi")
	private String hlonimi;
	@Column(name="lahetetty")
	private boolean lahetetty;
	@Column(name="toiminnassa")
	private boolean toiminnassa;
	
	
	public Company(String businessId, String name, String lahde, String email, String hlonimi, boolean lahetetty, boolean toiminnassa) {
		super();
		this.businessId = businessId;
		this.name = name;
		this.lahde = lahde;
		this.email = email;
		this.hlonimi = hlonimi;
		this.lahetetty = lahetetty;
		this.toiminnassa = toiminnassa;
	}
	
	public Company () {};
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLahde() {
		return lahde;
	}

	public void setLahde(String lahde) {
		this.lahde = lahde;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHlonimi() {
		return hlonimi;
	}

	public void setHlonimi(String hlonimi) {
		this.hlonimi = hlonimi;
	}

	public boolean isLahetetty() {
		return lahetetty;
	}

	public void setLahetetty(boolean lahetetty) {
		this.lahetetty = lahetetty;
	}

	public boolean isToiminnassa() {
		return toiminnassa;
	}

	public void setToiminnassa(boolean toiminnassa) {
		this.toiminnassa = toiminnassa;
	}

	@Override
	public String toString() {
	    return "Company{" +
	            "businessId='" + businessId + '\'' +
	            ", name='" + name + '\'' +
	            ", lahde='" + lahde + '\'' +
	            ", email='" + email + '\'' +
	            ", hlonimi='" + hlonimi + '\'' +
	            ", lahetetty=" + lahetetty +
	            ", toiminnassa=" + toiminnassa +
	            '}';
	}
}
