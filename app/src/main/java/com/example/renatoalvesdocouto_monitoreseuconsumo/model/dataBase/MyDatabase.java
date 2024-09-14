package com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoResidencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;

@Database(entities = {Residencia.class, ConsumoMensal.class}, version = 1)

public abstract class MyDatabase extends RoomDatabase {
    public abstract DaoResidencia daoResidencia();

    public abstract DaoConsumoMensal daoConsumoMensal();
}
