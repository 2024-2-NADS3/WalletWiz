package br.fecap.pi.walletwiz;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import br.fecap.walletwiz.R;

import java.util.ArrayList;
import java.util.List;

public class session extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private List<transacao> transacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session);

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
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);

            if (id == R.id.nav_home) {
                navigateToActivity(session.class);
                return true;

            } else if (id == R.id.nav_extract) {
                Intent intentExtract = new Intent(session.this, extrato.class);
                intentExtract.putExtra("transacoes", (ArrayList<transacao>) transacoes);
                startActivity(intentExtract);
                finish();
                return true;

            } else if (id == R.id.nav_simulador_aquisicoes) {
                navigateToActivity(simulador_aquisicoes.class);
                return true;

            } else {
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabAdd = findViewById(R.id.fab_add);
        fabDespesa = findViewById(R.id.fab_expense);
        fabReceita = findViewById(R.id.fab_income);

        fabDespesa.setVisibility(View.GONE);
        fabReceita.setVisibility(View.GONE);

        fabAdd.setOnClickListener(v -> {
            if (fabDespesa.getVisibility() == View.GONE) {
                fabDespesa.setVisibility(View.VISIBLE);
                fabReceita.setVisibility(View.VISIBLE);
            } else {
                fabDespesa.setVisibility(View.GONE);
                fabReceita.setVisibility(View.GONE);
            }
        });

        fabDespesa.setOnClickListener(v -> {
            Log.d("TAG", "Despesa button clicked");
            Intent intent = new Intent(session.this, despesa.class);
            intent.putExtra("transacoes", (ArrayList<transacao>) transacoes);
            startActivityForResult(intent, 1);
        });

        fabReceita.setOnClickListener(v -> {
            Log.d("TAG", "Receita button clicked");
            Intent intent = new Intent(session.this, receita.class);
            startActivityForResult(intent, 2);
        });
    }

    // Método para gerenciar a navegação
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) { // Despesa
                transacao novaDespesa = (transacao) data.getSerializableExtra("nova_transacao");
                if (novaDespesa != null) {
                    transacoes.add(novaDespesa);
                    Log.d("TAG", "Despesa adicionada: " + novaDespesa);
                }
            } else if (requestCode == 2) { // Receita
                transacao novaReceita = (transacao) data.getSerializableExtra("nova_transacao");
                if (novaReceita != null) {
                    transacoes.add(novaReceita);
                    Log.d("TAG", "Receita adicionada: " + novaReceita);
                }
            }

            calcularBalanco();
        }
    }

    public void calcularBalanco() {
        double receitaTotal = 0;
        double despesaTotal = 0;

        for (transacao transacao : transacoes) {
            if (transacao.getTipo().equals("receita")) {
                receitaTotal += transacao.getValor();
            } else if (transacao.getTipo().equals("despesa")) {
                despesaTotal += transacao.getValor();
            }
        }

        TextView textBalanco = findViewById(R.id.textBalanco);
        TextView textSalValue = findViewById(R.id.textSalValue);
        TextView textDesValue = findViewById(R.id.textDesValue);

        textBalanco.setText(String.format("Balanço: %.2f", receitaTotal - despesaTotal));
        textSalValue.setText(String.format("R$ %.2f", receitaTotal));
        textDesValue.setText(String.format("R$ %.2f", despesaTotal));
    }
}
