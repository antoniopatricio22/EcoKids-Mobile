package com.example.ecokids_v2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class GameSelectionDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_game_selection, null);

        EditText etPlayerName = view.findViewById(R.id.etPlayerName);
        Button btnStartGame = view.findViewById(R.id.btnStartGame);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),
                androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog);
        builder.setView(view);

        Dialog dialog = builder.create();

        btnStartGame.setOnClickListener(v -> {
            String name = etPlayerName.getText().toString().trim();
            if (name.isEmpty()) {
                etPlayerName.setError("Digite seu nome");
                return;
            }

            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra("player_name", name);
            startActivity(intent);
            dialog.dismiss();
        });

        return dialog;
    }
}