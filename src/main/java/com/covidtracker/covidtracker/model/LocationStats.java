package com.covidtracker.covidtracker.model;

public class LocationStats {
	
	private String state;
	private String country;
	private int activeCases;
	private int diffFromPrevDay;
	
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getActiveCases() {
		return activeCases;
	}
	public void setActiveCases(int activeCases) {
		this.activeCases = activeCases;
	}
	
	public int getDiffFromPrevDay() {
		return diffFromPrevDay;
	}
	public void setDiffFromPrevDay(int diffFromPrevDay) {
		this.diffFromPrevDay = diffFromPrevDay;
	}
	
	@Override
	public String toString() {
		return String.format("LocationStats [state=%s, country=%s, activeCases=%s, diffFromPrevDay=%s]", state, country,
				activeCases, diffFromPrevDay);
	}

}

