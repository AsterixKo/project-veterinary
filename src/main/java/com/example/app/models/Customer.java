package com.example.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "person_id")
public class Customer extends Person {

    private String address;
    private String city;
    private String telephone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="pet_id", referencedColumnName = "id")
    private Pet pet;
}
