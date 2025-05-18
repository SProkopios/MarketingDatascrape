package com.bot.marketing.domain;

public class CompanySearchRequest {
    private String categoryText;
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