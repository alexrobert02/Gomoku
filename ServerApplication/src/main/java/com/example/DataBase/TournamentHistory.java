package com.example.DataBase;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tournament_history")
public class TournamentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="lobby_name")
    private String name;

    @Column(name="player1_id")
    private Long player1Id;

    @Column(name="player2_id")
    private Long player2Id;
    @Column(name="player3_id")
    private Long player3Id;
    @Column(name="player4_id")
    private Long player4Id;
    @Column(name="player5_id")
    private Long player5Id;
    @Column(name="player6_id")
    private Long player6Id;
    @Column(name="player7_id")
    private Long player7Id;
    @Column(name="player8_id")
    private Long player8Id;

    @Column(name="date")
    private LocalDateTime date;

    @Column(name="result")
    private String result;
    @Column(name="status")
    private String status;
    public TournamentHistory(String name,Long player1Id, LocalDateTime date, String result, String status) {
        this.name=name;
        this.player1Id = player1Id;
        this.player2Id = null;
        this.player3Id = null;
        this.player4Id = null;
        this.player5Id = null;
        this.player6Id = null;
        this.player7Id = null;
        this.player8Id = null;
        this.date = date;
        this.result = result;
        this.status=status;
    }

    public TournamentHistory() {}

    public Long getId() {
        return id;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public Long getPlayer3Id() {
        return player3Id;
    }

    public Long getPlayer4Id() {
        return player4Id;
    }

    public Long getPlayer5Id() {
        return player5Id;
    }

    public Long getPlayer6Id() {
        return player6Id;
    }

    public Long getPlayer7Id() {
        return player7Id;
    }

    public Long getPlayer8Id() {
        return player8Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPlayer3Id(Long player3Id) {
        this.player3Id = player3Id;
    }

    public void setPlayer4Id(Long player4Id) {
        this.player4Id = player4Id;
    }

    public void setPlayer5Id(Long player5Id) {
        this.player5Id = player5Id;
    }

    public void setPlayer6Id(Long player6Id) {
        this.player6Id = player6Id;
    }

    public void setPlayer7Id(Long player7Id) {
        this.player7Id = player7Id;
    }

    public void setPlayer8Id(Long player8Id) {
        this.player8Id = player8Id;
    }
}