package com.example.ecokids_v2.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.ecokids_v2.R;
import com.example.ecokids_v2.activities.GameActivity;
import com.example.ecokids_v2.database.DatabaseHelper;
import com.example.ecokids_v2.models.Player;

/**
 * Dialog para seleção e validação de nome do jogador.
 * Garante que não haja duplicação de nomes no banco de dados.
 */
public class GameSelectionDialog extends DialogFragment {

    private DatabaseHelper databaseHelper;
    private EditText etPlayerName;
    private Button btnStartGame;
    private ProgressBar progressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_game_selection, null);

        // Inicializar views
        etPlayerName = view.findViewById(R.id.etPlayerName);
        btnStartGame = view.findViewById(R.id.btnStartGame);
        progressBar = view.findViewById(R.id.progressBar);

        // Inicializar DatabaseHelper
        databaseHelper = new DatabaseHelper(requireActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),
                androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog);
        builder.setView(view);

        Dialog dialog = builder.create();

        btnStartGame.setOnClickListener(v -> handleGameStart(dialog));

        return dialog;
    }

    /**
     * Valida e processa o início do jogo.
     * @param dialog Dialog a ser fechado após sucesso
     */
    private void handleGameStart(Dialog dialog) {
        String name = etPlayerName.getText().toString().trim();

        // Validações
        if (name.isEmpty()) {
            etPlayerName.setError("Digite seu nome");
            return;
        }

        if (name.length() < 2) {
            etPlayerName.setError("O nome deve ter pelo menos 2 caracteres");
            return;
        }

        if (name.length() > 50) {
            etPlayerName.setError("O nome não pode ter mais de 50 caracteres");
            return;
        }

        // Validar caracteres permitidos (letras, números, espaços, hífens)
        if (!name.matches("[a-zA-Z0-9\\s-]+")) {
            etPlayerName.setError("O nome contém caracteres inválidos");
            return;
        }

        // Mostrar progresso
        progressBar.setVisibility(View.VISIBLE);
        btnStartGame.setEnabled(false);

        // Verificar se o player já existe
        if (databaseHelper.playerExists(name)) {
            // Player já existe, mostrar mensagem
            Toast.makeText(getActivity(), 
                "Bem-vindo de volta, " + name + "!", 
                Toast.LENGTH_SHORT).show();
            
            // Apenas obter seu ID
            Player existingPlayer = databaseHelper.getPlayerByName(name);
            if (existingPlayer != null) {
                startGame(name, existingPlayer.getId(), dialog);
            } else {
                progressBar.setVisibility(View.GONE);
                btnStartGame.setEnabled(true);
                Toast.makeText(getActivity(), "Erro ao recuperar dados do jogador", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Novo player, criar na base de dados
            Player newPlayer = new Player(name);
            long playerId = databaseHelper.addPlayer(newPlayer);

            if (playerId != -1) {
                    Toast.makeText(getActivity(), 
                        "Bem-vindo ao jogo, " + name + "!", 
                        Toast.LENGTH_SHORT).show();
                startGame(name, (int) playerId, dialog);
            } else {
                progressBar.setVisibility(View.GONE);
                btnStartGame.setEnabled(true);
                Toast.makeText(getActivity(), "Erro ao criar novo jogador", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Inicia o jogo com os dados do player.
     * @param name Nome do player
     * @param playerId ID do player no banco de dados
     * @param dialog Dialog a ser fechado
     */
    private void startGame(String name, int playerId, Dialog dialog) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        intent.putExtra("player_name", name);
        intent.putExtra("player_id", playerId);
        startActivity(intent);
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            // O DatabaseHelper é um helper, não precisa fechar explicitamente
        }
    }
}
