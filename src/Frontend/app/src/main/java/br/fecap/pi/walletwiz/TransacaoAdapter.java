package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import br.fecap.pi.walletwiz.model.ENUM_TYPE;
import br.fecap.pi.walletwiz.model.Transaction;
import br.fecap.walletwiz.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransacaoAdapter extends RecyclerView.Adapter<TransacaoAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private Context context;

    public TransacaoAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transacao, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        final boolean isIncome = transaction.getTipo() == ENUM_TYPE.INCOME;

        holder.txtTitle.setText(String.format("[%s] %s", isIncome ? "Receita" : "Despesa", transaction.nome));
        holder.txtValor.setText(String.format("R$ %.2f", transaction.valor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.txtData.setText(transaction.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }


        int green = Color.parseColor("#388E3C");
        int red = Color.RED;

        // Define a cor do texto com base no tipo de transação
        if (isIncome) {
            holder.txtTitle.setTextColor(green);
            holder.txtValor.setTextColor(green);
            holder.txtData.setTextColor(green);
        } else {
            holder.txtTitle.setTextColor(red);
            holder.txtValor.setTextColor(red);
            holder.txtData.setTextColor(red);
        }

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent;
//            if (isIncome) {
//                intent = new Intent(context, receita.class);
//            } else {
//                intent = new Intent(context, despesa.class);
//            }
//            intent.putExtra("transaction", transaction.toJson());
//            ((extrato) context).startActivityForResult(intent, 2);
//        });
//
//        holder.itemView.setOnLongClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Escolha uma ação")
//                    .setItems(new CharSequence[]{"Editar", "Excluir"}, (dialog, which) -> {
//                        switch (which) {
//                            case 0: // Editar
//                                Intent editIntent;
//                                if (isIncome) {
//                                    editIntent = new Intent(context, receita.class); // Inicie a Activity de receita
//                                } else {
//                                    editIntent = new Intent(context, despesa.class); // Inicie a Activity de despesa
//                                }
//                                editIntent.putExtra("transaction", transaction.toJson());
//                                ((extrato) context).startActivityForResult(editIntent, 2);
//                                break;
//                            case 1: // Excluir
//                                ((extrato) context).atualizarTransacao(position);
//                                Toast.makeText(context, "Transação removida", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    });
//            builder.create().show();
//            return true;
//        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtValor, txtData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtData = itemView.findViewById(R.id.txtData);
            txtValor = itemView.findViewById(R.id.txtValor);
        }
    }
}
