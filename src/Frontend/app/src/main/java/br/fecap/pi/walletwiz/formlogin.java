package br.fecap.pi.walletwiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.fecap.walletwiz.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class formlogin extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPassword, inputPasswordConfirm;
    private Button signUpButton;
    private TextView loginLink;
    private OkHttpClient _client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formlogin);

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        inputName = findViewById(R.id.textNome);
        inputEmail = findViewById(R.id.textEmail);
        inputPassword = findViewById(R.id.textPassword);
        inputPasswordConfirm = findViewById(R.id.textPasswordConfirm);
        signUpButton = findViewById(R.id.signUpButton);
        loginLink = findViewById(R.id.link_login);

        signUpButton.setOnClickListener(v -> {
            submitRegistry();
        });

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        });
    }

    private void submitRegistry() {
        String nome = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordConfirm = inputPasswordConfirm.getText().toString();


        if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(formlogin.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password.equals(passwordConfirm))) {
            Toast.makeText(formlogin.this, "Senha deve ser igual à confirmacão", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> objectBody = new HashMap<String, Object>();
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("nome", nome);
        user.put("senha", password);
        user.put("email", email);
        objectBody.put("user", user);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(objectBody));

        String url = "http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/users";
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
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(formlogin.this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(formlogin.this, login.class));
                        finish();
                    });
                }
                else {
                    runOnUiThread(() -> {
                        Toast.makeText(formlogin.this, "Não foi possível criar uma conta", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}