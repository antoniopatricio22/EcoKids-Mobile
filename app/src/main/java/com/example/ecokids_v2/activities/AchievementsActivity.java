package com.example.ecokids_v2.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ecokids_v2.R;

/**
 * AchievementsActivity - Tela de visualização de conquistas.
 * Exibe todas as conquistas desbloqueadas pelo jogador.
 */
public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
    }
}
