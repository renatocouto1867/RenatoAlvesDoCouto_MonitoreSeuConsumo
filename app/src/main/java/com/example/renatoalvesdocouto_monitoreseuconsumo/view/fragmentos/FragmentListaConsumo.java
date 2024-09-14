package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.Mensagens.showSnackbarPersonalizado;

import android.app.AlertDialog;
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
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.view.adapter.AdapterConsumo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentListaConsumo extends Fragment {


    private DaoConsumoMensal daoConsumoMensal;
    private AdapterConsumo adapterConsumo;
    private Long idResidencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_consumo, container, false);

        inicializaRecyclerView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Residencia residencia = (Residencia) bundle.getSerializable("residencia");
            if (residencia != null) {
                idResidencia = residencia.getResidenciaId();
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(Objects.requireNonNull(requireContext()), MyDatabase.class, "meu_banco")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        daoConsumoMensal = db.daoConsumoMensal();

        loadConsumo(view);
    }//onViewCreated


    private void inicializaRecyclerView(View view) {
        RecyclerView consumoRecyclerView = view.findViewById(R.id.recyclerViewListaConsumo);
        consumoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterConsumo = new AdapterConsumo(new ArrayList<>(), new AdapterConsumo.OnItemClickListener() {
            @Override
            public void onEditClick(ConsumoMensal consumoMensal) {
                editConsumo(consumoMensal);
            }

            @Override
            public void onDeleteClick(ConsumoMensal consumoMensal) {
                deleteConsumo(consumoMensal, view);
            }
        });
        consumoRecyclerView.setAdapter(adapterConsumo);
    }

    private void loadConsumo(View view) {
        List<ConsumoMensal> consumoList = daoConsumoMensal.getConsumoMensalByResidenciaId(idResidencia);

        if (consumoList != null && !consumoList.isEmpty()) {
            adapterConsumo.setConsumoList(consumoList);
        } else {
            showSnackbarPersonalizado(view, getString(R.string.nenhum_consumo_cadastrada), 2, 3, 1600);
        }
    }

    private void deleteConsumo(ConsumoMensal consumoMensal, View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.confirmar_exclusao)
                .setMessage(getString(R.string.realmente_deseja_deletar) + "?")
                .setPositiveButton(R.string.deletar, (dialog, which) -> {
                    daoConsumoMensal.deleteConsumoMensal(consumoMensal);
                    loadConsumo(view); // Recarrega a lista após a exclusão
                })
                .setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss())
                .create()
                .show();

    }

    private void editConsumo(ConsumoMensal consumoMensal) {
        FragmentConsumoMensal fragmentConsumoMensal = new FragmentConsumoMensal();
        Bundle bundle = new Bundle();
        bundle.putSerializable("consumoMensal", consumoMensal);
        fragmentConsumoMensal.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentConsumoMensal)
                .addToBackStack(null)
                .commit();
    }
}
