package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventBean {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("title_ing")
    @Expose
    String title_ing;
    @SerializedName("description_ing")
    @Expose
    String description_ing;
    @SerializedName("start_event")
    @Expose
    String start_event;
    @SerializedName("end_event")
    @Expose
    String end_event;
    @SerializedName("filters")
    @Expose
    String filters;
    @SerializedName("participants")
    @Expose
    int participants;
    @SerializedName("url_resource")
    @Expose
    String url_resource;

    @SerializedName("color")
    @Expose
    String color;

    public EventBean() {
    }

    public EventBean(int id, String title, String description, String title_ing, String description_ing, String start_event, String end_event, String filters, int participants, String url_resource) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.title_ing = title_ing;
        this.description_ing = description_ing;
        this.start_event = start_event;
        this.end_event = end_event;
        this.filters = filters;
        this.participants = participants;
        this.url_resource = url_resource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle_ing() {
        return title_ing;
    }

    public void setTitle_ing(String title_ing) {
        this.title_ing = title_ing;
    }

    public String getDescription_ing() {
        return description_ing;
    }

    public void setDescription_ing(String description_ing) {
        this.description_ing = description_ing;
    }

    public String getStart_event() {
        return start_event;
    }

    public void setStart_event(String start_event) {
        this.start_event = start_event;
    }

    public String getEnd_event() {
        return end_event;
    }

    public void setEnd_event(String end_event) {
        this.end_event = end_event;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getUrl_resource() {
        return url_resource;
    }

    public void setUrl_resource(String url_resource) {
        this.url_resource = url_resource;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
