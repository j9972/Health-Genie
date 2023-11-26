package com.example.healthgenie.boundedContext.routine.repository;

import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.GenieRoutine;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenieRoutineRepository extends JpaRepository<GenieRoutine, Long> {
    List<GenieRoutine> findByLevel(Level level);
    List<GenieRoutine> findByLevelAndDay(Level level, Day day);
}
