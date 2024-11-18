package com.ainetdinov.tracker.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "assignee", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Set<Task> tasks = new HashSet<>();

    public void addTask(Task task) {
        tasks.add(task);
        task.setAssignee(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setAssignee(null);
    }

    public void removeTasks() {
        tasks.forEach(task -> task.setAssignee(null));
        tasks.clear();
    }
}
