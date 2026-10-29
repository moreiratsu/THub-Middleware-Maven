package com.alvaro.thub.model;

import java.util.Date;

public class MatchDTO extends AbstractValueObject{
	
	private Long id = null;
	private Date date = null;
	private String round = null;
	
	private Long venueId = null;
	private String venueName = null;
	private String venueAddres = null;
	
	private Long eventId = null;
	private String eventName = null;
	
	private Long statusId = null;
	private String statusName = null;
	
	public MatchDTO() {	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public Long getVenueId() {
		return venueId;
	}
	
	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueAddres() {
		return venueAddres;
	}

	public void setVenueAddres(String venueAddres) {
		this.venueAddres = venueAddres;
	}

	public Long getEventId() {
		return eventId;
	}
	
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Long getStatusId() {
		return statusId;
	}
	
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	
	
}
