package com.example.demo.repository;

import com.example.demo.entity.JStudentGroupHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupHistoryRepository extends JpaRepository<JStudentGroupHistory, String> {

  List<JStudentGroupHistory> findByStudentIdOrderByStartDateDesc(String studentId);
}
