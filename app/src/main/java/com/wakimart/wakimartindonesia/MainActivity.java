package com.wakimart.wakimartindonesia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wakimart.wakimartindonesia.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView tvSubTitle, tvMemberCardName, tvMemberCardCode;
    private Button btnAdvantage, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        getSupportActionBar().hide();
        tvSubTitle = findViewById(R.id.tvSubTitle);
        tvMemberCardCode = findViewById(R.id.tvMemberCardCode);
        tvMemberCardName = findViewById(R.id.tvMemberCardName);
        btnAdvantage = findViewById(R.id.btnAdvantage);
        btnLogout = findViewById(R.id.btnLogout);

        String userData = SharedPreferenceUtils.getPrefs(getApplicationContext()).getString(SharedPreferenceUtils.PREFERENCES_USER_DATA, "");

        if (!userData.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(userData);

                tvSubTitle.setText("Halo, " + jsonObject.getString("name"));
                tvMemberCardName.setText(jsonObject.getString("name"));
                if (jsonObject.getInt("member_type_id") <= 3) {
                    tvMemberCardCode.setText(jsonObject.getString("code"));
                }else{
                    tvMemberCardCode.setText(jsonObject.getString("agent_code"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btnAdvantage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AdvantageActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Apakah anda yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferenceUtils.removeAllPrefs(getApplicationContext());
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });
    }
}
