package com.example.renatoalvesdocouto_monitoreseuconsumo.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.renatoalvesdocouto_monitoreseuconsumo.R;
import com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos.FragmentInformacao;
import com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos.FragmentListaResidencia;
import com.example.renatoalvesdocouto_monitoreseuconsumo.view.fragmentos.FragmentResidencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

//2.Para a solução final seu app você deve integrar os seguintes componentes:
//ActionBar e Menu; = OK
//AlertDialog; ok
//ProgressBar; OK
//Botão flutuante; ok
//Fragments e Activities; OK
//Persistência de dados: SQLite ou Room = OK
//RecycleView e CardView. = OK

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });//ViewCompat

        setupToolbar();

        findViewById(R.id.imgLogo).setOnClickListener(view ->
                loadFragment(new FragmentListaResidencia()));// volta para tela inicial, efeito home

        FloatingActionButton fab = findViewById(R.id.fab);//botão flutuante
        fab.setOnClickListener(view -> loadFragment(new FragmentInformacao()));
        loadFragment(new FragmentListaResidencia());
    }//onCreate

    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowCustomEnabled(true);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }//loadFragment


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_nova_Residencia) {
            loadFragment(new FragmentResidencia());
            return true;
        } else if (id == R.id.menu_List_residencia) {
            loadFragment(new FragmentListaResidencia());
            return true;
        } else if (id == R.id.menu_sair) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

}