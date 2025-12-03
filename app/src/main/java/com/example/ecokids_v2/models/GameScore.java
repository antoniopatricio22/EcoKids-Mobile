package com.example.ecokids_v2.models;

/**
 * Modelo para armazenar a pontuação de cada partida.
 */
public class GameScore {
    private int id;
    private int playerId;
    private int score;
    private int errorCount;
    private long gameTime; // em segundos
    private long createdAt;

    public GameScore() {
    }

    public GameScore(int playerId, int score, int errorCount, long gameTime) {
        this.playerId = playerId;
        this.score = score;
        this.errorCount = errorCount;
        this.gameTime = gameTime;
        this.createdAt = System.currentTimeMillis();
    }

    public GameScore(int id, int playerId, int score, int errorCount, long gameTime, long createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.score = score;
        this.errorCount = errorCount;
        this.gameTime = gameTime;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GameScore{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", score=" + score +
                ", errorCount=" + errorCount +
                ", gameTime=" + gameTime +
                ", createdAt=" + createdAt +
                '}';
    }
}
