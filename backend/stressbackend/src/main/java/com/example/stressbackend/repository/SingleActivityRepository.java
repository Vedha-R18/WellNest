package com.example.stressbackend.repository;

import com.example.stressbackend.model.SingleActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SingleActivityRepository extends JpaRepository<SingleActivity, Long> {
    // Custom query to get a random activity
    @Query(value = "SELECT * FROM single_activities ORDER BY RAND() LIMIT 1", nativeQuery = true)
    SingleActivity findRandomActivity();
}