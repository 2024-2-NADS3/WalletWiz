package com.walletwiz;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class receita extends AppCompatActivity {
    private session sessionInstance;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private boolean isFabOpen = false;
    private List<transacao> transacoes = new ArrayList<>();


    private EditText editTextDescricao, editTextValor, editTextData;
    private Button buttonSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);
        transacoes = (ArrayList<transacao>) getIntent().getSerializableExtra("transacoes");

        if (transacoes == null) {
            transacoes = new ArrayList<>();
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ImageView icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            if (item.getItemId() == R.id.nav_home) {
                Intent intent = new Intent(receita.this, session.class);
                startActivity(intent);
                finish();

                return true;
            }


            return false;
        });


        fabAdd = findViewById(R.id.fab_add);
        fabDespesa = findViewById(R.id.fab_expense);
        fabReceita = findViewById(R.id.fab_income);


        fabDespesa.setVisibility(View.GONE);
        fabReceita.setVisibility(View.GONE);


        fabAdd.setOnClickListener(v -> {
            if (isFabOpen) {
                closeFABMenu();
            } else {
                openFABMenu();
            }
        });


        editTextDescricao = findViewById(R.id.editTextObs);
        editTextValor = findViewById(R.id.editTextReceita);
        editTextData = findViewById(R.id.editDateReceita);
        buttonSalvar = findViewById(R.id.buttonReceita);


        buttonSalvar.setOnClickListener(v -> {
            adicionarReceita();
        });
    }

    private void adicionarReceita() {

        String descricao = editTextDescricao.getText().toString();
        String valorString = editTextValor.getText().toString();
        String data = editTextData.getText().toString();

        if (!descricao.isEmpty() && !valorString.isEmpty() && !data.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorString);
                transacao novaTransacao = new transacao("receita", valor, data);


                transacoes.add(novaTransacao);


                Intent resultIntent = new Intent();
                resultIntent.putExtra("nova_transacao", novaTransacao);
                setResult(RESULT_OK, resultIntent);
                finish();
            } catch (NumberFormatException e) {
                Log.d("TAG", "Valor inválido: " + valorString);
            }
        } else {
            Log.d("TAG", "Por favor, preencha todos os campos");
        }
    }

    private void openFABMenu() {
        isFabOpen = true;
        fabDespesa.setVisibility(View.VISIBLE);
        fabReceita.setVisibility(View.VISIBLE);

        fabDespesa.animate().translationY(-100).setDuration(300);
        fabReceita.animate().translationY(-180).setDuration(300);
    }

    private void closeFABMenu() {
        isFabOpen = false;
        fabDespesa.animate().translationY(0).setDuration(300).withEndAction(() -> fabDespesa.setVisibility(View.GONE));
        fabReceita.animate().translationY(0).setDuration(300).withEndAction(() -> fabReceita.setVisibility(View.GONE));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
