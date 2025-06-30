package com.example.stressbackend.repository;

import com.example.stressbackend.model.LowStressAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LowStressAlertRepository extends JpaRepository<LowStressAlert, Long> {
    List<LowStressAlert> findByProcessedFalse();
}