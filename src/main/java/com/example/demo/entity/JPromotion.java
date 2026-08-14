package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
public class JPromotion {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String label;
  private Instant startYear;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
