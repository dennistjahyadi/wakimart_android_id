package com.wakimart.wakimartindonesia;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
    private Button btnLogout, btnVoucher, btnWebsite, btnPromo, btnPriceList,btnVideo;

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
        btnVoucher = findViewById(R.id.btnVoucher);
        btnWebsite = findViewById(R.id.btnWebsite);
        btnPromo = findViewById(R.id.btnPromo);
        btnPriceList = findViewById(R.id.btnPriceList);
        btnVideo = findViewById(R.id.btnVideo);
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

        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.wakimartWebsite));
                startActivity(browserIntent);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VideoListActivity.class);
                startActivity(i);
            }
        });
    }
}
