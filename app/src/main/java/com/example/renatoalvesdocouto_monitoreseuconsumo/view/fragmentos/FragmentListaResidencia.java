package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.Mensagens.showSnackbarPersonalizado;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.exibirBotaoFloatActionButton;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.ocultarBotaoFloatActionButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoResidencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.view.adapter.AdapterResidencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentListaResidencia extends Fragment {

    private DaoResidencia daoResidencia;
    private AdapterResidencia adapterResidencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_residencia, container, false);
        exibirBotaoFloatActionButton(view);

        RecyclerView residenciaRecyclerView = view.findViewById(R.id.recyclerViewListaResidencia);
        residenciaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterResidencia = new AdapterResidencia(new ArrayList<>(), new AdapterResidencia.OnItemClickListener() {
            @Override
            public void onEditClick(Residencia residencia) {
                editResidencia(residencia);
            }

            @Override
            public void onDeleteClick(Residencia residencia) {
                deleteResidencia(residencia);
            }

            @Override
            public void onAddConsumoClick(Residencia residencia) {
                addConsumoResidencia(residencia);
            }

            @Override
            public void onListarConsumoClick(Residencia residencia) {
                listarConsumo(residencia);

            }

            @Override
            public void onMetricaClick(Residencia residencia) {
                relatorios(residencia);
            }
        });

        residenciaRecyclerView.setAdapter(adapterResidencia);


        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(requireContext(), MyDatabase.class, "meu_banco").allowMainThreadQueries().build();
        daoResidencia = db.daoResidencia();
        loadsResidencia(view);
    }//onViewCreated

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ocultarBotaoFloatActionButton(requireView());
    }

    private void listarConsumo(Residencia residencia) {
        FragmentListaConsumo fragmentListaConsumo = new FragmentListaConsumo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("residencia", residencia);
        fragmentListaConsumo.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, fragmentListaConsumo).addToBackStack(null).commit();
    }

    private void addConsumoResidencia(Residencia residencia) {
        FragmentConsumoMensal fragmentConsumoMensal = new FragmentConsumoMensal();
        Bundle bundle = new Bundle();
        bundle.putSerializable("residencia", residencia);
        fragmentConsumoMensal.setArguments(bundle);
        Objects.requireNonNull(requireActivity()).getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, fragmentConsumoMensal).addToBackStack(null).commit();
    }

    private void loadsResidencia(View view) {
        List<Residencia> residenciasList = daoResidencia.getAllResidencia();
        if (residenciasList != null && !residenciasList.isEmpty()) {
            adapterResidencia.setResidenciaList(residenciasList);
        } else {
            showSnackbarPersonalizado(view, getString(R.string.nenhuma_residencia_cadastrada), 2, 3, 1600);

        }
    }

    private void editResidencia(Residencia residencia) {
        FragmentResidencia fragmentResidencia = new FragmentResidencia();
        Bundle bundle = new Bundle();
        bundle.putSerializable("residencia", residencia);
        fragmentResidencia.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentResidencia).addToBackStack(null).commit();
    }

    private void relatorios(Residencia residencia) {
        FragmentRelatorios fragmentRelatorios = new FragmentRelatorios();
        Bundle bundle = new Bundle();
        bundle.putLong("idResidencia", residencia.getResidenciaId());
        fragmentRelatorios.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentRelatorios).addToBackStack(null).commit();
    }

    private void deleteResidencia(final Residencia residencia) {
        new AlertDialog.Builder(getContext()).setTitle(R.string.confirmar_exclusao)
                .setMessage(getString(R.string.realmente_deseja_deletar) + residencia.getNome() + "?")
                .setPositiveButton(getString(R.string.deletar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //sim
                        daoResidencia.deleteResidencia(residencia);
                        loadsResidencia(getView());
                    }
                }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //não
                        dialog.dismiss();
                    }
                }).create().show();
    }

}