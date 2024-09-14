package com.example.renatoalvesdocouto_monitoreseuconsumo.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class Mensagens {
    public static void showSnackbarPersonalizado(View view, String message, int position, int backgroundColor,int tempo) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        int color;
        switch (backgroundColor) {
            case 1: // Azul
                color = ContextCompat.getColor(view.getContext(), android.R.color.holo_blue_dark);
                break;
            case 2: // Vermelho
                color = ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark);
                break;
            case 3: // Laranja
                color = ContextCompat.getColor(view.getContext(), android.R.color.holo_orange_dark);
                break;
            case 4: // Laranja
                color = ContextCompat.getColor(view.getContext(), android.R.color.holo_green_dark);
                break;
            default: // Padrão
                color = ContextCompat.getColor(view.getContext(), android.R.color.black);
                break;
        }
        snackbar.setBackgroundTint(color);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();

        int marginTop = (int) (100 * view.getResources().getDisplayMetrics().density); // Converte dp para pixels
        switch (position) {
            case 1:
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                break;
            case 2:
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
                break;
            case 3:
                params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
            default:
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                params.setMargins(0, marginTop, 0, 0);
                break;
        }
        snackbarView.setLayoutParams(params);
        snackbar.setDuration(tempo); // Duração de 1400ms
        snackbar.show();
    }//showSnackbar

}
