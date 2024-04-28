package com.example.recyclerview_in_fragment_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {

    List<String> arr;
    ArrayList<PopulationData> populationData;

    public AdapterItem(List<String> arr) {
        this.arr = arr;
    }
    public AdapterItem(ArrayList<PopulationData> populationData){
        this.populationData = populationData;};

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder
                (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.txt.setText(arr.get(position));
        holder.population.setText(populationData.get(position).toString());


    }

    @Override
    public int getItemCount() {
        //return arr.size();
        return populationData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView txt;
        TextView population;
        TextView weather;
        TextView workStatistics;
        TextView employmentRate;
        TextView municipalityName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.textView);
            population = itemView.findViewById(R.id.txtPopulation);
            weather = itemView.findViewById(R.id.txtWeather);
            workStatistics = itemView.findViewById(R.id.workStatistics);
            employmentRate = itemView.findViewById(R.id.employmentRate);
            municipalityName = itemView.findViewById(R.id.editMunicipalityName);

        }
    }
}
