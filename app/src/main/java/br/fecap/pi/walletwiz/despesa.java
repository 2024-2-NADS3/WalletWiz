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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import br.fecap.walletwiz.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class despesa extends AppCompatActivity {
    private session sessionInstance;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private boolean isFabOpen = false;
    private List<transacao> transacoes = new ArrayList<>();

    private EditText editTextDescricao, editTextValor;
    private Button buttonSelectDate, buttonSalvar;
    private Spinner spinnerGastos;

    private transacao transacaoParaEditar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
    }

    private void initViews() {
        editTextDescricao = findViewById(R.id.editTextObservacaoDespesa);
        editTextValor = findViewById(R.id.editTextValorDespesa);
        buttonSelectDate = findViewById(R.id.btn_select_date);
        buttonSalvar = findViewById(R.id.buttonDespesa);
        spinnerGastos = findViewById(R.id.spinnerGastos);
    }

    private void setupSpinner() {
        ArrayList<String> tiposDeGastos = new ArrayList<>();
        tiposDeGastos.add("Alimentação");
        tiposDeGastos.add("Transporte");
        tiposDeGastos.add("Saúde");
        tiposDeGastos.add("Educação");
        tiposDeGastos.add("Lazer");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposDeGastos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGastos.setAdapter(adapter);
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
                startActivity(new Intent(despesa.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_acount) {
                startActivity(new Intent(despesa.this, perfil.class));
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(despesa.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_simulador_aquisicoes) {
                startActivity(new Intent(despesa.this, simulador_aquisicoes.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(despesa.this, extrato.class));
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
            Intent intent = new Intent(despesa.this, despesa.class);
            intent.putExtra("transacoes", (ArrayList<transacao>) transacoes);
            startActivityForResult(intent, 1);
        });

        fabReceita.setOnClickListener(v -> {
            Log.d("TAG", "Receita button clicked");
            Intent intent = new Intent(despesa.this, receita.class);
            startActivityForResult(intent, 2);
        });
    }

    private void setupDatePicker() {
        buttonSelectDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(despesa.this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                buttonSelectDate.setText(selectedDate); // Exibe a data selecionada no botão
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setupSaveButton() {
        buttonSalvar.setOnClickListener(v -> adicionarDespesa());
    }

    private void adicionarDespesa() {
        String descricao = editTextDescricao.getText().toString();
        String valorString = editTextValor.getText().toString();
        String data = buttonSelectDate.getText().toString();
        String categoria = spinnerGastos.getSelectedItem().toString();

        if (!descricao.isEmpty() && !valorString.isEmpty() && !data.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorString);
                transacao novaTransacao = new transacao("despesa", valor, data, categoria);

                Intent resultIntent = new Intent();
                if (transacaoParaEditar != null) {
                    // Atualizar a transação existente
                    int position = transacoes.indexOf(transacaoParaEditar);
                    transacoes.set(position, novaTransacao); // Atualiza a transação
                    resultIntent.putExtra("position", position);
                    resultIntent.putExtra("transacao", novaTransacao);
                } else {
                    // Adicionar  transação
                    transacoes.add(novaTransacao);
                    resultIntent.putExtra("nova_transacao", novaTransacao);
                }

                setResult(RESULT_OK, resultIntent);
                finish();
            } catch (NumberFormatException e) {
                Log.d("TAG", "Valor inválido: " + valorString);
            }
        } else {
            Log.d("TAG", "Por favor, preencha todos os campos");
        }
    }

    private void preencherCamposComTransacao(transacao transacao) {
        editTextDescricao.setText(transacao.getTipo());
        editTextValor.setText(String.valueOf(transacao.getValor()));
        buttonSelectDate.setText(transacao.getData());
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerGastos.getAdapter();
        int spinnerPosition = adapter.getPosition(transacao.getCategoria());
        spinnerGastos.setSelection(spinnerPosition);
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
