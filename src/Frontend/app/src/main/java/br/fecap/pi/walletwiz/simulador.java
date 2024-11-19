package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.fecap.walletwiz.R;

public class simulador extends AppCompatActivity {


    private EditText etNome, etValorTotal, etValorGuardado;
    private TextView tvResultado, tvDataInicio, tvDataFim;
    private Button btnSelecionarInicio, btnSelecionarFim, btnSimular, btnSalvar;

    private Calendar dataInicio, dataFim;
    public static List<SimulacaoAquisicao> listaSimulacoes = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulador);

        // Referenciar os elementos do layout
        etNome = findViewById(R.id.eT_Snome);
        etValorTotal = findViewById(R.id.eT_Svalori);
        etValorGuardado = findViewById(R.id.eT_Svalorg);
        tvResultado = findViewById(R.id.textView4);
        tvDataInicio = findViewById(R.id.textView5);
        tvDataFim = findViewById(R.id.textView6);
        btnSelecionarInicio = findViewById(R.id.btn_select_inicio);
        btnSelecionarFim = findViewById(R.id.btn_select_final);
        btnSimular = findViewById(R.id.button_simular);
        btnSalvar = findViewById(R.id.button_save);

        dataInicio = Calendar.getInstance();
        dataFim = Calendar.getInstance();


        Intent intent = getIntent();
        if (intent.hasExtra("simulacao_nome")) {
            String nome = intent.getStringExtra("simulacao_nome");
            double valorTotal = intent.getDoubleExtra("simulacao_valor_total", 0);
            double valorGuardado = intent.getDoubleExtra("simulacao_valor_guardado", 0);


            etNome.setText(nome);
            etValorTotal.setText(String.valueOf(valorTotal));
            etValorGuardado.setText(String.valueOf(valorGuardado));
        }


        btnSelecionarInicio.setOnClickListener(v -> selecionarData(tvDataInicio, dataInicio));
        btnSelecionarFim.setOnClickListener(v -> selecionarData(tvDataFim, dataFim));


        btnSimular.setOnClickListener(v -> simular());


        btnSalvar.setOnClickListener(v -> salvarSimulacao());

        FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
    }




    private void selecionarData(TextView textView, Calendar data) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    data.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    textView.setText(sdf.format(data.getTime()));
                },
                data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

    private void simular() {
        String valorTotalStr = etValorTotal.getText().toString();
        String dataInicioStr = tvDataInicio.getText().toString();
        String dataFimStr = tvDataFim.getText().toString();

        if (valorTotalStr.isEmpty() || dataInicioStr.isEmpty() || dataFimStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorTotal = Double.parseDouble(valorTotalStr);

        long meses = (dataFim.get(Calendar.YEAR) - dataInicio.get(Calendar.YEAR)) * 12 +
                (dataFim.get(Calendar.MONTH) - dataInicio.get(Calendar.MONTH));

        if (meses <= 0) {
            Toast.makeText(this, "Selecione datas válidas", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorMensal = valorTotal / meses;
        tvResultado.setText(String.format(Locale.getDefault(), "Você deve guardar R$%.2f por mês", valorMensal));
    }

    private void salvarSimulacao() {
        String nomeAquisicao = etNome.getText().toString();
        String valorTotalStr = etValorTotal.getText().toString();
        String valorGuardadoStr = etValorGuardado.getText().toString();

        if (nomeAquisicao.isEmpty() || valorTotalStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }


        SimulacaoAquisicao novaSimulacao = new SimulacaoAquisicao(nomeAquisicao, Double.parseDouble(valorTotalStr));
        listaSimulacoes.add(novaSimulacao);


        Intent intent = new Intent(this, simulador_aquisicoes.class);
        intent.putExtra("nomeAquisicao", nomeAquisicao);
        intent.putExtra("valorTotal", Double.parseDouble(valorTotalStr));
        intent.putExtra("valorGuardado", valorGuardadoStr.isEmpty() ? 0 : Double.parseDouble(valorGuardadoStr));
        intent.putExtra("dataInicio", dataInicio.getTimeInMillis());
        intent.putExtra("dataFim", dataFim.getTimeInMillis());
        startActivity(intent);
    }
}
