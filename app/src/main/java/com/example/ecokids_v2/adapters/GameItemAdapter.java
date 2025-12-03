package com.example.ecokids_v2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecokids_v2.R;

import java.util.List;

/**
 * Adapter para exibir os itens de lixo na grade do jogo.
 */
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
        int drawableResId = drawables.get(position);
        holder.imageView.setImageResource(drawableResId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
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

    /**
     * Define o item selecionado e atualiza a visualização.
     * @param itemView View do item selecionado
     */
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

    /**
     * Limpa a seleção atual.
     */
    public void clearSelection() {
        setSelectedItem(null);
    }

    /**
     * Encontra a posição de uma view na lista.
     * @param viewToFind View a ser encontrada
     * @return Posição da view ou RecyclerView.NO_POSITION se não encontrado
     */
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

    /**
     * ViewHolder para os itens de lixo.
     */
    static class GameItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public GameItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
