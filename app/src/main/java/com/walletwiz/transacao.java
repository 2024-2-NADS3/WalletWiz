package com.walletwiz;

import java.io.Serializable;

public class transacao implements Serializable {
    private String tipo;
    private double valor;
    private String data;


    public transacao(String tipo, double valor, String data) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
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
}
