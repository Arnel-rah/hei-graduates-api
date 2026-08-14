package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "semester")
@Getter
@Setter
@NoArgsConstructor
public class JSemester {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String label;
  private int order;

  @ManyToOne
  @JoinColumn(name = "promotion_id", nullable = false)
  private JPromotion promotion;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
