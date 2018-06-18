package com.example.thyag.poiwifidata.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thyag.poiwifidata.R;
import com.example.thyag.poiwifidata.model.Thing;
import com.example.thyag.poiwifidata.model.ThingProbability;

import java.util.List;

public class AdapterThingProbability extends BaseAdapter {

    private final List<ThingProbability> thingProbabilityList;
    private final Activity activity;

    public AdapterThingProbability(List<ThingProbability> thingList, Activity actvity) {
        this.thingProbabilityList = thingList;
        this.activity = actvity;
    }

    @Override
    public int getCount() {
        return thingProbabilityList.size();
    }

    @Override
    public Object getItem(int position) {
        return thingProbabilityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.list_row, parent, false);
        ThingProbability thingProbability = thingProbabilityList.get(position);

        TextView tvThing = view.findViewById(R.id.tvThing);
        TextView tvLocality = view.findViewById(R.id.tvLocality);
        TextView tvProbability = view.findViewById(R.id.tvProbability);

        tvThing.setText(thingProbability.getThing().getNome().getTexto());
        tvLocality.setText(thingProbability.getThing().getLocalizacao().getTexto());
        tvProbability.setText(String.format("%.2f", thingProbability.getProbability() * 100) + "%");

        return view;
    }

    public void clear() {
        thingProbabilityList.clear();
    }

    public void addAll(List<ThingProbability> thingProbabilityList) {
        this.thingProbabilityList.addAll(thingProbabilityList);
    }
}