package com.example.recyclerview_in_fragment_project;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Button btn;
    private EditText editMunicipalityName;

    String dataString;
    String wetherString;

    WorkData workData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view);

        editMunicipalityName = findViewById(R.id.editMunicipalityName);



        Button searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonClick(v);
            }
        });




    }
    public void onSearchButtonClick (View view) {
        //System.out.println("This is testing");
        FirstFragment firstFragment = new FirstFragment();

        Context context = this;
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

                                workData = municipalityDataRetriever.getWorkPlaceAndEmploymentRate(context, editMunicipalityName.getText().toString());

                                WeatherData weatherData = weatherDataRetriever.getData(editMunicipalityName.getText().toString());

                                // When we want to update values we got from the API to the UI, we must do it inside runOnUiThread -method

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //String dataString = "";
                                        for (PopulationData data : municipalityDataArrayList) {
                                            dataString = dataString + data.getYear() + ": " + data.getPopulation() + "\n";
                                            System.out.println(data.getPopulation());

                                        }
                                        //txtPopulation.setText(dataString);
                                        //Log.d("This is testing", dataString);




                                        wetherString = weatherData.getName() + "\n" +
                                                "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                                                "Temperature: " + weatherData.getTemperature() + "\n" +
                                                "Wind speed: " + weatherData.getWindSpeed() + "\n";

                                        //txtWeather.setText(weatherDataAsString);

                                        //txtWorkStatistics.setText("Workplace self-sufficiency: " + workData.getWorkplaceSelfSufficiency().toString());
                                        //txtEmploymentRate.setText("Employment rate: " + workData.getEmploymentRate().toString());


                                    }
                                });

                            }

                        }
        );
    }

public String getPopulationData(){
        return dataString;
}
public String getWetherString(){
        return wetherString;
}
public WorkData getWorkData(){
        return workData;

}



}