package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.Mensagens.showSnackbarPersonalizado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoResidencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentResidencia extends Fragment {
    EditText editTextNomeResidencia;
    EditText editTextEndereco;
    Button button;
    DaoResidencia daoResidencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residencia, container, false);

        editTextNomeResidencia = view.findViewById(R.id.editTextNomeResidencia);
        editTextEndereco = view.findViewById(R.id.editTextEndereco);
        button = view.findViewById(R.id.buttonCadastrar);

        Residencia residencia;

        Bundle bundle = getArguments();
        if (bundle != null) {
            residencia = (Residencia) bundle.getSerializable("residencia");
            if (residencia != null) {
                editTextNomeResidencia.setText(residencia.getNome());
                editTextEndereco.setText(residencia.getEndereco());
            }
        } else residencia = null;

        button.setOnClickListener(v -> salvar(residencia, view));
        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyDatabase db;
        db = Room.databaseBuilder(Objects.requireNonNull(requireContext()), MyDatabase.class, "meu_banco").allowMainThreadQueries().build();
        daoResidencia = db.daoResidencia();
    }//onViewCreated

    private void salvar(Residencia residencia, View view) {
        if (residencia != null) {
            residencia.setNome(editTextNomeResidencia.getText().toString());
            residencia.setEndereco(editTextEndereco.getText().toString());
            daoResidencia.updateResidencia(residencia);

            showSnackbarPersonalizado(view, getString(R.string.registro_salvo), 2, 1, 1600);

            editTextNomeResidencia.setText("");
            editTextEndereco.setText("");
            requireActivity().getSupportFragmentManager().popBackStack();

        } else {

            residencia = new Residencia();
            residencia.setNome(editTextNomeResidencia.getText().toString());
            residencia.setEndereco(editTextEndereco.getText().toString());
            Long retorno = daoResidencia.insertResidencia(residencia);

            if (retorno != -1) {
                showSnackbarPersonalizado(view, getString(R.string.registro_salvo), 2, 1, 1600);
                editTextNomeResidencia.setText("");
                editTextEndereco.setText("");
                requireActivity().getSupportFragmentManager().popBackStack();

            } else {
                showSnackbarPersonalizado(view, getString(R.string.operao_nao_realizada), 2, 1, 1600);
            }

        }
    }//salvar


}