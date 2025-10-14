package com.evolting.authservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "permissions")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;    // e.g., "CREATE_QUIZ"

    private String resource;  // e.g., "QUIZ"

    private String action;   // e.g., "CREATE", "READ", "UPDATE", "DELETE"

    private String description;

}
