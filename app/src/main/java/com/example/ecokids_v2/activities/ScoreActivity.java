package com.example.ecokids_v2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecokids_v2.R;
import com.example.ecokids_v2.database.DatabaseHelper;
import com.example.ecokids_v2.models.Achievement;

import java.util.List;

/**
 * Activity que exibe a pontuação final e as conquistas desbloqueadas.
 * Integrada com banco de dados para salvar e carregar conquistas.
 */
public class ScoreActivity extends AppCompatActivity {

    private TextView tvPlayerName, tvPlayerScore, tvUnlockedAchievements;
    private DatabaseHelper databaseHelper;
    private int playerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Inicializar database
        databaseHelper = new DatabaseHelper(this);

        // Inicializar views
        tvPlayerName = findViewById(R.id.tvPlayerName);
        tvPlayerScore = findViewById(R.id.tvPlayerScore);
        tvUnlockedAchievements = findViewById(R.id.tvUnlockedAchievements);

        // Obter dados do Intent
        String name = getIntent().getStringExtra("player_name");
        playerId = getIntent().getIntExtra("player_id", -1);
        int score = getIntent().getIntExtra("score", 0);
        int errorCount = getIntent().getIntExtra("error_count", 0);
        long gameTime = getIntent().getLongExtra("game_time", 0);

        if (playerId == -1) {
            Toast.makeText(this, "Erro ao carregar dados do jogador", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvPlayerName.setText(name);
        tvPlayerScore.setText(score + " pontos");

        // Processar e salvar conquistas
        processAchievements(score, errorCount, gameTime);

        // Obter e exibir conquistas salvas no banco de dados
        displaySavedAchievements();

        // Configurar botão de reiniciar
        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Processa as conquistas da partida atual e salva no banco de dados.
     * @param score Pontuação final
     * @param errorCount Número de erros cometidos
     * @param gameTime Tempo de jogo em segundos
     */
    private void processAchievements(int score, int errorCount, long gameTime) {
        // Limitar para as 3 conquistas ativas (correspondentes aos 3 cards):
        // 1) Campeão da Reciclagem: sem erros e pontuação máxima
        if (errorCount == 0 && score == 25) {
            unlockAchievement(
                    "Campeão da Reciclagem",
                    "Finalize o jogo sem cometer nenhum erro. Um verdadeiro herói do meio ambiente!",
                    "🏆"
            );
        }

        // 2) Reciclador Nota 100: acertou todos os itens (pontuação máxima)
        if (score == 25) {
            unlockAchievement(
                    "Reciclador Nota 100",
                    "Coloque corretamente todos os itens nas suas lixeiras correspondentes.",
                    "⭐"
            );
        }

        // 3) Velocista Sustentável: completar em menos de 60 segundos
        if (gameTime < 60) {
            unlockAchievement(
                    "Velocista Sustentável",
                    "Complete o jogo em menos de 1 minuto e mostre que você é rápido e eficiente!",
                    "⚡"
            );
        }
    }

    /**
     * Desbloqueia uma conquista (se ainda não estiver desbloqueada).
     * @param name Nome da conquista
     * @param description Descrição da conquista
     * @param icon Ícone ou emoji da conquista
     */
    private void unlockAchievement(String name, String description, String icon) {
        if (!databaseHelper.hasAchievement(playerId, name)) {
            Achievement achievement = new Achievement(playerId, name, description, icon);
            long result = databaseHelper.addAchievement(achievement);
            if (result == -1) {
                Toast.makeText(this, "Erro ao salvar conquista", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Exibe todas as conquistas salvas do player.
     */
    private void displaySavedAchievements() {
        List<Achievement> achievements = databaseHelper.getPlayerAchievements(playerId);

        if (achievements.isEmpty()) {
            tvUnlockedAchievements.setText("Nenhuma conquista desbloqueada ainda. Tente novamente!");
            return;
        }

        StringBuilder achievementsText = new StringBuilder();
        for (Achievement achievement : achievements) {
            achievementsText.append(achievement.getIcon())
                    .append(" ")
                    .append(achievement.getName())
                    .append("\n")
                    .append(achievement.getDescription())
                    .append("\n\n");
        }

        tvUnlockedAchievements.setText(achievementsText.toString().trim());
    }
}
