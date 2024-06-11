package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.TripRecipe;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepo;

    @Autowired
    public TripService(TripRepository tripRepo) {
        this.tripRepo = tripRepo;
    }

    public Trip createTrip(Trip trip, User user) {
        trip.setUser(user);
        trip.setNumOfPeople(1);
        user.getTrips().add(trip);
        return tripRepo.save(trip);
    }

    public Optional<Trip> findById(Long tripId) {
        return tripRepo.findById(tripId);
    }

    public void delete(Trip trip) {
        tripRepo.delete(trip);
    }

    public void save(Trip trip) {
        tripRepo.save(trip);
    }

    public void updateTripDetails(Long tripId) {
        Optional<Trip> optionalTrip = findById(tripId);
        if(optionalTrip.isPresent()){
            Trip trip = optionalTrip.get();
            List<TripRecipe> tripRecipes = trip.getTripRecipes();

            double totalWeightOfTrip = 0.0;
            for(TripRecipe tripRecipe : tripRecipes){
                totalWeightOfTrip += tripRecipe.getTotalWeight()*tripRecipe.getRecipeQuantity();
            }

            Integer numOfPeople = trip.getNumOfPeople();
            Float numOfDays = trip.getNumOfDays();
            int gramsPerPersonPerDay = (int) (totalWeightOfTrip/(numOfPeople * numOfDays));
            trip.setGramsPerPersonPerDay(Math.round(gramsPerPersonPerDay));
            save(trip);
        }
    }

    public boolean isTripEmpty(Trip trip) {
        return trip.getTripName() == null &&
                trip.getNumOfDays() == null &&
                trip.getTripDetails() == null &&
                trip.getGramsPerPersonPerDay() == null &&
                trip.getPoundsPerPersonPerDay() == null;
    }
}
