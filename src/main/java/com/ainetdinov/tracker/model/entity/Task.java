package com.ainetdinov.tracker.model.entity;

import com.ainetdinov.tracker.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task implements Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    private String description;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ToString.Exclude
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_to_label",
            joinColumns = {
                    @JoinColumn(name = "task_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "label_id", referencedColumnName = "id")
            }
    )
    private List<Label> labels = new ArrayList<>();

    public Task(String title, String description, Status status, User assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setTask(null);
    }

    public void removeComments() {
        comments.forEach(comment -> comment.setTask(null));
        comments.clear();
    }

    public void setComments(List<Comment> comments) {
        removeComments();
        comments.forEach(this::addComment);
    }

    public void setLabels(List<Label> labels) {
        removeLabels();
        labels.forEach(this::addLabel);
    }

    public void addLabel(Label label) {
        labels.add(label);
        label.getTasks().add(this);
    }

    public void removeLabels() {
        labels.forEach(label -> label.getTasks().remove(this));
        labels.clear();
    }
}
