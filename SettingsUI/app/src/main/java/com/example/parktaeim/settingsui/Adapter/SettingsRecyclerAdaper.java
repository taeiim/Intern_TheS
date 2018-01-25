package com.example.parktaeim.settingsui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parktaeim.settingsui.Model.SettingsRecyclerItem;
import com.example.parktaeim.settingsui.R;

import java.util.ArrayList;

/**
 * Created by parktaeim on 2018. 1. 25..
 */

public class SettingsRecyclerAdaper extends RecyclerView.Adapter<SettingsRecyclerAdaper.ViewHolder>{
    private Context context;
    ArrayList<SettingsRecyclerItem> items;

    public SettingsRecyclerAdaper(Context context, ArrayList<SettingsRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public SettingsRecyclerAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings,parent,false);
        switch (viewType){

        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsRecyclerAdaper.ViewHolder holder, int position) {
        holder.titleTv.setText(items.get(position).getTitle());
        holder.connectTv.setText(items.get(position).getConnectText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv;
        private TextView connectTv;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.item_settings_titleTv);
            connectTv = (TextView) itemView.findViewById(R.id.item_settings_connectTv);
        }
    }

}
