package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grade_history")
@Getter
@Setter
@NoArgsConstructor
public class JGradeHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private BigDecimal oldValue;

  private BigDecimal newValue;

  private String reason;

  private Instant modifiedAt;

  @ManyToOne
  @JoinColumn(name = "grade_id", nullable = false)
  private JGrade grade;

  @ManyToOne
  @JoinColumn(name = "modified_by_account_id", nullable = false)
  private JAccount modifiedBy;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
    if (this.modifiedAt == null) {
      this.modifiedAt = Instant.now();
    }
  }
}
