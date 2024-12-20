package br.fecap.pi.walletwiz.model;
import android.os.Build;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Transaction {
    public  int id;
    public double valor;
    public LocalDateTime data;
    public String observacao;
    public String nome;
    public TransactionType transaction_type;

    public Transaction() {}
    public Transaction(JSONObject o) throws JSONException {
        this.build(o);
    }

    private void build(JSONObject o) throws JSONException {
        id = o.getInt("id");
        valor = o.getDouble("valor");
        nome = o.getString("nome");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(o.getString("data"));
            data = zonedDateTime.toLocalDateTime();
        }
        observacao = o.getString("observacao");

        transaction_type = new TransactionType(o.getJSONObject("transaction_type"));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public ENUM_TYPE getTipo(){
        if (valor < 0) {
            return ENUM_TYPE.OUTGOING;
        }
        return ENUM_TYPE.INCOME;
    }
}

