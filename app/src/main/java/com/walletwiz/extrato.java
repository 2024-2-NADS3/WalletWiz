package com.walletwiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class extrato extends AppCompatActivity {

    private RecyclerView recyclerViewExtrato;
    private TransacaoAdapter transacaoAdapter;
    private List<transacao> transacoes = new ArrayList<>();
    private static final int ADD_TRANSACTION_REQUEST = 1;
    private static final int EDIT_TRANSACTION_REQUEST = 2;

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

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_extrato);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(extrato.this, receita.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });
    }

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
                // Implementar a edição da transação aqui
                // ...
            }
        }
    }

    public void atualizarTransacao(int position) {
        transacoes.remove(position);
        transacaoAdapter.notifyItemRemoved(position);
    }
}
