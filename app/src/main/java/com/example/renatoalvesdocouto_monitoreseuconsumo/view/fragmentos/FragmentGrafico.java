package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;


import android.content.pm.ActivityInfo;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentGrafico extends Fragment {

    private DaoConsumoMensal daoConsumoMensal;
    private Long idResidencia;
    private LineChart lineChart;
    private int mes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grafico, container, false);
        lineChart = view.findViewById(R.id.lineChart);

        // Ocultar a ActionBar
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            // Definir a orientação para landscape
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        Bundle bundle = getArguments();
        if (bundle != null) {
            idResidencia = bundle.getLong("idResidencia");
            mes = bundle.getInt("qtMes");
        }


        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(Objects.requireNonNull(requireContext()), MyDatabase.class, "meu_banco")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        daoConsumoMensal = db.daoConsumoMensal();

        List<ConsumoMensal> consumoList = geraListaConsumo(idResidencia, mes);

        UtilidadesCalculos.criarGraficoConsumoAgua(lineChart, consumoList);

    }//onViewcreated

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Restaurar as configurações
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private ArrayList<ConsumoMensal> geraListaConsumo(Long idResidencia, int meses) {
        Calendar calendar = Calendar.getInstance();

        List<Calendar> ultimosMeses = new ArrayList<>();
        for (int i = 0; i < meses; i++) {
            ultimosMeses.add((Calendar) calendar.clone());
            calendar.add(Calendar.MONTH, -1);
        }
        ArrayList<ConsumoMensal> consumoList = new ArrayList<>();

        for (Calendar mes : ultimosMeses) {
            int ano = mes.get(Calendar.YEAR);
            int mesInt = mes.get(Calendar.MONTH) + 1; // Calendar.MONTH (mês) começa em 0
            ConsumoMensal consumo = daoConsumoMensal.getConsumoPorMesAno(mesInt, ano, idResidencia);
            if (consumo != null) {
                consumoList.add(consumo);
            }
        }
        return consumoList;
    }

}