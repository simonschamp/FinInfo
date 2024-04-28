package com.example.recyclerview_in_fragment_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private  TextView editMunicipalityName;
    Button btn;

    private List<String> arr;
    private AdapterItem adapterItem;
    private RecyclerView recyclerView;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_view, container, false);

        txtPopulation = view.findViewById(R.id.txtPopulation);
        txtWeather = view.findViewById(R.id.txtWeather);
        txtWorkStatistics = view.findViewById(R.id.workStatistics);
        txtEmploymentRate = view.findViewById(R.id.employmentRate);
        editMunicipalityName = view.findViewById(R.id.editMunicipalityName);
        btn = view.findViewById(R.id.button);


        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.item), (v, insets) ->{
            Insets systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemsBars.left, systemsBars.top, systemsBars.right, systemsBars.bottom);

            return insets;
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        arr = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            //arr.add(Integer.toString(i).concat(". Nick Steven"));

        }
        adapterItem = new AdapterItem(arr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItem);

        return view;

    }


    public void onSearchButtonClick (View view) {


        FirstFragment context = this;
        MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
        WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();

        // Here we fetch the municipality data in a background service, so that we do not disturb the UI
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                MunicipalityDataRetriever.getMunicipalityCodesMap();
                                ArrayList<PopulationData> municipalityDataArrayList = municipalityDataRetriever.getPopulationData(context, editMunicipalityName.getText().toString());

                                if (municipalityDataArrayList == null) {
                                    return;
                                }

                                WorkData workData = municipalityDataRetriever.getWorkPlaceAndEmploymentRate(context, editMunicipalityName.getText().toString());

                                WeatherData weatherData = weatherDataRetriever.getData(editMunicipalityName.getText().toString());

                                // When we want to update values we got from the API to the UI, we must do it inside runOnUiThread -method
                                
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String dataString = "";
                                        for (PopulationData data : municipalityDataArrayList) {
                                            dataString = dataString + data.getYear() + ": " + data.getPopulation() + "\n";
                                        }
                                        txtPopulation.setText(dataString);


                                        String weatherDataAsString = weatherData.getName() + "\n" +
                                                "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                                                "Temperature: " + weatherData.getTemperature() + "\n" +
                                                "Wind speed: " + weatherData.getWindSpeed() + "\n";

                                        txtWeather.setText(weatherDataAsString);

                                        txtWorkStatistics.setText("Workplace self-sufficiency: " + workData.getWorkplaceSelfSufficiency().toString());
                                        txtEmploymentRate.setText("Employment rate: " + workData.getEmploymentRate().toString());
                                    }
                                });
                            }

                            private void runOnUiThread(Runnable runnable) {
                            }
                        }
        );
    }

}



