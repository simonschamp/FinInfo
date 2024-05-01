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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    
    //MunicipalityData municipalityData;

    private EditText editMunicipalityName;
    
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMunicipalityName = findViewById(R.id.editMunicipalityName);



        Button searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchButtonClick(view);
            }
        });




    }
    public void onSearchButtonClick (View view) {
        //System.out.println("This is testing");
        FirstFragment firstFragment = new FirstFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,
                firstFragment).commit();

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

                                String cityName = editMunicipalityName.getText().toString();
                                WorkData workData = municipalityDataRetriever.getWorkPlaceAndEmploymentRate(context, cityName);
                                WeatherData weatherData = weatherDataRetriever.getData(cityName);
                                MunicipalityData municipalityData;
                                municipalityData = new MunicipalityData(municipalityDataArrayList, weatherData, workData, cityName);
                                ListMunicipalityData.getInstance().addMunicipalityToList(municipalityData);

                                String wikipediaSummary = getWikipediaSummary(cityName);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println(municipalityData.cityName);
                                        firstFragment.setMunicipalityData(municipalityData);
                                        firstFragment.setCityInfo(wikipediaSummary);
                                        }
                                    });
                            }
        });
    }

    private String getWikipediaSummary(String cityName) {
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exintro&titles=" + URLEncoder.encode(cityName, "UTF-8") + "&format=json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject pages = jsonObject.getJSONObject("query").getJSONObject("pages");
            String pageId = pages.keys().next();  // Get the first key (page id)
            String extract = pages.getJSONObject(pageId).getString("extract");

            return getFirstTwoSentencesFromHtml(extract);
        } catch (Exception e) {
            e.printStackTrace();
            return "No information found.";
        }
    }

    private String getFirstTwoSentencesFromHtml(String html) {
        // Remove HTML tags
        String textOnly = html.replaceAll("\\<.*?\\>", "");
        // Replace multiple spaces with a single space
        textOnly = textOnly.replaceAll("\\s+", " ");

        // Find the first period followed by a space
        int endOfFirstSentence = textOnly.indexOf(". ");
        if (endOfFirstSentence != -1) {
            // Find the second period followed by a space, starting from the character after the first period
            int endOfSecondSentence = textOnly.indexOf(". ", endOfFirstSentence + 2);
            if (endOfSecondSentence != -1) {
                return textOnly.substring(0, endOfSecondSentence + 1).trim(); // Include the second period
            } else {
                return textOnly.substring(0, endOfFirstSentence + 1).trim(); // Only one complete sentence available
            }
        } else {
            return textOnly; // Return the whole text if no period is found
        }
    }

}