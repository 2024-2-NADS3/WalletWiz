package com.walletwiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransacaoAdapter extends RecyclerView.Adapter<TransacaoAdapter.ViewHolder> {

    private List<transacao> transacoes;
    private Context context;

    public TransacaoAdapter(List<transacao> transacoes, Context context) {
        this.transacoes = transacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        transacao transacao = transacoes.get(position);
        holder.textViewDescricao.setText(transacao.getData() + ": " + transacao.getTipo() + " - R$" + transacao.getValor());

        holder.itemView.setOnClickListener(v -> {
            // Lógica para editar a transação
            Intent intent = new Intent(context, receita.class);
            intent.putExtra("transacao", transacao);
            ((extrato) context).startActivityForResult(intent, 2);
        });

        holder.itemView.setOnLongClickListener(v -> {
            ((extrato) context).atualizarTransacao(position);
            Toast.makeText(context, "Transação removida", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return transacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDescricao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescricao = itemView.findViewById(R.id.text_view_descricao);
        }
    }
}
