package com.example.ecokids_v2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Intent;

import com.example.ecokids_v2.R;
import com.example.ecokids_v2.dialogs.GameSelectionDialog;

/**
 * MainActivity - Tela inicial do jogo com menu principal.
 * Permite ao usuário escolher entre jogar, aprender ou ver conquistas.
 */
public class MainActivity extends AppCompatActivity {

    private Button btnAprender, btnConquistas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAprender = findViewById(R.id.btnAprender);
        btnConquistas = findViewById(R.id.btnConquistas);

        // Abrir dialog de seleção de jogo ao clicar na área "Jogar"
        View logoPlay = findViewById(R.id.imgRecycle);
        logoPlay.setOnClickListener(v -> {
            GameSelectionDialog dialog = new GameSelectionDialog();
            dialog.show(getSupportFragmentManager(), "gameDialog");
        });

        btnAprender.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LearnActivity.class)));

        btnConquistas.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AchievementsActivity.class)));
    }
}
