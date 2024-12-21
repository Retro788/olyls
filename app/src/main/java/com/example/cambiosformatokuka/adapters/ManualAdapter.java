package com.example.cambiosformatokuka.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cambiosformatokuka.ManualActivity;
import com.example.cambiosformatokuka.R;
import com.example.cambiosformatokuka.VerManualActivity;
import com.example.cambiosformatokuka.models.Manual;

import java.util.List;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ManualViewHolder> {

    private List<Manual> manualList;
    private Context context;
    private final OnManualClickListener onManualClickListener;

    public ManualAdapter(List<Manual> manualList, OnManualClickListener onManualClickListener) {
        this.manualList = manualList;
        this.onManualClickListener = onManualClickListener;
    }

    @Override
    public ManualViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manual, parent, false);
        return new ManualViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManualViewHolder holder, int position) {
        Manual manual = manualList.get(position);
        holder.tituloTextView.setText(manual.getTitulo());
        holder.itemView.setOnClickListener(v -> onManualClickListener.onManualClick(manual));
    }

    @Override
    public int getItemCount() {
        return manualList.size();
    }

    // ViewHolder para el RecyclerView
    public static class ManualViewHolder extends RecyclerView.ViewHolder {

        TextView tituloTextView;

        public ManualViewHolder(View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.textViewTituloManual);
        }
    }

    public interface OnManualClickListener {
        void onManualClick(Manual manual);
    }
}


