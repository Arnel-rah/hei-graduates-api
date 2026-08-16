package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class JCourse {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String ref;
  private String title;
  private int credits;
  private boolean mandatory;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
