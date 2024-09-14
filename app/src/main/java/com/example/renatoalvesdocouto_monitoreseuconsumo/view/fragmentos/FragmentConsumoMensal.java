package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.Mensagens.showSnackbarPersonalizado;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.calcularVariacaoConsumo;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.Residencia;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class FragmentConsumoMensal extends Fragment {
    ConsumoMensal consumoMensal;
    private DaoConsumoMensal daoConsumoMensal;
    private TextInputEditText mesEditText;
    private TextInputEditText anoEditText;
    private TextInputEditText qtM3EditText;
    private Calendar selectedDate;
    private Long idResidencia;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumo_mensal, container, false);


        Bundle bundle = getArguments();

        if (bundle != null) {
            Residencia residencia = (Residencia) bundle.getSerializable("residencia");
            if (residencia != null) {
                idResidencia = residencia.getResidenciaId();
            }
        }

        mesEditText = view.findViewById(R.id.editTextMes);
        anoEditText = view.findViewById(R.id.editTextAno);
        qtM3EditText = view.findViewById(R.id.editTextQtM3);
        Button salvarButton = view.findViewById(R.id.buttonCadastrar);
        progressBar = view.findViewById(R.id.progressBarSalvar);

        selectedDate = Calendar.getInstance();

        mesEditText.setOnClickListener(v -> seletorDeData());
        anoEditText.setOnClickListener(v -> seletorDeData());
        salvarButton.setOnClickListener(v -> salvarConsumo(consumoMensal, view));

        if (bundle != null) {
            consumoMensal = (ConsumoMensal) bundle.getSerializable("consumoMensal");
            if (consumoMensal != null) {
                selectedDate.set(consumoMensal.getAno(), consumoMensal.getMes() - 1, 1);
                qtM3EditText.setText(String.valueOf(consumoMensal.getQtM3()));
                setCampos();
            }
        } else consumoMensal = null;

        return view;
    }//oncreateview

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(Objects.requireNonNull(requireContext()), MyDatabase.class, "meu_banco")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        daoConsumoMensal = db.daoConsumoMensal();
    }

    private void seletorDeData() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.selecione_mes_ano).setSelection(selectedDate.getTimeInMillis())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate.setTimeInMillis(selection);
            selectedDate.set(Calendar.DAY_OF_MONTH, 1);
            setCampos();
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }//seletorDeData

    private void setCampos() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("pt", "BR"));
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        mesEditText.setText(monthFormat.format(selectedDate.getTime()));
        anoEditText.setText(yearFormat.format(selectedDate.getTime()));
    }//


    private void salvarConsumo(ConsumoMensal consumoMensal, View viewConsumo) {
        int mes = selectedDate.get(Calendar.MONTH) + 1;
        int ano = selectedDate.get(Calendar.YEAR);
        String qtM3Text = Objects.requireNonNull(qtM3EditText.getText()).toString();

        if (qtM3Text.isEmpty()) {
            showSnackbarPersonalizado(viewConsumo, getString(R
                    .string.campo_m3_vazio), 2, 1, 1600);
            return;
        }

        int qtM3 = Integer.parseInt(qtM3Text);

        if (consumoMensal != null) {
            // Edição de um registro existente
            consumoMensal.setMes(mes);
            consumoMensal.setAno(ano);
            consumoMensal.setQtM3(qtM3);

            try {
                daoConsumoMensal.updateConsumoMensal(consumoMensal);
                comparaNovoConsumo(consumoMensal, viewConsumo);
                progressBar.setVisibility(ProgressBar.VISIBLE);

                new Handler().postDelayed(() -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    showSnackbarPersonalizado(viewConsumo, getString(R
                            .string.registro_salvo), 2, 1, 1600);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }, 2500);

            } catch (Exception e) {
                showSnackbarPersonalizado(viewConsumo, getString(R
                        .string.erro_Exception_consumo) + e.getMessage(), 2, 2, 1600);
            }
        } else {
            // Inserção de um novo registro
            ConsumoMensal consumoExistente = daoConsumoMensal.getConsumoPorMesAno(mes, ano, idResidencia);

            if (consumoExistente != null) {
                showSnackbarPersonalizado(viewConsumo, getString(R
                        .string.erro_consumo_duplicado), 2, 3, 1600);
                return;
            }
            consumoMensal = new ConsumoMensal();
            consumoMensal.setMes(mes);
            consumoMensal.setAno(ano);
            consumoMensal.setQtM3(qtM3);
            consumoMensal.setResidenciaId(idResidencia);

            try {
                daoConsumoMensal.insertConsumoMensal(consumoMensal);
                comparaNovoConsumo(consumoMensal, viewConsumo);
                progressBar.setVisibility(ProgressBar.VISIBLE);

                new Handler().postDelayed(() -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    showSnackbarPersonalizado(viewConsumo, getString(R
                            .string.registro_salvo), 2, 1, 1600);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }, 2500);

            } catch (Exception e) {
                showSnackbarPersonalizado(viewConsumo, getString(R
                        .string.erro_Exception_consumo) + e.getMessage(), 2, 2, 1600);
            }
        }
    }//salvar


    public void comparaNovoConsumo(ConsumoMensal consumoMensal, View viewNovoConsumo) {
        int mes = consumoMensal.getMes();
        int ano = consumoMensal.getAno();
        long idRes = consumoMensal.getResidenciaId();

        ConsumoMensal consumoMensalAnterior;
        if (isJaneiro(mes)) {
            consumoMensalAnterior = daoConsumoMensal.getConsumoPorMesAno(12, ano - 1, idRes);
        } else {
            consumoMensalAnterior = daoConsumoMensal.getConsumoPorMesAno(mes - 1, ano, idRes);
        }

        if (consumoMensalAnterior != null) {
            int qtM3_1 = consumoMensalAnterior.getQtM3();
            int qtM3_2 = consumoMensal.getQtM3();

            if (qtM3_1 > 0 && qtM3_2 > 0) {
                double resultado = calcularVariacaoConsumo(qtM3_1, qtM3_2);
                exibirResultados(resultado, viewNovoConsumo);
            } else {
                showSnackbarPersonalizado(viewNovoConsumo, getString(R
                        .string.erro_verifique_os_dados), 2, 2, 1600);
            }
        }
    }

    public boolean isJaneiro(int mes) {
        return mes == 1;
    }

    private void exibirResultados(double resultado, View viewMensagem) {
        String mensagem;
        int cor = 0;

        if (resultado >= 5) {
            mensagem = getString(R.string.aumento_consumo);
            cor = 2;
        } else if (resultado > -5) {
            mensagem = getString(R.string.consumo_estavel);
            cor = 3;
        } else if (resultado <= -5) {
            mensagem = getString(R.string.reducao_consumo);
            cor = 4;
        } else {
            mensagem = getString(R.string.erro_verifique_os_dados);
        }
        showSnackbarPersonalizado(viewMensagem, mensagem, 4, cor, 3000);
    }

}
