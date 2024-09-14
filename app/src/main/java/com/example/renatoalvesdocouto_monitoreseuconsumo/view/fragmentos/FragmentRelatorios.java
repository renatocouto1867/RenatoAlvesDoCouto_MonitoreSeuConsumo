package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */


public class FragmentRelatorios extends Fragment {

    private DaoConsumoMensal daoConsumoMensal;
    private Long idResidencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_relatorios, container, false);

        Button btnGrafico1 = view.findViewById(R.id.btnGrafico1);
        Button btnGrafico2 = view.findViewById(R.id.btnGrafico2);
        Button btnComparacao = view.findViewById(R.id.btnComparacao);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idResidencia = bundle.getLong("idResidencia");
        }

        btnGrafico1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graficoUltimosMeses(idResidencia, 6);
            }
        });

        btnGrafico2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graficoUltimosMeses(idResidencia, 12);
            }
        });

        btnComparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareConsumo(idResidencia);
            }
        });

        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(getContext(), MyDatabase.class, "meu_banco")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        daoConsumoMensal = db.daoConsumoMensal();
    }//onViewCreated

    private void compareConsumo(Long idResidencia) {
        FragmentComparacao fragmentComparacao = new FragmentComparacao();
        Bundle bundle = new Bundle();
        bundle.putLong("idResidencia", idResidencia);
        fragmentComparacao.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, fragmentComparacao).addToBackStack(null).commit();
    }

    private void graficoUltimosMeses(Long idResidencia, int qtMes) {
        FragmentGrafico fragmentGrafico = new FragmentGrafico();
        Bundle bundle = new Bundle();
        bundle.putInt("qtMes", qtMes);
        bundle.putLong("idResidencia", idResidencia);
        fragmentGrafico.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, fragmentGrafico).addToBackStack(null).commit();
    }

}//fragmentRelatorios