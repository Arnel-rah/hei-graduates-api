package com.example.demo.repository;

import com.example.demo.entity.JAcademicYear;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<JAcademicYear, String> {

  List<JAcademicYear> findByPromotionId(String promotionId);
}
