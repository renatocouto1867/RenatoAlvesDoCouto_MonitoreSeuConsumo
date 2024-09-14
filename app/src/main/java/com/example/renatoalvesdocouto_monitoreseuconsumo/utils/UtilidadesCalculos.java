package com.example.renatoalvesdocouto_monitoreseuconsumo.utils;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.model.entity.ConsumoMensal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilidadesCalculos {


    public static double calcularVariacaoConsumo(int qtM3_1, int qtM3_2) {
        return ((double) qtM3_2 / qtM3_1 - 1) * 100;
    }


    public static boolean validaDatas(Calendar dataInicial, Calendar dataFinal) {
        return dataFinal.after(dataInicial);
    }//validaDatas

    public static void ordenarPorAnoMesDecrescente(List<ConsumoMensal> consumoList) {
        Collections.sort(consumoList, new Comparator<ConsumoMensal>() {
            @Override
            public int compare(ConsumoMensal c1, ConsumoMensal c2) {
                int anoComparacao = Integer.compare(c2.getAno(), c1.getAno());
                if (anoComparacao != 0) {
                    return anoComparacao;
                }
                return Integer.compare(c2.getMes(), c1.getMes());
            }
        });
    }//ordenarPorAnoMesDecrescente

    public static void ordenarPorAnoMesCrescente(List<ConsumoMensal> consumoList) {
        Collections.sort(consumoList, new Comparator<ConsumoMensal>() {
            @Override
            public int compare(ConsumoMensal c1, ConsumoMensal c2) {
                int anoComparacao = Integer.compare(c1.getAno(), c2.getAno());
                if (anoComparacao != 0) {
                    return anoComparacao;
                }
                return Integer.compare(c1.getMes(), c2.getMes());
            }
        });
    }//ordenarPorAnoMesCrescente


    public static void ocultarBotaoFloatActionButton(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        if (activity != null) {
            View fab = activity.findViewById(R.id.fab);
            if (fab != null) {
                fab.setVisibility(View.GONE);
            }
        }
    } //ocultarBotaoFloatActionButton

    public static void exibirBotaoFloatActionButton(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        if (activity != null) {
            View fab = activity.findViewById(R.id.fab);
            if (fab != null) {
                fab.setVisibility(View.VISIBLE);
            }
        }
    }


    public static void criarGraficoConsumoAgua(LineChart chart, List<ConsumoMensal> dados) {
        // Ordena os dados por ano e mês
        ordenarPorAnoMesCrescente(dados);

        // Cria listas para armazenar os valores e rótulos do gráfico
        List<Entry> valores = new ArrayList<>();
        List<String> rotulos = new ArrayList<>();

        // Preenche as listas com os dados de consumo
        for (int i = 0; i < dados.size(); i++) {
            ConsumoMensal consumo = dados.get(i);
            valores.add(new Entry(i, consumo.qtM3));
            rotulos.add(String.format("%02d/%d", consumo.mes, consumo.ano));
        }

        // Configura o dataset com os valores e o rótulo do gráfico
        LineDataSet dataSet = new LineDataSet(valores, "Consumo de Água (m³)");
        dataSet.setColor(android.graphics.Color.BLUE); // Cor da linha
        dataSet.setValueTextColor(android.graphics.Color.BLACK); // Cor dos valores
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Linha suavizada
        dataSet.setCubicIntensity(0.2f); // Intensidade da suavização
        dataSet.setLineWidth(2f); // Espessura da linha
        dataSet.setCircleRadius(4f); // Tamanho dos círculos nos pontos
        dataSet.setCircleColor(android.graphics.Color.BLUE); // Cor dos círculos
        dataSet.setDrawCircleHole(true); // Buraco nos círculos
        dataSet.setCircleHoleRadius(2f); // Tamanho do buraco

        // Configura o gráfico
        chart.setExtraOffsets(20, 10, 20, 10); // Margens ao redor do gráfico
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        // Configura o eixo X para mostrar todos os rótulos de meses
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(rotulos.size(), true); // Exibir rótulos de todos os meses
        xAxis.setGranularity(1f); // Define a granularidade para 1 unidade
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return (index >= 0 && index < rotulos.size()) ? rotulos.get(index) : "";
            }
        });
        xAxis.setDrawGridLines(false); // Remove as linhas de grade verticais

        // Configura o eixo Y
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // Remove as linhas de grade horizontais
        chart.getAxisRight().setEnabled(false); // Desativa o eixo Y direito

        // Outras configurações do gráfico
        chart.getDescription().setEnabled(false); // Desativa a descrição
        chart.setDrawBorders(false); // Remove as bordas
        chart.setTouchEnabled(true); // Habilita toque
        chart.setDragEnabled(true); // Habilita arrastar
        chart.setScaleEnabled(true); // Habilita zoom
        chart.setPinchZoom(true); // Habilita zoom por pinça
        chart.setDrawGridBackground(false); // Remove fundo da grade

        // Personaliza a legenda
        Legend legend = chart.getLegend();
        legend.setEnabled(true); // Habilita legenda
        legend.setTextColor(android.graphics.Color.BLACK); // Cor do texto da legenda
        legend.setTextSize(14f); // Tamanho do texto da legenda

        // Animação
        chart.animateY(1000); // Animação vertical

        // Atualiza o gráfico
        chart.invalidate();
    }


    public static void criarGraficoConsumoAguaReto(LineChart chart, List<ConsumoMensal> dados) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dados.size(); i++) {
            ConsumoMensal consumo = dados.get(i);
            entries.add(new Entry(i, consumo.qtM3));
            labels.add(String.format("%02d/%d", consumo.mes, consumo.ano));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Consumo de Água (m³)");
        dataSet.setColor(android.graphics.Color.BLUE);
        dataSet.setValueTextColor(android.graphics.Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index);
                }
                return "";
            }
        });

        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }


}//utilidades
