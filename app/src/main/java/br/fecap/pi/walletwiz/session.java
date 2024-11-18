package br.fecap.pi.walletwiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import br.fecap.pi.walletwiz.model.UserBalance;
import br.fecap.walletwiz.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class session extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private List<transacao> transacoes = new ArrayList<>();
    private OkHttpClient _client = new OkHttpClient();
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session);

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
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);

            if (id == R.id.nav_home) {
                navigateToActivity(session.class);
                return true;

            } else if (id == R.id.nav_extract) {
                Intent intentExtract = new Intent(session.this, extrato.class);
                intentExtract.putExtra("transacoes", (ArrayList<transacao>) transacoes);
                startActivity(intentExtract);
                finish();
                return true;

            } else if (id == R.id.nav_simulador_aquisicoes) {
                navigateToActivity(simulador_aquisicoes.class);
                return true;

            } else if (id == R.id.nav_academy) {
                navigateToActivity(academy.class);
                return true;

            }else {
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fabAdd = findViewById(R.id.fab_add);
        fabDespesa = findViewById(R.id.fab_expense);
        fabReceita = findViewById(R.id.fab_income);

        fabDespesa.setVisibility(View.GONE);
        fabReceita.setVisibility(View.GONE);

        fabAdd.setOnClickListener(v -> {
            if (fabDespesa.getVisibility() == View.GONE) {
                fabDespesa.setVisibility(View.VISIBLE);
                fabReceita.setVisibility(View.VISIBLE);
            } else {
                fabDespesa.setVisibility(View.GONE);
                fabReceita.setVisibility(View.GONE);
            }
        });

        fabDespesa.setOnClickListener(v -> {
            Log.d("TAG", "Despesa button clicked");
            Intent intent = new Intent(session.this, despesa.class);
            intent.putExtra("transacoes", (ArrayList<transacao>) transacoes);
            startActivityForResult(intent, 1);
        });

        fabReceita.setOnClickListener(v -> {
            Log.d("TAG", "Receita button clicked");
            Intent intent = new Intent(session.this, receita.class);
            startActivityForResult(intent, 2);
        });

        ///Grafico
        pieChart = findViewById(R.id.pieChart);
        getBalance(2);

    }

    /// Metodo para gerenciar a navegação
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) { // Despesa
                transacao novaDespesa = (transacao) data.getSerializableExtra("nova_transacao");
                if (novaDespesa != null) {
                    transacoes.add(novaDespesa);
                    Log.d("TAG", "Despesa adicionada: " + novaDespesa);
                }
            } else if (requestCode == 2) { // Receita
                transacao novaReceita = (transacao) data.getSerializableExtra("nova_transacao");
                if (novaReceita != null) {
                    transacoes.add(novaReceita);
                    Log.d("TAG", "Receita adicionada: " + novaReceita);
                }
            }

//            calcularBalanco();
        }
    }

    public void calcularBalanco(UserBalance userBalance) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                TextView textBalanco = findViewById(R.id.textBalanco);
                TextView textSalValue = findViewById(R.id.textSalValue);
                TextView textDesValue = findViewById(R.id.textDesValue);

                textBalanco.setText(String.format("Balanço: %.2f", userBalance.total_balance));
                textSalValue.setText(String.format("R$ %.2f", userBalance.total_income));
                textDesValue.setText(String.format("R$ %.2f", userBalance.total_outgoing));

            }
        });

    }

    public void getBalance(int id) {
        String url = "http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/users";
        String path = String.format("%s/%s/show_balance", url, id);
        Request getRequest = new Request.Builder()
                .url(path)
                .build();

        _client.newCall(getRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UserBalance u = new Gson().fromJson(response.body().string(), UserBalance.class);
                renderChart(u);
                calcularBalanco(u);
            }
        });
    }

    private void renderChart(final UserBalance userBalance) {
        final int red = ContextCompat.getColor(this, android.R.color.holo_red_dark);
        final int green = ContextCompat.getColor(this, android.R.color.holo_green_dark);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {


                ArrayList<PieEntry> balancos = new ArrayList<>();

                balancos.add(new PieEntry(userBalance.total_outgoing * -1, "Despesa"));
                balancos.add(new PieEntry(userBalance.total_income, "Receita"));

                List<Integer> colors = new ArrayList<Integer>();
                colors.add(red);
                colors.add(green);

                PieDataSet pieDataSet = new PieDataSet(balancos, "Balanços");
                pieDataSet.setColors(colors);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Balanços");
                pieChart.animate();

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            }
        });

    }
}
