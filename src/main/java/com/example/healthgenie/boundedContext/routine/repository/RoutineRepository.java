package com.example.healthgenie.boundedContext.routine.repository;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByMemberId(Long userId);

    List<Routine> findByLevel(Level level);
    List<Routine> findByLevelAndDay(Level level, Day day);

    List<Routine> findAllByMemberIdAndDay(Long userId, Day day);

}
