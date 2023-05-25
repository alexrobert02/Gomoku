package com.example.homework;

import jakarta.persistence.*;

@Entity
@Table(name="game_history")
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="player1_id")
    private Long player1Id;

    @Column(name="player2_id")
    private Long player2Id;

    @Column(name="date")
    private String date;

    @Column(name="result")
    private String result;

    public GameHistory(Long player1Id, Long player2Id, String date, String result) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.date = date;
        this.result = result;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}