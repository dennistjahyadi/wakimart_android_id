package com.wakimart.wakimartindonesia.lookup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wakimart.wakimartindonesia.R;
import com.wakimart.wakimartindonesia.Utils;
import com.wakimart.wakimartindonesia.lookup.adapter.CityLookupAdapter;
import com.wakimart.wakimartindonesia.lookup.adapter.ProvinceLookupAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CityLookup extends AppCompatActivity {

    private List<Map<String, Object>> itemList = new ArrayList<>();
    private RecyclerView rvCities;
    private CityLookupAdapter cityLookupAdapter;
    private Integer provinceId;
    private FrameLayout loadingBar;
    private TextView tvBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_lookup);
        getSupportActionBar().hide();
        provinceId = getIntent().getIntExtra("province_id",1);
        rvCities = findViewById(R.id.rvCities);
        loadingBar = findViewById(R.id.loadingBar);
        tvBtnBack = findViewById(R.id.tvBtnBack);
        tvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cityLookupAdapter = new CityLookupAdapter(CityLookup.this,itemList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCities.setLayoutManager(linearLayoutManager);
        rvCities.setAdapter(cityLookupAdapter);
        fetchData();
    }

    private void fetchData() {
        loadingBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(Utils.API_URL + "fetchCities")
                .addQueryParameter("province_id", provinceId + "")
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
                            cityLookupAdapter.notifyDataSetChanged();
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
