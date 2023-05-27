package com.example.DataBase;

import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class DataBasePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public DataBasePlayer(String name) {
        this.name = name;
    }
    public DataBasePlayer(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}