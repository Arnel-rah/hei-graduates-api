package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grade")
@Getter
@Setter
@NoArgsConstructor
public class JGrade {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private BigDecimal value;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private JStudent student;

  @ManyToOne
  @JoinColumn(name = "exam_id", nullable = false)
  private JExam exam;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
