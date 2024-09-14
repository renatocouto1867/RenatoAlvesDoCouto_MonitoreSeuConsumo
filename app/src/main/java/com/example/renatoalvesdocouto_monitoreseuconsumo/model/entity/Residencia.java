package com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "_residencia")
public class Residencia implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "residencia_id")
    public Long residenciaId;
    @ColumnInfo(name = "nome")
    public String nome;
    @ColumnInfo(name = "endereco")
    public String endereco;

    public Long getResidenciaId() {
        return residenciaId;
    }

    public void setResidenciaId(Long residenciaId) {
        this.residenciaId = residenciaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
