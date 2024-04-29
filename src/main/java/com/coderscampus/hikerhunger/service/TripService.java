package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
