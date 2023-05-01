package com.example.hrautomationbackend.model;

import java.util.Date;

public class EventFilter {
    private String name;
    private Date fromDate;
    private Date toDate;
    private Long cityId;
    private EventFormat format;

    public EventFilter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public EventFormat getFormat() {
        return format;
    }

    public void setFormat(EventFormat format) {
        this.format = format;
    }
}
