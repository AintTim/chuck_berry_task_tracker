package com.ainetdinov.tracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "labels")
public class Label implements Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label_name", unique = true, nullable = false)
    private String label;

    @Column(name = "label_color")
    private String color = "#808080";

    @ToString.Exclude
    @ManyToMany(mappedBy = "labels", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Task> tasks = new ArrayList<>();
}
