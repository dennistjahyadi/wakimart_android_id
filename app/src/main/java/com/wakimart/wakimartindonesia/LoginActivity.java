package com.wakimart.wakimartindonesia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wakimart.wakimartindonesia.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private Button btnLogin, btnRegister;
    private FrameLayout loadingBar;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SharedPreferenceUtils.getPrefs(getApplicationContext()).getString(SharedPreferenceUtils.PREFERENCES_USER_DATA, "").equals("")) {
            getSupportActionBar().hide();
            doLogin();
        }else{
            setContentView(R.layout.activity_login);
            init();
        }
    }

    private void init() {
        getSupportActionBar().hide();
        loadingBar = findViewById(R.id.loadingBar);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(LoginActivity.this);
                doLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void doLogin() {
        if(loadingBar!=null) {
            loadingBar.setVisibility(View.VISIBLE);
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!SharedPreferenceUtils.getPrefs(getApplicationContext()).getString(SharedPreferenceUtils.PREFERENCES_USER_DATA, "").equals("")) {
                String login = SharedPreferenceUtils.getPrefs(getApplicationContext()).getString(SharedPreferenceUtils.PREFERENCES_USER_LOGIN, "");
                String password = SharedPreferenceUtils.getPrefs(getApplicationContext()).getString(SharedPreferenceUtils.PREFERENCES_USER_PASSWORD, "");
                jsonObject.put("login", login);
                jsonObject.put("password", password);
            } else {
                jsonObject.put("login", etLogin.getText().toString());
                jsonObject.put("password", etPassword.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(Utils.API_URL + "login")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jsonData = response.getString("data");
                            SharedPreferenceUtils.setPrefs(getApplicationContext(), SharedPreferenceUtils.PREFERENCES_USER_DATA, jsonData);
                            SharedPreferenceUtils.setPrefs(getApplicationContext(), SharedPreferenceUtils.PREFERENCES_USER_LOGIN, jsonObject.getString("login"));
                            SharedPreferenceUtils.setPrefs(getApplicationContext(), SharedPreferenceUtils.PREFERENCES_USER_PASSWORD, jsonObject.getString("password"));
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if(loadingBar!=null) {
                            loadingBar.setVisibility(View.GONE);
                        }
                        SharedPreferenceUtils.removeAllPrefs(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
