package com.example.renatoalvesdocouto_monitoreseuconsumo.view.adapter;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.ordenarPorAnoMesDecrescente;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;

import java.util.List;

public class AdapterConsumo extends RecyclerView.Adapter<AdapterConsumo.MyViewHolder> {
    private final OnItemClickListener onItemClickListener;
    private List<ConsumoMensal> consumoList;

    public AdapterConsumo(List<ConsumoMensal> consumoList, OnItemClickListener onItemClickListener) {
        this.consumoList = consumoList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setConsumoList(List<ConsumoMensal> consumoList) {
        this.consumoList = consumoList;
        ordenarPorAnoMesDecrescente(consumoList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumo, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ConsumoMensal consumoMensal = consumoList.get(position);
        Log.d("AdapterConsumo", "BindViewHolder position: " + position + ", Mes: " + consumoMensal.getMes());

        holder.textViewMes.setText(String.valueOf(consumoMensal.getMes()));
        holder.textViewAno.setText(String.valueOf(consumoMensal.getAno()));
        holder.textViewConsumo.setText(String.valueOf(consumoMensal.getQtM3()));

        holder.btnEditConsumo.setOnClickListener(v -> onItemClickListener.onEditClick(consumoMensal));
        holder.btnDeleteConsumo.setOnClickListener(v -> onItemClickListener.onDeleteClick(consumoMensal));
    }


    @Override
    public int getItemCount() {
        return consumoList.size();
    }

    public interface OnItemClickListener {
        void onEditClick(ConsumoMensal consumoMensal);

        void onDeleteClick(ConsumoMensal consumoMensal);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMes;
        TextView textViewAno;
        TextView textViewConsumo;
        ImageButton btnEditConsumo;
        ImageButton btnDeleteConsumo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMes = itemView.findViewById(R.id.tv_consumo_mes);
            textViewAno = itemView.findViewById(R.id.tv_consumo_ano);
            textViewConsumo = itemView.findViewById(R.id.tv_consumo_qtM3);
            btnEditConsumo = itemView.findViewById(R.id.btn_edit_consumo_list);
            btnDeleteConsumo = itemView.findViewById(R.id.btn_delete_consumo_list);
        }
    }

}
