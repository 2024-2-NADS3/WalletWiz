package br.fecap.pi.walletwiz;

import android.os.Parcel;
import android.os.Parcelable;

public class SimulacaoAquisicao implements Parcelable {
    private String nome;
    private double valorTotal;
    private double valorGuardado;
    private int progresso;

    public SimulacaoAquisicao(String nome, double valorTotal) {
        this.nome = nome;
        this.valorTotal = valorTotal;
        this.valorGuardado = 0;
        this.progresso = 0;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
        atualizarProgresso();
    }

    public double getValorGuardado() {
        return valorGuardado;
    }

    public void adicionarValor(double valorAdicionar) {
        this.valorGuardado += valorAdicionar;
        atualizarProgresso();
    }

    public int getProgresso() {
        return progresso;
    }

    private void atualizarProgresso() {
        if (valorTotal > 0) {
            this.progresso = (int) ((valorGuardado / valorTotal) * 100);
        } else {
            this.progresso = 0;
        }
    }


    public double calcularPorcentagem() {
        return valorTotal > 0 ? (valorGuardado / valorTotal) * 100 : 0;
    }


    protected SimulacaoAquisicao(Parcel in) {
        nome = in.readString();
        valorTotal = in.readDouble();
        valorGuardado = in.readDouble();
        progresso = in.readInt();
    }

    public static final Creator<SimulacaoAquisicao> CREATOR = new Creator<SimulacaoAquisicao>() {
        @Override
        public SimulacaoAquisicao createFromParcel(Parcel in) {
            return new SimulacaoAquisicao(in);
        }

        @Override
        public SimulacaoAquisicao[] newArray(int size) {
            return new SimulacaoAquisicao[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeDouble(valorTotal);
        dest.writeDouble(valorGuardado);
        dest.writeInt(progresso);
    }
}
