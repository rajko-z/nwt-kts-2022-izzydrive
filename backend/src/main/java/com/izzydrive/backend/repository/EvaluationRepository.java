package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e" +
            " left join fetch e.driving d" +
            " where e.driving.id = ?1")
    List<Evaluation> findAllByDrivingId(Long drivingId);
}
