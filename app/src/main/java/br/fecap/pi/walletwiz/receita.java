package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

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
    private session sessionInstance;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private boolean isFabOpen = false;
    private List<transacao> transacoes = new ArrayList<>();

    private EditText editTextDescricao, editTextValor;
    private Button buttonSelectDate, buttonSalvar;
    private Spinner spinnerReceita;
    private OkHttpClient _client = new OkHttpClient();

    private transacao transacaoParaEditar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        transacoes = (ArrayList<transacao>) getIntent().getSerializableExtra("transacoes");
        if (transacoes == null) {
            transacoes = new ArrayList<>();
        }

        // Receber a transação para edição
        transacaoParaEditar = (transacao) getIntent().getSerializableExtra("transacao");

        initViews();
        setupSpinner();
        setupDrawer();
        setupFloatingActionButton();
        setupDatePicker();
        setupSaveButton();


        if (transacaoParaEditar != null) {
            preencherCamposComTransacao(transacaoParaEditar);
        }

        FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
    }

    private void initViews() {
        editTextDescricao = findViewById(R.id.editTextObs);
        editTextValor = findViewById(R.id.editTextReceita);
        buttonSelectDate = findViewById(R.id.btn_select_date);
        buttonSalvar = findViewById(R.id.buttonSalvarReceita);
        spinnerReceita = findViewById(R.id.spinnerReceita);
    }


    private void setupSpinner() {
        ArrayList<String> tiposDeReceitas = new ArrayList<>();
        tiposDeReceitas.add("Salário");
        tiposDeReceitas.add("Renda extra");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposDeReceitas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReceita.setAdapter(adapter);
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
            } else if (item.getItemId() == R.id.nav_acount) {
                startActivity(new Intent(receita.this, perfil.class));
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(receita.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_simulador_aquisicoes) {
                startActivity(new Intent(receita.this, simulador_aquisicoes.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(receita.this, extrato.class));
                return true;
            }

            return false;
        });
    }



    private void setupFloatingActionButton() {
        fabAdd = findViewById(R.id.fab_add);
        fabDespesa = findViewById(R.id.fab_expense);
        fabReceita = findViewById(R.id.fab_income);

        fabDespesa.setVisibility(View.GONE);
        fabReceita.setVisibility(View.GONE);

        fabAdd.setOnClickListener(v -> {
            if (isFabOpen) {
                closeFABMenu();
            } else {
                openFABMenu();
            }
        });

        fabDespesa.setOnClickListener(v -> {
            Log.d("TAG", "Despesa button clicked");
            Intent intent = new Intent(receita.this, despesa.class);
            intent.putExtra("transacoes", (ArrayList<transacao>) transacoes);
            startActivityForResult(intent, 1);
        });

        fabReceita.setOnClickListener(v -> {
            Log.d("TAG", "Receita button clicked");
            Intent intent = new Intent(receita.this, receita.class);
            startActivityForResult(intent, 2);
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
        String descricao = editTextDescricao.getText().toString();
        String valor = editTextValor.getText().toString();
        String data = buttonSelectDate.getText().toString();
//        String categoria = spinnerGastos.getSelectedItem().toString();

        if (!descricao.isEmpty() && !valor.isEmpty() && !data.isEmpty()) {
            try {
                final Intent resultIntent = new Intent();

                Map<String, Object> objectBody = new HashMap<String, Object>();
                Map<String, Object> transaction = new HashMap<String, Object>();
                transaction.put("observacao", descricao);
                transaction.put("valor", Float.parseFloat(valor));
                transaction.put("data", data);
                transaction.put("user_id", 2);
                transaction.put("transaction_type_id", 2);
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

    private void preencherCamposComTransacao(transacao transacao) {
        editTextDescricao.setText(transacao.getTipo());
        editTextValor.setText(String.valueOf(transacao.getValor()));
        buttonSelectDate.setText(transacao.getData());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerReceita.getAdapter();
        int spinnerPosition = adapter.getPosition(transacao.getCategoria());
        spinnerReceita.setSelection(spinnerPosition);
    }

    private void openFABMenu() {
        isFabOpen = true;
        fabDespesa.setVisibility(View.VISIBLE);
        fabReceita.setVisibility(View.VISIBLE);
        fabDespesa.animate().translationY(-100).setDuration(300);
        fabReceita.animate().translationY(-180).setDuration(300);
    }

    private void closeFABMenu() {
        isFabOpen = false;
        fabDespesa.animate().translationY(0).setDuration(300).withEndAction(() -> fabDespesa.setVisibility(View.GONE));
        fabReceita.animate().translationY(0).setDuration(300).withEndAction(() -> fabReceita.setVisibility(View.GONE));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
