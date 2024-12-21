package com.example.cambiosformatokuka.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cambiosformatokuka.R;
import com.example.cambiosformatokuka.models.Guia;

import java.util.List;

public class GuiaAdapter extends RecyclerView.Adapter<GuiaAdapter.GuiaViewHolder> {

    private List<Guia> guias;
    private final OnGuiaClickListener listener;

    public interface OnGuiaClickListener {
        void onGuiaClick(Guia guia);
    }

    public GuiaAdapter(List<Guia> guias, OnGuiaClickListener listener) {
        this.guias = guias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GuiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guia, parent, false);
        return new GuiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuiaViewHolder holder, int position) {
        Guia guia = guias.get(position);
        holder.descripcionTextView.setText(guia.getDescripcion());
        holder.itemView.setOnClickListener(v -> listener.onGuiaClick(guia));
    }

    @Override
    public int getItemCount() {
        return guias.size();
    }

    public static class GuiaViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionTextView;

        public GuiaViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcionTextView = itemView.findViewById(R.id.textViewDescripcionGuia);
        }
    }
}
