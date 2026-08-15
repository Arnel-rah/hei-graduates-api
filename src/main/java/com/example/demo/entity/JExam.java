package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JExam {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private Instant dateExam;
  private BigDecimal coefficient;

  @ManyToOne
  @JoinColumn(name = "course_assignment_id", nullable = false)
  private JCourseAssignment courseAssignment;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
