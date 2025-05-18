package com.bot.marketing.domain;

public class Filter {
	private String area;
	private String industry;
	
	public Filter(String area, String industry) {
		this.area = area;
		this.industry = industry;
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
		return "Filter [area=" + area + ", industry=" + industry + "]";
	}

}
