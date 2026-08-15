package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "course_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class JCourseAssignment {

  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private JCourse course;

  @ManyToOne
  @JoinColumn(name = "teacher_id", nullable = false)
  private JTeacher teacher;

  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private JGroup group;

  @ManyToOne
  @JoinColumn(name = "semester_id", nullable = false)
  private JSemester semester;

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }
}
