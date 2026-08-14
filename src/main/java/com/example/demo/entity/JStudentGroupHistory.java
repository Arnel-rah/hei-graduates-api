package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_group_history")
@Getter
@Setter
@NoArgsConstructor
public class JStudentGroupHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private Instant startDate;

  private Instant endDate;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private JStudent student;

  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private JGroup group;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
    if (this.startDate == null) {
      this.startDate = Instant.now();
    }
  }
}
