package com.example.healthcare.repository.diet;

import com.example.healthcare.domain.diet.DietImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietImageUrlRepository extends JpaRepository<DietImageUrl, Long> {
    List<DietImageUrl> findAllByDietId(Long id);
    void deleteAllByDietId(Long dietId);
}
