package com.example.healthcare.repository.diet;

import com.example.healthcare.domain.diet.DietInfo;
import com.example.healthcare.domain.enumType.diet.DietStatusRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietInfoRepository extends JpaRepository<DietInfo ,Long> {

    List<DietInfo> findAllByDietId(Long dietId);
    void deleteAllByDietId(Long dietId);

    List<DietInfo> findAllByDietIdAndDietStatusRole(Long dietId, DietStatusRole dietStatusRole);
}
