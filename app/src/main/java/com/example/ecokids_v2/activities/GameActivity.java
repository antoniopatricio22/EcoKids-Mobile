package com.example.ecokids_v2.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecokids_v2.R;
import com.example.ecokids_v2.adapters.GameItemAdapter;
import com.example.ecokids_v2.database.DatabaseHelper;
import com.example.ecokids_v2.models.GameScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity principal do jogo - responsável pela lógica de gameplay.
 * Integrada com banco de dados para salvar pontuações.
 */
public class GameActivity extends AppCompatActivity {

    private View selectedItemView = null;
    private String selectedCategory = "";
    private final Map<Integer, String> drawableToCategory = new HashMap<>();
    private RecyclerView recyclerItems;
    private GameItemAdapter adapter;
    private final List<Integer> drawables = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private Button btnScore, btnTimer;
    private int score = 0;
    private int errorCount = 0;

    private long gameStartTime = 0;

    private MediaPlayer soundClick;
    private MediaPlayer soundCorrect;
    private MediaPlayer soundIncorrect;
    private MediaPlayer musicPlayer;
    private boolean isMusicPlaying = true;
    private ImageView btnSound;
    private ImageView imgFeedback;
    private int selectedPosition = RecyclerView.NO_POSITION;

    // Database
    private DatabaseHelper databaseHelper;
    private int playerId = -1;
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Inicializar views
        btnScore = findViewById(R.id.btnScore);
        btnTimer = findViewById(R.id.btnTimer);
        btnSound = findViewById(R.id.btnSound);
        imgFeedback = findViewById(R.id.imgFeedback);
        recyclerItems = findViewById(R.id.recyclerItems);

        // Inicializar database
        databaseHelper = new DatabaseHelper(this);

        // Obter dados do Intent
        playerId = getIntent().getIntExtra("player_id", -1);
        playerName = getIntent().getStringExtra("player_name");

        if (playerId == -1 || playerName == null) {
            Toast.makeText(this, "Erro ao carregar dados do jogador", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar sons
        soundClick = MediaPlayer.create(this, R.raw.click_sound);
        soundCorrect = MediaPlayer.create(this, R.raw.correct_sound);
        soundIncorrect = MediaPlayer.create(this, R.raw.incorrect_sound);
        musicPlayer = MediaPlayer.create(this, R.raw.background_music);
        if (musicPlayer != null) {
            musicPlayer.setLooping(true);
            musicPlayer.start();
        }

        btnSound.setOnClickListener(v -> toggleMusic());

        setupDrawableCategoryMap();
        setupGameBoard();
        setupBinClickListeners();
        startGameTimer();
    }

    private void toggleMusic() {
        if (musicPlayer == null) return;
        if (isMusicPlaying) {
            musicPlayer.pause();
            btnSound.setImageResource(R.drawable.turn_off_sound);
        } else {
            musicPlayer.start();
            btnSound.setImageResource(R.drawable.turn_on_sound);
        }
        isMusicPlaying = !isMusicPlaying;
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
        Collections.shuffle(drawables);

        adapter = new GameItemAdapter(drawables, (drawableResId, position, itemView) -> {
            selectedItemView = itemView;
            selectedCategory = drawableToCategory.get(drawableResId);
            selectedPosition = position;
            if (soundClick != null) soundClick.start();
            adapter.setSelectedItem(itemView);
        });

        int spanCount = 5;
        recyclerItems.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerItems.setAdapter(adapter);
        recyclerItems.setHasFixedSize(true);
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
            if (selectedItemView == null) {
                Toast.makeText(this, "Selecione um item primeiro", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedCategory.equals(category)) {
                if (soundCorrect != null) soundCorrect.start();
                score++;
                btnScore.setText("Acertos: " + score);
                showFeedback(true);
                removeItemFromBoard();
            } else {
                if (soundIncorrect != null) soundIncorrect.start();
                errorCount++;
                showFeedback(false);
            }
        });
    }

    private void removeItemFromBoard() {
        if (selectedPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (selectedPosition >= 0 && selectedPosition < drawables.size()) {
            drawables.remove(selectedPosition);
            adapter.notifyItemRemoved(selectedPosition);
        }

        selectedItemView = null;
        selectedCategory = "";
        selectedPosition = RecyclerView.NO_POSITION;
        adapter.clearSelection();

        if (drawables.isEmpty()) {
            endGame();
        }
    }

    private void startGameTimer() {
        gameStartTime = System.currentTimeMillis();
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
        if (imgFeedback == null) return;
        imgFeedback.setImageResource(correct ? R.drawable.thumb_up : R.drawable.sad_face);
        imgFeedback.setVisibility(View.VISIBLE);
        imgFeedback.setAlpha(1.0f);
        imgFeedback.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .withEndAction(() -> imgFeedback.setVisibility(View.GONE))
                .start();
    }

    private void endGame() {
        if (countDownTimer != null) countDownTimer.cancel();

        long gameTimeSeconds = 0;
        if (gameStartTime > 0) {
            gameTimeSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
            if (gameTimeSeconds < 0) gameTimeSeconds = 0;
        }

        // Salvar pontuação no banco de dados
        GameScore gameScore = new GameScore(playerId, score, errorCount, gameTimeSeconds);
        long scoreId = databaseHelper.addGameScore(gameScore);

        Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
        intent.putExtra("player_name", playerName);
        intent.putExtra("player_id", playerId);
        intent.putExtra("score", score);
        intent.putExtra("error_count", errorCount);
        intent.putExtra("game_time", gameTimeSeconds);
        intent.putExtra("score_id", scoreId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) countDownTimer.cancel();
        if (soundClick != null) {
            soundClick.release();
            soundClick = null;
        }
        if (soundCorrect != null) {
            soundCorrect.release();
            soundCorrect = null;
        }
        if (soundIncorrect != null) {
            soundIncorrect.release();
            soundIncorrect = null;
        }
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
    }
}
