package com.example.DataBase;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="game_history")
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="lobby_name")
    private String name;

    @Column(name="player1_id")
    private Long player1Id;

    @Column(name="player2_id")
    private Long player2Id;

    @Column(name="date")
    private LocalDateTime date;

    @Column(name="result")
    private String result;
    @Column(name="status")
    private String status;
    public GameHistory(String name,Long player1Id, Long player2Id, LocalDateTime date, String result, String status) {
        this.name=name;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.date = date;
        this.result = result;
        this.status=status;
    }

    public GameHistory() {}

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
}