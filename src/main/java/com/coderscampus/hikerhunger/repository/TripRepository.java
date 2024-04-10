package com.coderscampus.hikerhunger.repository;

import com.coderscampus.hikerhunger.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}
