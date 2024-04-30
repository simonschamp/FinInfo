package com.example.recyclerview_in_fragment_project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirstFragment extends Fragment {

    private TextView txtPopulation;
    private TextView txtWeather;
    private TextView txtWorkStatistics;
    private TextView txtEmploymentRate;


    //private List<String> arr;
    private AdapterItem adapterItem;
    private RecyclerView recyclerView;

    public FirstFragment() {
        // // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_view, container, false);

        txtPopulation = view.findViewById(R.id.txtPopulation);
        txtWeather = view.findViewById(R.id.txtWeather);
        txtWorkStatistics = view.findViewById(R.id.workStatistics);
        txtEmploymentRate = view.findViewById(R.id.employmentRate);


        MainActivity activity = (MainActivity) getActivity();
       //String dataString= activity.getPopulationData();

        if(!ListMunicipalityData.getInstance().getMunicipalities().isEmpty()){
            txtPopulation.setText(ListMunicipalityData.getInstance().getMunicipalities().get(0).cityName);

        }

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemsBars.left, systemsBars.top, systemsBars.right, systemsBars.bottom);

            return insets;

        });

        return view;

    }
    public void setMunicipalityData(MunicipalityData municipalityData){
        txtPopulation.setVisibility(View.VISIBLE);
        //txtPopulation.setText( municipalityData.cityName);
        String dataString = "";
        for (PopulationData populationData: municipalityData.getPopulationData()) {
            dataString = dataString + populationData.getYear() + ": " + populationData.getPopulation() + "\n";
        }
        txtPopulation.setText(dataString);

        WeatherData weatherData = municipalityData.getWeatherData();
        String weatherDataAsString = weatherData.getName() + "\n" +
                "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                "Temperature: " + weatherData.getTemperature() + "\n" +
                "Wind speed: " + weatherData.getWindSpeed() + "\n";

        txtWeather.setText(weatherDataAsString);

        WorkData workData = municipalityData.workData;

        txtWorkStatistics.setText("Work place self sufficiency:" + workData.getWorkplaceSelfSufficiency().toString());
        txtEmploymentRate.setText("Employment rate:" + workData.getEmploymentRate().toString());

    }

}



