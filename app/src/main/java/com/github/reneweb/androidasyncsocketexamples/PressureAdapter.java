package com.github.reneweb.androidasyncsocketexamples;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.ViewHolder> {
    private List<Pressure> pressures = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView pressure;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pressure = itemView.findViewById(R.id.pressure);
            date = itemView.findViewById(R.id.datePressure);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pressure_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Pressure press = pressures.get(i);
        viewHolder.pressure.setText(press.getPressure());
        viewHolder.date.setText(press.getDate());


    }

    @Override
    public int getItemCount() {
        return pressures.size();
    }

    public void setPressures(List<Pressure> pressures) {
        this.pressures = pressures;
        notifyDataSetChanged();
    }
}
