package com.bot.marketing.domain;
import jakarta.validation.constraints.NotNull;

public class CompanySearchRequest {
	
	@NotNull
    private String categoryText;
	
	@NotNull
    private String area;

    // Getters and setters
    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}