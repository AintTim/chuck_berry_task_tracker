package com.ainetdinov.tracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
