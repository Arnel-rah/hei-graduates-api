package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class JStudent {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;
  private String firstName;
  private String ref;

  @OneToOne
  @JoinColumn(name = "account_id", nullable = false, unique = true)
  private JAccount account;

  @ManyToOne
  @JoinColumn(name = "promotion_id")
  private JPromotion promotion;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
