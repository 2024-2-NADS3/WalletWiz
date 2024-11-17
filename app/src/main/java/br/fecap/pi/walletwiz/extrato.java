package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import br.fecap.walletwiz.R;

import java.util.ArrayList;
import java.util.List;


public class extrato extends AppCompatActivity {

    private RecyclerView recyclerViewExtrato;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TransacaoAdapter transacaoAdapter;
    private List<transacao> transacoes = new ArrayList<>();
    private static final int ADD_TRANSACTION_REQUEST = 1;
    private static final int EDIT_TRANSACTION_REQUEST = 2;

    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);



        recyclerViewExtrato = findViewById(R.id.recyclerViewExtrato);
        recyclerViewExtrato.setLayoutManager(new LinearLayoutManager(this));

        // Obter transações da sessão
        transacoes = (ArrayList<transacao>) getIntent().getSerializableExtra("transacoes");
        if (transacoes == null) {
            transacoes = new ArrayList<>();
        }

        transacaoAdapter = new TransacaoAdapter(transacoes, this);
        recyclerViewExtrato.setAdapter(transacaoAdapter);

        // Configuração do FAB
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

        // Configuração dos botões de despesa e receita
        fabDespesa.setOnClickListener(v -> {
            Intent intent = new Intent(extrato.this, despesa.class);
            intent.putExtra("transacoes", (ArrayList<transacao>) transacoes);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });

        fabReceita.setOnClickListener(v -> {
            Intent intent = new Intent(extrato.this, receita.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });

        setupDrawer();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
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

    ////////////////////
    private void setupDrawer() {
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

            // Verificando o item selecionado e iniciando a activity correspondente
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(extrato.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_acount) {
                startActivity(new Intent(extrato.this, perfil.class));
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(extrato.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_simulador_aquisicoes) {
                startActivity(new Intent(extrato.this, simulador_aquisicoes.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(extrato.this, extrato.class));
                return true;
            }

            return false;
        });
    }

///////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_TRANSACTION_REQUEST) {
                transacao novaTransacao = (transacao) data.getSerializableExtra("nova_transacao");
                if (novaTransacao != null) {
                    transacoes.add(novaTransacao);
                    transacaoAdapter.notifyItemInserted(transacoes.size() - 1);
                }
            } else if (requestCode == EDIT_TRANSACTION_REQUEST) {
                // Atualizar a transação editada
                transacao transacaoEditada = (transacao) data.getSerializableExtra("transacao");
                int position = data.getIntExtra("position", -1);
                if (transacaoEditada != null && position != -1) {
                    transacoes.set(position, transacaoEditada);
                    transacaoAdapter.notifyItemChanged(position);
                    Toast.makeText(this, "Transação editada com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void atualizarTransacao(int position) {
        transacoes.remove(position);
        transacaoAdapter.notifyItemRemoved(position);
    }
}
