package com.wakimart.wakimartindonesia.lookup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wakimart.wakimartindonesia.MainActivity;
import com.wakimart.wakimartindonesia.R;
import com.wakimart.wakimartindonesia.Utils;
import com.wakimart.wakimartindonesia.lookup.adapter.ProvinceLookupAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProvinceLookup extends AppCompatActivity {

    private List<Map<String, Object>> itemList = new ArrayList<>();
    private RecyclerView rvProvince;
    private ProvinceLookupAdapter provinceLookupAdapter;
    private FrameLayout loadingBar;
    private TextView tvBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_lookup);
        getSupportActionBar().hide();
        rvProvince = findViewById(R.id.rvProvince);
        tvBtnBack = findViewById(R.id.tvBtnBack);
        loadingBar = findViewById(R.id.loadingBar);
        tvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        provinceLookupAdapter = new ProvinceLookupAdapter(ProvinceLookup.this,itemList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProvince.setLayoutManager(linearLayoutManager);
        rvProvince.setAdapter(provinceLookupAdapter);
        fetchData();
    }

    private void fetchData() {
        loadingBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(Utils.API_URL + "fetchProvinces")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("data");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject data = results.getJSONObject(i);
                                itemList.add(Utils.toMap(data));
                            }
                            provinceLookupAdapter.notifyDataSetChanged();
                            loadingBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        loadingBar.setVisibility(View.GONE);
                        finish();
                    }
                });

    }


}
