package com.example.ecokids_v2;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class GameActivity extends AppCompatActivity {

    private View selectedItem = null;
    private String selectedCategory = "";
    private final Map<Integer, String> drawableToCategory = new HashMap<>();
    private final List<Integer> drawables = new ArrayList<>();
    private final Map<View, Integer> viewToDrawable = new HashMap<>();

    private CountDownTimer countDownTimer;
    private Button btnScore, btnTimer;
    private int score = 0;

    private MediaPlayer soundClick;
    private MediaPlayer soundCorrect;

    private MediaPlayer soundIncorrect;

    private MediaPlayer musicPlayer;
    private boolean isMusicPlaying = true;
    private ImageView btnSound;

    private ImageView imgFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnScore = findViewById(R.id.btnScore);
        btnTimer = findViewById(R.id.btnTimer);
        btnSound = findViewById(R.id.btnSound);

        soundClick = MediaPlayer.create(this, R.raw.click_sound);
        soundCorrect = MediaPlayer.create(this, R.raw.correct_sound);
        soundIncorrect = MediaPlayer.create(this, R.raw.incorrect_sound);

        // Inicializa música de fundo
        musicPlayer = MediaPlayer.create(this, R.raw.background_music);
        musicPlayer.setLooping(true);
        musicPlayer.start();

        btnSound.setOnClickListener(v -> {
            if (isMusicPlaying) {
                musicPlayer.pause();
                btnSound.setImageResource(R.drawable.turn_off_sound);
            } else {
                musicPlayer.start();
                btnSound.setImageResource(R.drawable.turn_on_sound);
            }
            isMusicPlaying = !isMusicPlaying;
        });

        setupDrawableCategoryMap();
        setupGameBoard();
        setupBinClickListeners();
        startGameTimer();
    }

    private void setupDrawableCategoryMap() {
        drawableToCategory.put(R.drawable.item_banana, "organico");
        drawableToCategory.put(R.drawable.item_caderno, "papel");
        drawableToCategory.put(R.drawable.item_caixa_papelao, "papel");
        drawableToCategory.put(R.drawable.item_caixinha_suco, "papel");
        drawableToCategory.put(R.drawable.item_cenoura, "organico");
        drawableToCategory.put(R.drawable.item_chave_boca, "metal");
        drawableToCategory.put(R.drawable.item_colher_metal, "metal");
        drawableToCategory.put(R.drawable.item_copo_descartavel_plastico, "plastico");
        drawableToCategory.put(R.drawable.item_copo_leite, "plastico");
        drawableToCategory.put(R.drawable.item_dispenser_plastico, "plastico");
        drawableToCategory.put(R.drawable.item_escova_dentes, "plastico");
        drawableToCategory.put(R.drawable.item_espiga_milho, "organico");
        drawableToCategory.put(R.drawable.item_folha_jornal, "papel");
        drawableToCategory.put(R.drawable.item_lata_tomate, "metal");
        drawableToCategory.put(R.drawable.item_folha_papel, "papel");
        drawableToCategory.put(R.drawable.item_livros, "papel");
        drawableToCategory.put(R.drawable.item_maca_fruta, "organico");
        drawableToCategory.put(R.drawable.item_mamadeira, "plastico");
        drawableToCategory.put(R.drawable.item_moeda, "metal");
        drawableToCategory.put(R.drawable.item_pao_caixa, "organico");
        drawableToCategory.put(R.drawable.item_uvas, "organico");
        drawableToCategory.put(R.drawable.item_sino_dourado, "metal");
        drawableToCategory.put(R.drawable.item_pote_creme, "plastico");
        drawableToCategory.put(R.drawable.item_papel_higienico, "papel");
        drawableToCategory.put(R.drawable.item_pote_vidro, "vidro");

        drawables.addAll(drawableToCategory.keySet());
    }

    private void setupGameBoard() {
        GridLayout grid = findViewById(R.id.gridItems);
        List<View> items = new ArrayList<>();

        TypedArray itemIds = getResources().obtainTypedArray(R.array.game_item_ids);

        for (int i = 0; i < itemIds.length(); i++) {
            int resId = itemIds.getResourceId(i, 0); // Pega o ID do item no array
            if (resId != 0) {
                ImageView itemView = findViewById(resId);
                if (itemView != null) {
                    items.add(itemView);
                }
            }
        }

        itemIds.recycle();

        Collections.shuffle(drawables);

        for (int i = 0; i < items.size(); i++) {
            ImageView item = (ImageView) items.get(i);
            int drawable = drawables.get(i);
            item.setImageResource(drawable);
            item.setVisibility(View.VISIBLE);
            viewToDrawable.put(item, drawable);

            item.setOnClickListener(v -> {
                selectedItem = v;
                selectedCategory = drawableToCategory.get(viewToDrawable.get(v));
                if (soundClick != null) soundClick.start();
            });
        }

        Collections.shuffle(items);
        grid.removeAllViews();
        for (View view : items) {
            grid.addView(view);
        }
    }

    private void setupBinClickListeners() {
        setupBin(R.id.binPaper, "papel");
        setupBin(R.id.binPlastic, "plastico");
        setupBin(R.id.binGlass, "vidro");
        setupBin(R.id.binMetal, "metal");
        setupBin(R.id.binOrganic, "organico");
    }

    private void setupBin(int binId, String category) {
        View bin = findViewById(binId);
        bin.setOnClickListener(v -> {
            if (selectedItem == null) {
                Toast.makeText(this, "Selecione um item primeiro", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedCategory.equals(category)) {
                soundCorrect.start();
                score++;
                btnScore.setText("Acertos: " + score);
                removeItemAndCollapse(selectedItem);
                //showFeedback(true);
                selectedItem = null;
                selectedCategory = "";
            } else {
                //showFeedback(false);
                soundIncorrect.start();
                Toast.makeText(this, "Errado!.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeItemAndCollapse(View itemToRemove) {
        GridLayout grid = findViewById(R.id.gridItems);
        grid.removeView(itemToRemove);
        viewToDrawable.remove(itemToRemove);

        List<View> remaining = new ArrayList<>();
        for (int i = 0; i < grid.getChildCount(); i++) {
            remaining.add(grid.getChildAt(i));
        }

        grid.removeAllViews();
        for (View v : remaining) {
            grid.addView(v);
        }

        if (remaining.isEmpty()) {
            endGame(); // vitória
        }
    }

    private void startGameTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnTimer.setText("Tempo: " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                btnTimer.setText("Tempo: 0s");
                endGame();
            }
        }.start();
    }

    private void showFeedback(boolean correct) {
        imgFeedback.setImageResource(correct ? R.drawable.thumb_up : R.drawable.sad_face);
        imgFeedback.setVisibility(View.VISIBLE);
        imgFeedback.setScaleX(0f);
        imgFeedback.setScaleY(0f);
        imgFeedback.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(200)
                .withEndAction(() -> imgFeedback.postDelayed(() -> {
                    imgFeedback.animate().scaleX(0f).scaleY(0f)
                            .setDuration(200)
                            .withEndAction(() -> imgFeedback.setVisibility(View.GONE))
                            .start();
                }, 500))
                .start();
    }


    private void endGame() {
        if (countDownTimer != null) countDownTimer.cancel();

        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        finish(); // fecha a partida atual
    }

    @Override
    protected void onDestroy() {
        if (soundClick != null) soundClick.release();
        if (soundCorrect != null) soundCorrect.release();
        if (countDownTimer != null) countDownTimer.cancel();
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
        }
        super.onDestroy();
    }
}
