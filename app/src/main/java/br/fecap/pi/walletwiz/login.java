package br.fecap.pi.walletwiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

public class login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private OkHttpClient _client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, session.class));
            finish();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailInputText);
        passwordEditText = findViewById(R.id.passwordInputText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            authenticateUser(email, password);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void authenticateUser(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {

            Map<String, Object> objectBody = new HashMap<String, Object>();
            objectBody.put("email", email);
            objectBody.put("senha", password);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(objectBody));

            String url = "http://ec2-3-14-146-243.us-east-2.compute.amazonaws.com:3003/users/login";
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
                        try {
                            JSONObject user = new JSONObject(response.body().string());

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putInt("userId", user.getInt("id"));
                            editor.putString("nome", user.getString("nome"));
                            editor.putString("sobrenome", user.getString("sobrenome"));
                            editor.putString("cpf", user.getString("cpf"));
                            editor.putString("endereco", user.getString("endereco"));
                            editor.putString("email", user.getString("email"));
                            editor.apply();

                            runOnUiThread(() -> {
                                startActivity(new Intent(login.this, session.class));
                                finish();
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        runOnUiThread(() -> {
                            Toast.makeText(login.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });


        } else {
            Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
    }
}