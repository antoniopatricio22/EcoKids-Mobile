package com.example.ecokids_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView tvPlayerName, tvPlayerScore, tvUnlockedAchievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tvPlayerName = findViewById(R.id.tvPlayerName);
        tvPlayerScore = findViewById(R.id.tvPlayerScore);
        tvUnlockedAchievements = findViewById(R.id.tvUnlockedAchievements);

        String name = getIntent().getStringExtra("player_name");
        int score = getIntent().getIntExtra("score", 0);

        tvPlayerName.setText(name);
        tvPlayerScore.setText(score + " pontos");


        if (score == 25) {
            tvUnlockedAchievements.setText("Reciclador Nota 100!\nVocê acertou todos os itens!");
        } else {
            tvUnlockedAchievements.setText("Nenhuma conquista desbloqueada ainda.");
        }

        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

    }
}