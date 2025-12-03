package com.example.ecokids_v2.models;

/**
 * Modelo para representar um jogador no banco de dados.
 */
public class Player {
    private int id;
    private String name;
    private long createdAt;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }

    public Player(int id, String name, long createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
