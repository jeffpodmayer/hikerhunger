package com.coderscampus.hikerhunger.service;

import com.coderscampus.hikerhunger.domain.Trip;
import com.coderscampus.hikerhunger.domain.User;
import com.coderscampus.hikerhunger.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripService {

    private TripRepository tripRepo;

    @Autowired
    public TripService(TripRepository tripRepo) {
        this.tripRepo = tripRepo;
    }

    public Trip createTrip(Trip trip, User user) {
        trip.setUser(user);
        user.getTrips().add(trip);
        return tripRepo.save(trip);
    }

    public Optional<Trip> findById(Long tripId) {
        return tripRepo.findById(tripId);
    }

    public Trip saveTrip(Trip trip) {
        tripRepo.save(trip);
        return trip;
    }

    public void delete(Trip trip) {
        tripRepo.delete(trip);
    }
}
