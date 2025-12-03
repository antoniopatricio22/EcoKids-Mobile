package com.example.ecokids_v2.models;

/**
 * Modelo para armazenar as conquistas do jogador.
 */
public class Achievement {
    private int id;
    private int playerId;
    private String name;
    private String description;
    private String icon;
    private long unlockedAt;

    public Achievement() {
    }

    public Achievement(int playerId, String name, String description, String icon) {
        this.playerId = playerId;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.unlockedAt = System.currentTimeMillis();
    }

    public Achievement(int id, int playerId, String name, String description, String icon, long unlockedAt) {
        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.unlockedAt = unlockedAt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(long unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", unlockedAt=" + unlockedAt +
                '}';
    }
}
