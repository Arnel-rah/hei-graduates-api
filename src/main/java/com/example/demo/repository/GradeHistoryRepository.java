package com.example.demo.repository;

import com.example.demo.entity.JGradeHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeHistoryRepository extends JpaRepository<JGradeHistory, String> {

  List<JGradeHistory> findByGradeIdOrderByModifiedAtAsc(String gradeId);
}
