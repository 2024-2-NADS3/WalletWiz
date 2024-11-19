package br.fecap.pi.walletwiz.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionType {
    public int id;
    public String nome;
    public TransactionType() {}
    public TransactionType(JSONObject o) throws JSONException {
        this.id = o.getInt("id");
        this.nome = o.getString("nome");
    }

    @NonNull
    @Override
    public String toString() {
        return this.nome;
    }
}
