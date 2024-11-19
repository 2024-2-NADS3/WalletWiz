package br.fecap.pi.walletwiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.fecap.pi.walletwiz.model.Transaction;
import br.fecap.walletwiz.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class extrato extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int userId;
    private OkHttpClient _client = new OkHttpClient();
    private List<Transaction> transactions = new ArrayList<Transaction>();
    private RecyclerView recyclerViewExtrato;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TransacaoAdapter adapter;

    private static final int ADD_TRANSACTION_REQUEST = 1;

    private FloatingActionButton fabAdd, fabDespesa, fabReceita;
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Se o usuário não estiver logado, redirecionar para o Login
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, login.class));
            finish();
            return;
        }

        userId = sharedPreferences.getInt("userId", 0);

        setContentView(R.layout.activity_extrato);

        recyclerViewExtrato = findViewById(R.id.recyclerViewExtrato);
        recyclerViewExtrato.setLayoutManager(new LinearLayoutManager(this));

        getTransactions(userId);

        adapter = new TransacaoAdapter(transactions, this);
        recyclerViewExtrato.setAdapter(adapter);

        // Configuração do FAB
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

        // Configuração dos botões de despesa e receita
        fabDespesa.setOnClickListener(v -> {
            Intent intent = new Intent(extrato.this, despesa.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });

        fabReceita.setOnClickListener(v -> {
            Intent intent = new Intent(extrato.this, receita.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });

        setupDrawer();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton fabBack = findViewById(R.id.back);
        fabBack.setOnClickListener(v -> onBackPressed());
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

    ////////////////////
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
                startActivity(new Intent(extrato.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(extrato.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(extrato.this, extrato.class));
                return true;
            }

            return false;
        });
    }

///////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            transactions = new ArrayList<Transaction>();
            getTransactions(userId);
        }
    }

    private void getTransactions(int id) {
        String url = "http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/users";
        String path = String.format("%s/%s/transactions", url, id);
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
                try {
                    JSONArray arr = new JSONArray(response.body().string());
                    for (int i = 0; i < arr.length(); i++) {
                        Transaction t = new Transaction(arr.getJSONObject(i));
                        transactions.add(t);
                    }
                    runOnUiThread(() -> {
                        adapter = new TransacaoAdapter(transactions, extrato.this);
                        recyclerViewExtrato.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
