package br.fecap.pi.walletwiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.fecap.walletwiz.R;

public class SimulacaoAdapter extends RecyclerView.Adapter<SimulacaoAdapter.SimulacaoViewHolder> {
    private List<SimulacaoAquisicao> simulacoes;
    private OnSimulacaoClickListener listener;

    public SimulacaoAdapter(List<SimulacaoAquisicao> simulacoes, OnSimulacaoClickListener listener) {
        this.simulacoes = simulacoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimulacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simulacao, parent, false);
        return new SimulacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimulacaoViewHolder holder, int position) {
        SimulacaoAquisicao simulacao = simulacoes.get(position);
        holder.textNomeAquisicao.setText(simulacao.getNome());
        holder.textValorTotal.setText("Valor Total: R$ " + simulacao.getValorTotal());
        holder.textValorGuardado.setText("Valor Guardado: R$ " + simulacao.getValorGuardado());
        holder.progressBarSimulacao.setProgress(simulacao.getProgresso());
        holder.textPorcentagem.setText("Progresso: " + simulacao.getProgresso() + "%");


        holder.itemView.setOnLongClickListener(v -> {
            listener.onSimulacaoLongClick(simulacao);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return simulacoes.size();
    }

    public interface OnSimulacaoClickListener {
        void onSimulacaoLongClick(SimulacaoAquisicao simulacao);
    }

    public static class SimulacaoViewHolder extends RecyclerView.ViewHolder {
        TextView textNomeAquisicao, textValorTotal, textValorGuardado, textPorcentagem;
        ProgressBar progressBarSimulacao;

        public SimulacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeAquisicao = itemView.findViewById(R.id.textNomeAquisicao);
            textValorTotal = itemView.findViewById(R.id.textValorTotal);
            textValorGuardado = itemView.findViewById(R.id.textValorGuardado);
            progressBarSimulacao = itemView.findViewById(R.id.progressBarSimulacao);
            textPorcentagem = itemView.findViewById(R.id.textPorcentagem);
        }
    }
}
