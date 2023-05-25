package com.example.homework;

import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class Players {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    public Players(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Players(){}

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