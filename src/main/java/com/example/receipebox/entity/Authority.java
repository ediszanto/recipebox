package com.example.receipebox.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "authority", schema = "public")
@Data
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
