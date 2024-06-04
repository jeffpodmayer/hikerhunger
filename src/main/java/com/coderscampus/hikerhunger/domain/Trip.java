package com.coderscampus.hikerhunger.domain;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tripId")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;
    private String tripName;
    private Float numOfDays;
    private Integer numOfPeople;
    @Column(length=1000)
    private String tripDetails;
    private Integer gramsPerPersonPerDay;
    private Float poundsPerPersonPerDay;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.MERGE)
    private List<TripRecipe> tripRecipes = new ArrayList<>();

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

    public String getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(String tripDetails) {
        this.tripDetails = tripDetails;
    }

    public Integer getGramsPerPersonPerDay() {
        return gramsPerPersonPerDay;
    }

    public void setGramsPerPersonPerDay(Integer gramsPerPersonPerDay) {
        this.gramsPerPersonPerDay = gramsPerPersonPerDay;
    }

    public Float getPoundsPerPersonPerDay() {
        return poundsPerPersonPerDay;
    }

    public void setPoundsPerPersonPerDay(Float poundsPerPersonPerDay) {
        this.poundsPerPersonPerDay = poundsPerPersonPerDay;
    }

    public List<TripRecipe> getTripRecipes() {
        return tripRecipes;
    }

    public void setTripRecipes(List<TripRecipe> tripRecipes) {
        this.tripRecipes = tripRecipes;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", user=" + user +
                ", tripName='" + tripName + '\'' +
                ", numOfDays=" + numOfDays +
                ", numOfPeople=" + numOfPeople +
                ", tripDetails='" + tripDetails + '\'' +
                ", gramsPerPersonPerDay=" + gramsPerPersonPerDay +
                ", tripRecipes=" + tripRecipes +
                '}';
    }
}
