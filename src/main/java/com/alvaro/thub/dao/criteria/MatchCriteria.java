package com.alvaro.thub.dao.criteria;

import java.util.Date;

import com.alvaro.thub.model.AbstractValueObject;

public class MatchCriteria extends AbstractValueObject{
	
	private Long id = null;
	private Date date = null;
	private String round = null;
	
	private Long venueId = null;
	
	private Long eventId = null;
	
	private Long statusId = null;
	
	public MatchCriteria() {		
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

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	

	
	
}
