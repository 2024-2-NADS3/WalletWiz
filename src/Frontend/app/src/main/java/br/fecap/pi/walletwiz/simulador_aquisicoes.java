package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import br.fecap.walletwiz.R;
import java.util.List;

public class simulador_aquisicoes extends AppCompatActivity implements SimulacaoAdapter.OnSimulacaoClickListener {
    private TextView textNomeAquisicao, textValorTotal, textValorGuardado, textPorcentagem;
    private ProgressBar progressBarSimulacao;
    private FloatingActionButton fabAdd;
    private SimulacaoAquisicao simulacao;
    private RecyclerView recyclerView;
    private SimulacaoAdapter adapter;
    private RecyclerView recyclerViewExtrato;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador_aquisicoes);


        textNomeAquisicao = findViewById(R.id.textNomeAquisicao);
        textValorTotal = findViewById(R.id.textValorTotal);
        textValorGuardado = findViewById(R.id.textValorGuardado);
        progressBarSimulacao = findViewById(R.id.progressBarSimulacao);
        textPorcentagem = findViewById(R.id.textPorcentagem);
        fabAdd = findViewById(R.id.fab_add);


        recyclerView = findViewById(R.id.recyclerViewAquisicoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        simulacao = new SimulacaoAquisicao("Exemplo de Aquisição", 10000); // Exemplo de criação de uma simulação
        atualizarViews();


        atualizarListaSimulacoes();


        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(simulador_aquisicoes.this, simulador.class); // Substitua 'simulador' pela Activity que deseja abrir
            startActivity(intent);
        });

        setupDrawer();
        FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
    }

    //////////////////////////////

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
                startActivity(new Intent(simulador_aquisicoes.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(simulador_aquisicoes.this, academy.class));
                return true;
            }
            else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(simulador_aquisicoes.this, extrato.class));
                return true;
            }

            return false;
        });
    }
    ////////////////////////////////////////////////////////////


    private void atualizarListaSimulacoes() {

        List<SimulacaoAquisicao> simulacoes = simulador.listaSimulacoes;


        adapter = new SimulacaoAdapter(simulacoes, this);
        recyclerView.setAdapter(adapter);
    }

    // Método para atualizar as views com os valores da simulação
    private void atualizarViews() {
        textNomeAquisicao.setText(simulacao.getNome());
        textValorTotal.setText("Valor Total: R$ " + String.format("%.2f", simulacao.getValorTotal()));
        textValorGuardado.setText("Valor Guardado: R$ " + String.format("%.2f", simulacao.getValorGuardado()));
        progressBarSimulacao.setMax(100); // A barra de progresso vai de 0 a 100
        progressBarSimulacao.setProgress((int) simulacao.calcularPorcentagem()); // Calcula a porcentagem de progresso
        textPorcentagem.setText("Progresso: " + String.format("%.2f", simulacao.calcularPorcentagem()) + "%");
    }

    @Override
    public void onSimulacaoLongClick(SimulacaoAquisicao simulacao) {
        // Exibe um dialog para editar ou excluir
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        String[] options = {"Editar", "Excluir"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Editar
                Intent intent = new Intent(simulador_aquisicoes.this, simulador.class);
                // Passar os dados da simulação para a activity de edição
                intent.putExtra("SIMULACAO", simulacao);
                startActivity(intent);
            } else if (which == 1) {
                // Excluir
                excluirSimulacao(simulacao);
            }
        });
        builder.show();
    }

    private void excluirSimulacao(SimulacaoAquisicao simulacao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Exclusão");
        builder.setMessage("Você tem certeza que deseja excluir esta simulação?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            // Remover a simulação da lista e atualizar o RecyclerView
            simulador.listaSimulacoes.remove(simulacao);
            atualizarListaSimulacoes();
        });
        builder.setNegativeButton("Não", null);
        builder.show();
    }
}
