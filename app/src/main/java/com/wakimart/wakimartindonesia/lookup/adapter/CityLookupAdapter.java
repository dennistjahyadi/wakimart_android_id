package com.wakimart.wakimartindonesia.lookup.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakimart.wakimartindonesia.R;

import java.util.List;
import java.util.Map;

public class CityLookupAdapter extends RecyclerView.Adapter<CityLookupAdapter.MyViewHolder> {

    private List<Map<String,Object>> itemList;
    private Activity activity;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout constraintLayout;
        private TextView tvName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public CityLookupAdapter(Activity activity,List<Map<String,Object>> itemList){
        this.activity = activity;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CityLookupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_city, viewGroup, false);
        return new CityLookupAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityLookupAdapter.MyViewHolder viewHolder, int i) {
        final Map<String,Object> result = itemList.get(i);
        viewHolder.tvName.setText((String)result.get("name"));
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("id", (Integer)result.get("id"));
                data.putExtra("name", (String)result.get("name"));
                activity.setResult(Activity.RESULT_OK,data);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
