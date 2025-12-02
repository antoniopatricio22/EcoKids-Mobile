package com.example.ecokids_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private Button btnScore, btnTimer;
    private GridLayout gridItems;
    private LinearLayout binPaper, binPlastic, binGlass, binMetal, binOrganic;

    private CountDownTimer timer;
    private int timeLeft = 60; // segundos
    private int score = 0;

    // id do item selecionado
    private View selectedItem = null;

    // categoria do item selecionado
    private String selectedCategory = null;

    // mapa simples: idView -> categoria (papel, plastico, etc)
    private final Map<Integer, String> itemCategories = new HashMap<>();

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnScore = findViewById(R.id.btnScore);
        btnTimer = findViewById(R.id.btnTimer);
        gridItems = findViewById(R.id.gridItems);

        binPaper = findViewById(R.id.binPaper);
        binPlastic = findViewById(R.id.binPlastic);
        binGlass = findViewById(R.id.binGlass);
        binMetal = findViewById(R.id.binMetal);
        binOrganic = findViewById(R.id.binOrganic);

        playerName = getIntent().getStringExtra("player_name");

        setupItems();
        setupBins();
        startTimer();
    }

    private void setupItems() {
        View.OnClickListener itemClickListener = v -> {
            selectedItem = v;
            selectedCategory = itemCategories.get(v.getId());
            Toast.makeText(this, "Item selecionado: " + selectedCategory, Toast.LENGTH_SHORT).show();
        };

        for (int i = 1; i <= 25; i++) {
            String itemId = "item" + i;
            int resId = getResources().getIdentifier(itemId, "id", getPackageName());
            ImageView item = findViewById(resId);
            if (item != null) {
                item.setOnClickListener(itemClickListener);
            }
        }

        // Define categorias de exemplo (preencha conforme seu jogo)
        itemCategories.put(R.id.item1, "organico");
        itemCategories.put(R.id.item2, "papel");
        itemCategories.put(R.id.item3, "papel");
        itemCategories.put(R.id.item4, "papel");
        itemCategories.put(R.id.item5, "organico");

        itemCategories.put(R.id.item6, "metal");
        itemCategories.put(R.id.item7, "metal");
        itemCategories.put(R.id.item8, "plastico");
        itemCategories.put(R.id.item9, "plastico");
        itemCategories.put(R.id.item10, "plastico");

        itemCategories.put(R.id.item11, "plastico");
        itemCategories.put(R.id.item12, "organico");
        itemCategories.put(R.id.item13, "papel");
        itemCategories.put(R.id.item14, "metal");
        itemCategories.put(R.id.item15, "papel");

        itemCategories.put(R.id.item16, "papel");
        itemCategories.put(R.id.item17, "organico");
        itemCategories.put(R.id.item18, "plastico");
        itemCategories.put(R.id.item19, "metal");
        itemCategories.put(R.id.item20, "organico");

        itemCategories.put(R.id.item21, "organico");
        itemCategories.put(R.id.item22, "metal");
        itemCategories.put(R.id.item23, "plastico");
        itemCategories.put(R.id.item24, "papel");
        itemCategories.put(R.id.item25, "vidro");
    }

    private void setupBins() {
        View.OnClickListener binClickListener = v -> {
            if (selectedItem == null || selectedCategory == null) {
                Toast.makeText(this, "Selecione um item primeiro!", Toast.LENGTH_SHORT).show();
                return;
            }

            String binCategory = null;
            int id = v.getId();

            if (id == R.id.binPaper) binCategory = "papel";
            else if (id == R.id.binPlastic) binCategory = "plastico";
            else if (id == R.id.binGlass) binCategory = "vidro";
            else if (id == R.id.binMetal) binCategory = "metal";
            else if (id == R.id.binOrganic) binCategory = "organico";

            if (binCategory != null && binCategory.equals(selectedCategory)) {
                score++;
                btnScore.setText("Acertos: " + score);
                selectedItem.setVisibility(View.INVISIBLE);
                selectedItem = null;
                selectedCategory = null;
                Toast.makeText(this, "Acertou!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ops, lixeira errada!", Toast.LENGTH_SHORT).show();
            }
        };

        binPaper.setOnClickListener(binClickListener);
        binPlastic.setOnClickListener(binClickListener);
        binGlass.setOnClickListener(binClickListener);
        binMetal.setOnClickListener(binClickListener);
        binOrganic.setOnClickListener(binClickListener);
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeft * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft--;
                btnTimer.setText("Tempo: " + timeLeft + "s");
            }

            @Override
            public void onFinish() {
                btnTimer.setText("Tempo: 0s");
                endGame();
            }
        }.start();
    }

    private void endGame() {
        if (timer != null) timer.cancel();

        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
        intent.putExtra("player_name", playerName);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}