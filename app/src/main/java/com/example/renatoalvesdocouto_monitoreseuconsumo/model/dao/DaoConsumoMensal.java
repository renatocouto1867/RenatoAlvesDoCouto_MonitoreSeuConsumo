package com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;

import java.util.List;

@Dao
public interface DaoConsumoMensal {
    @Insert
    Long insertConsumoMensal(ConsumoMensal consumoMensal);

    @Delete
    void deleteConsumoMensal(ConsumoMensal consumoMensal);


    @Query("SELECT * FROM _consumo_mensal")
    List<ConsumoMensal> getAllConsumo();


    @Query("SELECT * FROM _consumo_mensal WHERE consumo_id = :consumo_id ")
    ConsumoMensal getConsumoMensalById(Long consumo_id);

    @Query("SELECT * FROM _consumo_mensal WHERE residencia_id = :residencia_id ")
    List<ConsumoMensal> getConsumoMensalByResidenciaId(Long residencia_id);

    @Query("SELECT * FROM _consumo_mensal WHERE mes = :mes AND ano = :ano AND residencia_id = :residenciaId LIMIT 1")
    ConsumoMensal getConsumoPorMesAno(int mes, int ano, Long residenciaId);

    @Update
    void updateConsumoMensal(ConsumoMensal consumoMensal);

}
