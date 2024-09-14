package com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos;

import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.Mensagens.showSnackbarPersonalizado;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.calcularVariacaoConsumo;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.ocultarBotaoFloatActionButton;
import static com.example.renatoalvesdocouto_monitoreseuconsumo.utils.UtilidadesCalculos.validaDatas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dao.DaoConsumoMensal;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.dataBase.MyDatabase;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class FragmentComparacao extends Fragment {
    private final Calendar data1 = Calendar.getInstance();
    private final Calendar data2 = Calendar.getInstance();
    private final Calendar selectedDate = Calendar.getInstance();  // Inicializa o Calendar para evitar NullPointerException
    private DaoConsumoMensal daoConsumoMensal;
    private Integer mes1, ano1, mes2, ano2, qtM3_1, qtM3_2;
    private Long idResidencia;
    private TextInputEditText mesEditText1, mesEditText2;
    private TextInputEditText anoEditText1, anoEditText2;
    private TextView tViewAnalise, tViewCons1, tViewCons2, tViewRel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comparacao, container, false);

        ocultarBotaoFloatActionButton(view);

        mesEditText1 = view.findViewById(R.id.editTextMes1);
        mesEditText2 = view.findViewById(R.id.editTextMes2);
        anoEditText1 = view.findViewById(R.id.editTextAno1);
        anoEditText2 = view.findViewById(R.id.editTextAno2);
        tViewAnalise = view.findViewById(R.id.tViewAnalise);
        tViewCons1 = view.findViewById(R.id.tViewCons1);
        tViewCons2 = view.findViewById(R.id.tViewCons2);
        tViewRel = view.findViewById(R.id.tViewRel);
        Button btnComparar = view.findViewById(R.id.btnComparar);

        mesEditText1.setOnClickListener(v -> seletorDeData(1));
        mesEditText2.setOnClickListener(v -> seletorDeData(2));
        anoEditText1.setOnClickListener(v -> seletorDeData(1));
        anoEditText2.setOnClickListener(v -> seletorDeData(2));
        btnComparar.setOnClickListener(v -> comparar());

        Bundle bundle = getArguments();
        if (bundle != null) {
            idResidencia = bundle.getLong("idResidencia");
        }

        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyDatabase db = Room.databaseBuilder(Objects.requireNonNull(requireContext()), MyDatabase.class, "meu_banco").allowMainThreadQueries().build();
        daoConsumoMensal = db.daoConsumoMensal();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ocultarBotaoFloatActionButton(requireView());
    }

    private void seletorDeData(int opcao) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(R.string.selecione_mes_ano).setSelection(selectedDate.getTimeInMillis()).setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR).build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate.setTimeInMillis(selection);
            selectedDate.set(Calendar.DAY_OF_MONTH, 1);
            setCampos(opcao);
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void setCampos(int opcao) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("pt", "BR"));
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String mes = monthFormat.format(selectedDate.getTime());
        String ano = yearFormat.format(selectedDate.getTime());

        if (opcao == 1) {
            mesEditText1.setText(mes);
            anoEditText1.setText(ano);
            mes1 = selectedDate.get(Calendar.MONTH) + 1;
            ano1 = selectedDate.get(Calendar.YEAR);
            data1.set(ano1, mes1 - 1, 1);

        } else {
            mesEditText2.setText(mes);
            anoEditText2.setText(ano);
            mes2 = selectedDate.get(Calendar.MONTH) + 1;
            ano2 = selectedDate.get(Calendar.YEAR);
            data2.set(ano2, mes2 - 1, 1);

            if (!validaDatas(data1, data2)) {
                showSnackbarPersonalizado(getView(), getString(R.string.data_final_deve_ser_maior_que_a_data_inicial), 2, 2, 1600);
            }
        }
    }


    public void comparar() {
        if (!validaDatas(data1, data2)) {
            showSnackbarPersonalizado(getView(), getString(R.string.data_final_deve_ser_maior_que_a_data_inicial), 2, 2, 1600);
            return;
        }

        ConsumoMensal consumoMensal1 = verificaExistenciaConsumoMes(mes1, ano1, idResidencia);
        ConsumoMensal consumoMensal2 = verificaExistenciaConsumoMes(mes2, ano2, idResidencia);

        if (consumoMensal1 == null || consumoMensal2 == null) {
            return; //a mensagem de erro já foi tratada no metodo verificaExistenciaConsumoMes
        }

        qtM3_1 = consumoMensal1.getQtM3();
        qtM3_2 = consumoMensal2.getQtM3();

        if (qtM3_1 > 0 && qtM3_2 > 0) {
            double resultado = calcularVariacaoConsumo(qtM3_1, qtM3_2);
            exibirResultados(resultado);
        } else {
            showSnackbarPersonalizado(getView(), getString(R.string.erro_verifique_os_dados), 2, 2, 160);
        }
    }

    private ConsumoMensal verificaExistenciaConsumoMes(int mes, int ano, Long idResidencia) {
        ConsumoMensal consumoMensal = daoConsumoMensal.getConsumoPorMesAno(mes, ano, idResidencia);
        if (consumoMensal == null) {
            showSnackbarPersonalizado(getView(), getString(R.string.Erro_mes_inexistente), 2, 2, 1600);
        }
        return consumoMensal;
    }


    private void exibirResultados(double resultado) {
        StringBuilder stringBuilder = new StringBuilder();

        tViewAnalise.setText(getString(R.string.analise));
        tViewCons1.setText(getString(R.string.consumo_do_primeiro_periodo) + qtM3_1 + "m³");
        tViewCons2.setText(getString(R.string.consumo_do_segundo_periodo) + qtM3_2 + "m³");

        double moduloResultado = Math.abs(resultado);

        String resultadoFormatado = String.format("%.2f", moduloResultado);

        if (resultado >= 5) {
            stringBuilder.append(getString(R.string.texto_aumento_1) + " ").append(resultadoFormatado).append(getString(R.string.texto_aumnto_2));
            atualizarTextView(tViewRel, stringBuilder.toString(), R.color.white, R.color.warningColor);
        } else if (resultado > -5) {
            String resultadoFormatadoNormal = String.format("%.2f", resultado);
            stringBuilder.append(getString(R.string.consumo_normal_1) + " ").append(resultadoFormatadoNormal).append(getString(R.string.consumo_normal_2));
            atualizarTextView(tViewRel, stringBuilder.toString(), R.color.primaryDarkColor, R.color.gray_50);

        } else if (resultado <= -5) {
            stringBuilder.append(getString(R.string.consumo_menor_1) + " ").append(resultadoFormatado).append(getString(R.string.consumo_menor_2));
            atualizarTextView(tViewRel, stringBuilder.toString(), R.color.primaryDarkColor, R.color.gray_50);
        } else {
            showSnackbarPersonalizado(getView(), getString(R.string.erro_verifique_os_dados), 2, 2, 1600);
        }
    }// exibirResultados

    private void atualizarTextView(TextView textView, String texto, int corTexto, int corFundo) {
        textView.setText(texto);
        textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(requireContext()), corTexto));
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), corFundo));
    }


}