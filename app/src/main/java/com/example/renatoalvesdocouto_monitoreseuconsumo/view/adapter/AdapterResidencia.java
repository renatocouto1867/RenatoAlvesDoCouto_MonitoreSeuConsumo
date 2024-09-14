package com.example.renatoalvesdocouto_monitoreseuconsumo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;

import java.util.List;

public class AdapterResidencia extends RecyclerView.Adapter<AdapterResidencia.MyViewHolder> {
    private final OnItemClickListener onItemClickListener;
    private List<Residencia> residenciaList;

    public AdapterResidencia(List<Residencia> residenciaList, OnItemClickListener onItemClickListener) {
        this.residenciaList = residenciaList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setResidenciaList(List<Residencia> residenciaList) {
        this.residenciaList = residenciaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residencia, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Residencia residencia = residenciaList.get(position);
        holder.textViewResidencia.setText(residencia.getNome());
        holder.btnEditCasa.setOnClickListener(v -> onItemClickListener.onEditClick(residencia));
        holder.btnDeleteCasa.setOnClickListener(v -> onItemClickListener.onDeleteClick(residencia));
        holder.btn_add_consumo.setOnClickListener(v -> onItemClickListener.onAddConsumoClick(residencia));
        holder.btn_listar_consumo.setOnClickListener(v -> onItemClickListener.onListarConsumoClick(residencia));
        holder.btn_metrica.setOnClickListener(v -> onItemClickListener.onMetricaClick(residencia));
    }

    @Override
    public int getItemCount() {
        return residenciaList.size();
    }

    public interface OnItemClickListener {
        void onEditClick(Residencia residencia);

        void onDeleteClick(Residencia residencia);

        void onAddConsumoClick(Residencia residencia);

        void onListarConsumoClick(Residencia residencia);

        void onMetricaClick(Residencia residencia);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewResidencia;
        ImageButton btnEditCasa;
        ImageButton btnDeleteCasa;
        ImageButton btn_add_consumo;
        ImageButton btn_listar_consumo;
        ImageButton btn_metrica;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewResidencia = itemView.findViewById(R.id.tv_residencia);
            btnEditCasa = itemView.findViewById(R.id.btn_edit_casa);
            btnDeleteCasa = itemView.findViewById(R.id.btn_delete_casa);
            btn_add_consumo = itemView.findViewById(R.id.btn_add_consumo);
            btn_listar_consumo = itemView.findViewById(R.id.btn_listar_consumo);
            btn_metrica = itemView.findViewById(R.id.btn_metrica);
        }
    }
}
