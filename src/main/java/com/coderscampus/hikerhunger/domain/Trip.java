package com.coderscampus.hikerhunger.domain;

import jakarta.persistence.Entity;

@Entity
public class Trip {
    private Long tripId;
    private User user;
    private String tripName;
    private Float numOfDays;
    private Integer numOfPeople;
    private Float poundsPerPersonPerDay;

    public Float getPoundsPerPersonPerDay() {
        return poundsPerPersonPerDay;
    }

    public void setPoundsPerPersonPerDay(Float poundsPerPersonPerDay) {
        this.poundsPerPersonPerDay = poundsPerPersonPerDay;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Float getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(Float numOfDays) {
        this.numOfDays = numOfDays;
    }

    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    //    @Override
//    public String toString() {
//        return "Trip{" +
//                "tripId=" + tripId +
//                ", user=" + user +
//                ", tripName='" + tripName + '\'' +
//                ", numOfDays=" + numOfDays +
//                ", poundsPersonPerDay=" + poundsPersonPerDay +
//                '}';
//    }


}
