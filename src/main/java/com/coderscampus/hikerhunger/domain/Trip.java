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
    private String tripDetails;
    private Float poundsPerPersonPerDay;
    
    //START HERE
    @ManyToMany
    @JoinTable(
            name = "trip_recipe",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes = new ArrayList<>();

    public List<Recipe> getRecipes() {
        return recipes;
    }
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
    public String getTripDetails() {
        return tripDetails;
    }
    public void setTripDetails(String tripDetails) {
        this.tripDetails = tripDetails;
    }
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

}
