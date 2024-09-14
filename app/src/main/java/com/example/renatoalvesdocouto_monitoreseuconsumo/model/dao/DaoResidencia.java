package com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;

import java.util.List;

@Dao
public interface DaoResidencia {
    @Insert
    public Long insertResidencia(Residencia residencia);

    @Query("SELECT * FROM _residencia")
    public List<Residencia> getAllResidencia();

    @Query("SELECT * FROM _residencia WHERE residencia_id = :residencia_id ")
    public Residencia getResidenciaById(Long residencia_id);

    @Update
    void updateResidencia(Residencia residencia);

    @Delete
    void deleteResidencia(Residencia residencia);

}
