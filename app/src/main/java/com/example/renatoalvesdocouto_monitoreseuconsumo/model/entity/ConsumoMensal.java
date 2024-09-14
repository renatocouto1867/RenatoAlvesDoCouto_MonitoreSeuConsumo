package com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "_consumo_mensal",
        foreignKeys = @ForeignKey(
                entity = Residencia.class,
                parentColumns = {"residencia_id"},
                childColumns = {"residencia_id"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = {@Index(value = {"mes", "ano", "residencia_id"}, unique = true)}
)
public class ConsumoMensal implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "consumo_id")
    public Long consumoId;
    @ColumnInfo(name = "mes")
    public int mes;
    @ColumnInfo(name = "ano")
    public int ano;
    @ColumnInfo(name = "qt_m3")
    public int qtM3;
    @ColumnInfo(name = "residencia_id")
    public Long residenciaId;

    public Long getConsumoId() {
        return consumoId;
    }

    public void setConsumoId(Long consumoId) {
        this.consumoId = consumoId;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Long getResidenciaId() {
        return residenciaId;
    }

    public void setResidenciaId(Long residenciaId) {
        this.residenciaId = residenciaId;
    }

    public int getQtM3() {
        return qtM3;
    }

    public void setQtM3(int qtM3) {
        this.qtM3 = qtM3;
    }
}
