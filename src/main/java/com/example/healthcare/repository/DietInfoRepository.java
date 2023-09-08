package com.example.healthcare.repository;

import com.example.healthcare.domain.diet.DietInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietInfoRepository extends JpaRepository<DietInfo ,Long> {

    List<DietInfo> findAllByDietId(Long dietId);
    void deleteAllByDietId(Long dietId);
}
