package com.example.demo.service;

import com.example.demo.mapper.GradeHistoryMapper;
import com.example.demo.model.GradeHistoryRecord;
import com.example.demo.repository.GradeHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeHistoryService {

  private final GradeHistoryRepository gradeHistoryRepository;

  public List<GradeHistoryRecord> getHistory(String gradeId) {
    return gradeHistoryRepository.findByGradeIdOrderByModifiedAtAsc(gradeId).stream()
        .map(GradeHistoryMapper::toRecord)
        .toList();
  }
}
