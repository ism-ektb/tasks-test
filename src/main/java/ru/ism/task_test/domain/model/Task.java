package ru.ism.task_test.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "tasks")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOfTask status = StatusOfTask.IN_WAITING;
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private PriorityOfTask priority;
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable (name = "performers_users",
            joinColumns = @JoinColumn (name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> performers;
}
