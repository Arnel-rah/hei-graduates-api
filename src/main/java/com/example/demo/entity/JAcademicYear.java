package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "academic_year")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class JAcademicYear {

  @Id
  @Column(length = 36)
  private String id;

  private String label;

  private int order;

  @ManyToOne
  @JoinColumn(name = "promotion_id", nullable = false)
  private JPromotion promotion;

  private boolean published;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
