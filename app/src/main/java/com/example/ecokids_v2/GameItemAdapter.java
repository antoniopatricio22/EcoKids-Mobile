package com.example.ecokids_v2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameItemAdapter extends RecyclerView.Adapter<GameItemAdapter.GameItemViewHolder> {

    private final List<Integer> drawables;
    private final OnItemClickListener listener;
    private View selectedItemView = null;

    public interface OnItemClickListener {
        void onItemClick(int drawableResId, int position, View itemView);
    }

    public GameItemAdapter(List<Integer> drawables, OnItemClickListener listener) {
        this.drawables = drawables;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameItemViewHolder holder, int position) {
        // FIX 1: Changed 'items' to 'drawables'
        int drawableResId = drawables.get(position);
        // FIX 3: Changed 'imgItem' to 'imageView' to match the ViewHolder definition
        holder.imageView.setImageResource(drawableResId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    // FIX 2: Changed 'items' to 'drawables'
                    int clickedResId = drawables.get(pos);
                    listener.onItemClick(clickedResId, pos, holder.itemView);
                }
            }
        });

        if (holder.itemView == selectedItemView) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return drawables.size();
    }


    public void setSelectedItem(View itemView) {
        View previousSelectedItem = selectedItemView;
        selectedItemView = itemView;

        // Remove o destaque do item anterior, se houver
        if (previousSelectedItem != null) {
            int previousPosition = findPositionForView(previousSelectedItem);
            if (previousPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousPosition);
            }
        }
        // Adiciona destaque ao novo item
        if (selectedItemView != null) {
            int newPosition = findPositionForView(selectedItemView);
            if (newPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(newPosition);
            }
        }
    }

    // Limpa a seleção
    public void clearSelection() {
        setSelectedItem(null);
    }

    private int findPositionForView(View viewToFind) {
        if (viewToFind == null) {
            return RecyclerView.NO_POSITION;
        }
        RecyclerView rv = (RecyclerView) viewToFind.getParent();
        if (rv == null) {
            return RecyclerView.NO_POSITION;
        }

        RecyclerView.ViewHolder vh = rv.getChildViewHolder(viewToFind);
        if (vh != null) {
            return vh.getAdapterPosition();
        }
        return RecyclerView.NO_POSITION;
    }


    static class GameItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public GameItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // O ID da ImageView dentro de item_game.xml é item_image
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
