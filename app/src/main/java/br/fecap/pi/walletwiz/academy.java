package br.fecap.pi.walletwiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import br.fecap.walletwiz.R;

public class academy extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView title1, content1, icon1, title2, content2, icon2, title3, content3, icon3, title4, content4, icon4, title5, content5, icon5, title6, content6, icon6, title7, content7, icon7, title8, content8, icon8;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy);

        // Inicializar as views
        title1 = findViewById(R.id.title1);
        content1 = findViewById(R.id.content1);
        icon1 = findViewById(R.id.icon1);
        image1 = findViewById(R.id.image1);

        title2 = findViewById(R.id.title2);
        content2 = findViewById(R.id.content2);
        icon2 = findViewById(R.id.icon2);
        image2 = findViewById(R.id.image2);

        title3 = findViewById(R.id.title3);
        content3 = findViewById(R.id.content3);
        icon3 = findViewById(R.id.icon3);
        image3 = findViewById(R.id.image3);

        title4 = findViewById(R.id.title4);
        content4 = findViewById(R.id.content4);
        icon4 = findViewById(R.id.icon4);
        image4 = findViewById(R.id.image4);

        title5 = findViewById(R.id.title5);
        content5 = findViewById(R.id.content5);
        icon5 = findViewById(R.id.icon5);
        image5 = findViewById(R.id.image5);

        title6 = findViewById(R.id.title6);
        content6 = findViewById(R.id.content6);
        icon6 = findViewById(R.id.icon6);
        image6 = findViewById(R.id.image6);

        title7 = findViewById(R.id.title7);
        content7 = findViewById(R.id.content7);
        icon7 = findViewById(R.id.icon7);
        image7 = findViewById(R.id.image7);

        title8 = findViewById(R.id.title8);
        content8 = findViewById(R.id.content8);
        icon8 = findViewById(R.id.icon8);
        image8 = findViewById(R.id.image8);



        TextView contentInvest = findViewById(R.id.content1);
        content1.setText(Html.fromHtml(getString(R.string.content_invest), Html.FROM_HTML_MODE_LEGACY));

        TextView contenthabitosfinanceiros = findViewById(R.id.content2);
        content2.setText(Html.fromHtml(getString(R.string.content_habitos_financeiros), Html.FROM_HTML_MODE_LEGACY));

        TextView contentapostasnaoinvestimento = findViewById(R.id.content3);
        content3.setText(Html.fromHtml(getString(R.string.content_apostas_nao_investimento), Html.FROM_HTML_MODE_LEGACY));

        TextView contentlogicacassinos = findViewById(R.id.content4);
        content4.setText(Html.fromHtml(getString(R.string.content_logica_cassinos), Html.FROM_HTML_MODE_LEGACY));

        TextView contentrendafixa = findViewById(R.id.content5);
        content5.setText(Html.fromHtml(getString(R.string.content_renda_fixa), Html.FROM_HTML_MODE_LEGACY));

        TextView contentrendavariavel = findViewById(R.id.content6);
        content6.setText(Html.fromHtml(getString(R.string.content_renda_variavel), Html.FROM_HTML_MODE_LEGACY));

        TextView contentinvestimento = findViewById(R.id.content7);
        content7.setText(Html.fromHtml(getString(R.string.content_investimento), Html.FROM_HTML_MODE_LEGACY));

        TextView contentcriptomoedas = findViewById(R.id.content8);
        content8.setText(Html.fromHtml(getString(R.string.content_criptomoedas), Html.FROM_HTML_MODE_LEGACY));



        // Configurar clique para alternar visibilidade do conteúdo 1
        findViewById(R.id.accordion1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content1, icon1, image1);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 2
        findViewById(R.id.accordion2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content2, icon2,image2);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 3
        findViewById(R.id.accordion3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content3, icon3, image3);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 4
        findViewById(R.id.accordion4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content4, icon4, image4);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 5
        findViewById(R.id.accordion5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content5, icon5, image5);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 6
        findViewById(R.id.accordion6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content6, icon6, image6);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 7
        findViewById(R.id.accordion7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content7, icon7, image7);
            }
        });

        // Configurar clique para alternar visibilidade do conteúdo 6
        findViewById(R.id.accordion8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleContent(content8, icon8, image8);
            }
        });
    }

    // Função para alternar a visibilidade do conteúdo e o ícone
    private void toggleContent(TextView content, TextView icon, ImageView image) {
        if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            icon.setText("-");
        } else {
            content.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            icon.setText("+");
        }
    } private void setupDrawer() {
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
                startActivity(new Intent(academy.this, session.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_acount) {
                startActivity(new Intent(academy.this, perfil.class));
                return true;
            } else if (item.getItemId() == R.id.nav_academy) {
                startActivity(new Intent(academy.this, academy.class));
                return true;
            } else if (item.getItemId() == R.id.nav_simulador_aquisicoes) {
                startActivity(new Intent(academy.this, simulador_aquisicoes.class));
                return true;
            }else if (item.getItemId() == R.id.nav_extract) {
                startActivity(new Intent(academy.this, extrato.class));
                return true;
            }

            return false;
        });
    }
}