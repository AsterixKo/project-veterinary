package com.example.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private Pet pet;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "veterinary_id", nullable = false)
    private Veterinary veterinary;

    @Column(name = "visit_status")
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus;
}
