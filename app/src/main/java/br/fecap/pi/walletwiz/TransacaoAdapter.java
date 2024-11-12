package br.fecap.pi.walletwiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import br.fecap.walletwiz.R;

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
        holder.textViewDescricao.setText(transacao.getData() + ": " + transacao.getTipo() + " - R$" + transacao.getValor() + " " + transacao.getCategoria());

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (transacao.getTipo().equals("receita")) {
                intent = new Intent(context, receita.class);
            } else {
                intent = new Intent(context, despesa.class);
            }
            intent.putExtra("transacao", transacao);
            ((extrato) context).startActivityForResult(intent, 2);
        });

        holder.itemView.setOnLongClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Escolha uma ação")
                    .setItems(new CharSequence[]{"Editar", "Excluir"}, (dialog, which) -> {
                        switch (which) {
                            case 0: // Editar
                                Intent editIntent;
                                if (transacao.getTipo().equals("receita")) {
                                    editIntent = new Intent(context, receita.class); // Inicie a Activity de receita
                                } else {
                                    editIntent = new Intent(context, despesa.class); // Inicie a Activity de despesa
                                }
                                editIntent.putExtra("transacao", transacao);
                                editIntent.putExtra("position", position);
                                ((extrato) context).startActivityForResult(editIntent, 2);
                                break;
                            case 1: // Excluir
                                ((extrato) context).atualizarTransacao(position);
                                Toast.makeText(context, "Transação removida", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
            builder.create().show();
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
