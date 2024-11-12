package br.fecap.pi.walletwiz;

import java.io.Serializable;

public class transacao implements Serializable {
    private String tipo;
    private double valor;
    private String data;
    private String categoria;


    public transacao(String tipo, double valor, String data, String categoria) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getData() {
        return data;
    }

    public String getCategoria() {return categoria;}
}
