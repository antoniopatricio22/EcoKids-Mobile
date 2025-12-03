package com.example.ecokids_v2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ecokids_v2.models.Achievement;
import com.example.ecokids_v2.models.GameScore;
import com.example.ecokids_v2.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper para gerenciar todas as operações de banco de dados SQLite.
 * Armazena: Players (nomes únicos), GameScores, e Achievements.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecokids_v2.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela PLAYERS
    private static final String TABLE_PLAYERS = "players";
    private static final String COLUMN_PLAYER_ID = "id";
    private static final String COLUMN_PLAYER_NAME = "name";
    private static final String COLUMN_PLAYER_CREATED_AT = "created_at";

    // Tabela GAME_SCORES
    private static final String TABLE_GAME_SCORES = "game_scores";
    private static final String COLUMN_SCORE_ID = "id";
    private static final String COLUMN_SCORE_PLAYER_ID = "player_id";
    private static final String COLUMN_SCORE_POINTS = "score";
    private static final String COLUMN_SCORE_ERROR_COUNT = "error_count";
    private static final String COLUMN_SCORE_GAME_TIME = "game_time";
    private static final String COLUMN_SCORE_CREATED_AT = "created_at";

    // Tabela ACHIEVEMENTS
    private static final String TABLE_ACHIEVEMENTS = "achievements";
    private static final String COLUMN_ACH_ID = "id";
    private static final String COLUMN_ACH_PLAYER_ID = "player_id";
    private static final String COLUMN_ACH_NAME = "name";
    private static final String COLUMN_ACH_DESCRIPTION = "description";
    private static final String COLUMN_ACH_ICON = "icon";
    private static final String COLUMN_ACH_UNLOCKED_AT = "unlocked_at";

    // SQL para criar tabelas
    private static final String CREATE_TABLE_PLAYERS =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_NAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PLAYER_CREATED_AT + " LONG NOT NULL);";

    private static final String CREATE_TABLE_GAME_SCORES =
            "CREATE TABLE " + TABLE_GAME_SCORES + " (" +
                    COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SCORE_PLAYER_ID + " INTEGER NOT NULL, " +
                    COLUMN_SCORE_POINTS + " INTEGER NOT NULL, " +
                    COLUMN_SCORE_ERROR_COUNT + " INTEGER NOT NULL, " +
                    COLUMN_SCORE_GAME_TIME + " LONG NOT NULL, " +
                    COLUMN_SCORE_CREATED_AT + " LONG NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_SCORE_PLAYER_ID + ") REFERENCES " +
                    TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + ") ON DELETE CASCADE);";

    private static final String CREATE_TABLE_ACHIEVEMENTS =
            "CREATE TABLE " + TABLE_ACHIEVEMENTS + " (" +
                    COLUMN_ACH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ACH_PLAYER_ID + " INTEGER NOT NULL, " +
                    COLUMN_ACH_NAME + " TEXT NOT NULL, " +
                    COLUMN_ACH_DESCRIPTION + " TEXT, " +
                    COLUMN_ACH_ICON + " TEXT, " +
                    COLUMN_ACH_UNLOCKED_AT + " LONG NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_ACH_PLAYER_ID + ") REFERENCES " +
                    TABLE_PLAYERS + "(" + COLUMN_PLAYER_ID + ") ON DELETE CASCADE, " +
                    "UNIQUE(" + COLUMN_ACH_PLAYER_ID + ", " + COLUMN_ACH_NAME + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYERS);
        db.execSQL(CREATE_TABLE_GAME_SCORES);
        db.execSQL(CREATE_TABLE_ACHIEVEMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    // ==================== MÉTODOS PARA PLAYERS ====================

    /**
     * Cria um novo player no banco de dados.
     * @param player Objeto Player
     * @return ID do player criado, ou -1 se falhar
     */
    public long addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, player.getName());
        values.put(COLUMN_PLAYER_CREATED_AT, player.getCreatedAt());

        long result = db.insert(TABLE_PLAYERS, null, values);
        db.close();
        return result;
    }

    /**
     * Verifica se um jogador com este nome já existe.
     * @param name Nome do jogador
     * @return true se existe, false caso contrário
     */
    public boolean playerExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYERS,
                new String[]{COLUMN_PLAYER_ID},
                COLUMN_PLAYER_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Obtém um player pelo nome.
     * @param name Nome do jogador
     * @return Objeto Player ou null se não encontrado
     */
    public Player getPlayerByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYERS,
                null,
                COLUMN_PLAYER_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        Player player = null;
        if (cursor.moveToFirst()) {
            player = new Player(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_NAME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_CREATED_AT))
            );
        }

        cursor.close();
        db.close();
        return player;
    }

    /**
     * Obtém um player pelo ID.
     * @param playerId ID do jogador
     * @return Objeto Player ou null se não encontrado
     */
    public Player getPlayerById(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYERS,
                null,
                COLUMN_PLAYER_ID + " = ?",
                new String[]{String.valueOf(playerId)},
                null,
                null,
                null
        );

        Player player = null;
        if (cursor.moveToFirst()) {
            player = new Player(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_NAME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_CREATED_AT))
            );
        }

        cursor.close();
        db.close();
        return player;
    }

    /**
     * Obtém todos os players cadastrados.
     * @return Lista de Players
     */
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PLAYERS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_PLAYER_CREATED_AT + " DESC"
        );

        while (cursor.moveToNext()) {
            players.add(new Player(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_NAME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_CREATED_AT))
            ));
        }

        cursor.close();
        db.close();
        return players;
    }

    // ==================== MÉTODOS PARA GAME SCORES ====================

    /**
     * Cria um novo record de pontuação.
     * @param gameScore Objeto GameScore
     * @return ID do record criado, ou -1 se falhar
     */
    public long addGameScore(GameScore gameScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_PLAYER_ID, gameScore.getPlayerId());
        values.put(COLUMN_SCORE_POINTS, gameScore.getScore());
        values.put(COLUMN_SCORE_ERROR_COUNT, gameScore.getErrorCount());
        values.put(COLUMN_SCORE_GAME_TIME, gameScore.getGameTime());
        values.put(COLUMN_SCORE_CREATED_AT, gameScore.getCreatedAt());

        long result = db.insert(TABLE_GAME_SCORES, null, values);
        db.close();
        return result;
    }

    /**
     * Obtém todos os scores de um player.
     * @param playerId ID do jogador
     * @return Lista de GameScores
     */
    public List<GameScore> getPlayerScores(int playerId) {
        List<GameScore> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_GAME_SCORES,
                null,
                COLUMN_SCORE_PLAYER_ID + " = ?",
                new String[]{String.valueOf(playerId)},
                null,
                null,
                COLUMN_SCORE_CREATED_AT + " DESC"
        );

        while (cursor.moveToNext()) {
            scores.add(new GameScore(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_PLAYER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_POINTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ERROR_COUNT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_GAME_TIME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_CREATED_AT))
            ));
        }

        cursor.close();
        db.close();
        return scores;
    }

    /**
     * Obtém a melhor pontuação de um player.
     * @param playerId ID do jogador
     * @return GameScore com a melhor pontuação ou null
     */
    public GameScore getHighestScore(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_GAME_SCORES,
                null,
                COLUMN_SCORE_PLAYER_ID + " = ?",
                new String[]{String.valueOf(playerId)},
                null,
                null,
                COLUMN_SCORE_POINTS + " DESC",
                "1"
        );

        GameScore score = null;
        if (cursor.moveToFirst()) {
            score = new GameScore(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_PLAYER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_POINTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ERROR_COUNT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_GAME_TIME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_CREATED_AT))
            );
        }

        cursor.close();
        db.close();
        return score;
    }

    // ==================== MÉTODOS PARA ACHIEVEMENTS ====================

    /**
     * Adiciona uma conquista para um player.
     * @param achievement Objeto Achievement
     * @return ID da conquista criada, ou -1 se falhar
     */
    public long addAchievement(Achievement achievement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACH_PLAYER_ID, achievement.getPlayerId());
        values.put(COLUMN_ACH_NAME, achievement.getName());
        values.put(COLUMN_ACH_DESCRIPTION, achievement.getDescription());
        values.put(COLUMN_ACH_ICON, achievement.getIcon());
        values.put(COLUMN_ACH_UNLOCKED_AT, achievement.getUnlockedAt());

        long result = db.insert(TABLE_ACHIEVEMENTS, null, values);
        db.close();
        return result;
    }

    /**
     * Verifica se um player já desbloqueou uma conquista.
     * @param playerId ID do jogador
     * @param achievementName Nome da conquista
     * @return true se desbloqueada, false caso contrário
     */
    public boolean hasAchievement(int playerId, String achievementName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_ACHIEVEMENTS,
                new String[]{COLUMN_ACH_ID},
                COLUMN_ACH_PLAYER_ID + " = ? AND " + COLUMN_ACH_NAME + " = ?",
                new String[]{String.valueOf(playerId), achievementName},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Obtém todas as conquistas de um player.
     * @param playerId ID do jogador
     * @return Lista de Achievements
     */
    public List<Achievement> getPlayerAchievements(int playerId) {
        List<Achievement> achievements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_ACHIEVEMENTS,
                null,
                COLUMN_ACH_PLAYER_ID + " = ?",
                new String[]{String.valueOf(playerId)},
                null,
                null,
                COLUMN_ACH_UNLOCKED_AT + " ASC"
        );

        while (cursor.moveToNext()) {
            achievements.add(new Achievement(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACH_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACH_PLAYER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACH_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACH_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACH_ICON)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ACH_UNLOCKED_AT))
            ));
        }

        cursor.close();
        db.close();
        return achievements;
    }
}
