package com.example.hrautomationbackend.model;

import java.util.List;

public class EventsWithPages {
    private List<Event> events;
    private int pages;

    public EventsWithPages() {
    }

    public static EventsWithPages toModel(List<Event> events, int pages) {
        EventsWithPages model = new EventsWithPages();
        model.setEvents(events);
        model.setPages(pages);
        return model;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
