package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;

import br.fecap.pi.walletwiz.model.TransactionType;
import br.fecap.walletwiz.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class receita extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private int userId;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private EditText editTextDescricao, editTextValor, editTextNome;
    private Button buttonSelectDate, buttonSalvar;
    private Spinner spinnerReceita;
    private OkHttpClient _client = new OkHttpClient();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", 0);

        setContentView(R.layout.activity_receita);

        initViews();
        setupSpinner();
        setupDrawer();
        setupDatePicker();
        setupSaveButton();

        FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
    }

    private void initViews() {
        editTextNome = findViewById(R.id.editTextNome);
        editTextDescricao = findViewById(R.id.editTextObservacao);
        editTextValor = findViewById(R.id.editTextReceita);
        buttonSelectDate = findViewById(R.id.btn_select_date);
        buttonSalvar = findViewById(R.id.buttonSalvarReceita);
        spinnerReceita = findViewById(R.id.spinnerReceita);
    }


    private void setupSpinner() {
        Request request = new Request.Builder()
                .url("http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/transaction_types")
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(receita.this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        ArrayList<TransactionType> tiposDeReceita = new ArrayList<TransactionType>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TransactionType tt = new TransactionType(jsonArray.getJSONObject(i));
                            tiposDeReceita.add(tt);
                        }

                        runOnUiThread(() -> {
                            ArrayAdapter<TransactionType> adapter = new ArrayAdapter<>(receita.this, android.R.layout.simple_spinner_item, tiposDeReceita);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerReceita.setAdapter(adapter);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(receita.this, "Erro ao processar dados", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(receita.this, "Erro na resposta da API", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ImageView icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            // Verificando o item selecionado e iniciando a activity correspondente
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(receita.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(receita.this, academy.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(receita.this, extrato.class));
                return true;
            }

            return false;
        });
    }

    private void setupDatePicker() {
        buttonSelectDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(receita.this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                buttonSelectDate.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupSaveButton() {
        buttonSalvar.setOnClickListener(v -> adicionarReceita());
    }

    private void adicionarReceita() {
        String nome = editTextNome.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String valor = editTextValor.getText().toString();
        String data = buttonSelectDate.getText().toString();
        TransactionType categoria = (TransactionType) spinnerReceita.getSelectedItem();

        if (!descricao.isEmpty() && !valor.isEmpty() && !data.isEmpty() && !nome.isEmpty()) {
            try {
                final Intent resultIntent = new Intent();

                Map<String, Object> objectBody = new HashMap<String, Object>();
                Map<String, Object> transaction = new HashMap<String, Object>();
                transaction.put("nome", nome);
                transaction.put("observacao", descricao);
                transaction.put("valor", Float.parseFloat(valor));
                transaction.put("data", data);
                transaction.put("user_id", userId);
                transaction.put("transaction_type_id", categoria.id);
                transaction.put("transaction_tag", "receita");
                objectBody.put("transaction", transaction);

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(objectBody));

                String url = "http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/transactions";
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                _client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Toast.makeText(receita.this, "Receita Salva", Toast.LENGTH_SHORT).show();
                        resultIntent.putExtra("new", true);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
            } catch (NumberFormatException e) {
                Log.d("TAG", "Valor inválido: " + valor);
            }
        } else {
            Log.d("TAG", "Por favor, preencha todos os campos");
        }
    }
}
