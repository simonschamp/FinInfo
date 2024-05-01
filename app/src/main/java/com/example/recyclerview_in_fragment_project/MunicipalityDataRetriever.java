package com.example.recyclerview_in_fragment_project;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MunicipalityDataRetriever {
    static ObjectMapper objectMapper = new ObjectMapper();

    static HashMap<String, String> municipalityNamesToCodesMap = null;

    /**
     * Get municipality codes, we need to do this only once
     *
     */
    public static HashMap<String, String> getMunicipalityCodesMap() {
        if (municipalityNamesToCodesMap == null) {
            JsonNode areas = readAreaDataFromTheAPIURL(objectMapper);
            municipalityNamesToCodesMap = createMunicipalityNamesToCodesMap(areas);
        }
        return municipalityNamesToCodesMap;
    }

    public WorkData getWorkPlaceAndEmploymentRate(Context context, String municipalityName) {
        String code = getMunicipalityCodesMap().get(municipalityName);

        WorkData workData = new WorkData();

        // Workplace self-sufficiency
        try {
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.workplaceselfsufficiencyquery));
            ((ObjectNode) jsonQuery.findValue("query").get(1).get("selection")).putArray("values").add(code);

            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px"));

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode workAndEmploymentData = objectMapper.readTree(response.toString());

                JsonNode value = workAndEmploymentData.get("value");

                workData.setWorkplaceSelfSufficiency(new BigDecimal(value.get(0).asText()).setScale(2));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Employment rate
        try {
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentratequery));
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);

            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px"));

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode workAndEmploymentData = objectMapper.readTree(response.toString());
                JsonNode value = workAndEmploymentData.get("value");

                workData.setEmploymentRate(new BigDecimal(value.get(0).asText()).setScale(2));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return workData;
    }

    public ArrayList<PopulationData> getPopulationData(Context context, String municipalityName) {
        String code = getMunicipalityCodesMap().get(municipalityName);

        try {
            JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.populationquery));
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);

            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px"));
            System.out.println("This"  + jsonQuery.toPrettyString());

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode municipalityData = objectMapper.readTree(response.toString());

                ArrayList<String> years = new ArrayList<>();
                JsonNode populations = null;

                for (JsonNode node : municipalityData.get("dimension").get("Vuosi")
                        .get("category").get("label")) {
                    years.add(node.asText());
                }

                populations = municipalityData.get("value");
                ArrayList<PopulationData> populationData = new ArrayList<>();
                for (int i = 0; i < populations.size(); i++) {
                    Integer population = populations.get(i).asInt();
                    populationData.add(new PopulationData(Integer.parseInt(years.get(i)), population));
                }

                return populationData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery, URL url)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);
        }
        return con;
    }


    /**
     * In this method, we find all the municipality names and codes from the Json and put them into a HashMap,
     * so that we can get search for the municipality code by providing the municipality name
     *
     * @param areas
     * @return HashMap where municipality name is mapped to municipality code
     */
    private static HashMap<String, String> createMunicipalityNamesToCodesMap(JsonNode areas) {
        JsonNode codes = null;
        JsonNode names = null;

        for (JsonNode node : areas.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                codes = node.findValue("values");
                names = node.findValue("valueTexts");
            }
        }

        HashMap<String, String> municipalityNamesToCodesMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            municipalityNamesToCodesMap.put(name, code);
        }

        return municipalityNamesToCodesMap;
    }


    /**
     * Here we read the all the JSON from the URL to a JsonNode
     * <p>
     * How to improve this: instead of fetching the same data all over again when restarting the app, we could store
     * the areas JSON to a file and read it from there. Then we would only need to fetch it once, if the file does
     * not yet exist.
     *
     * @param objectMapper
     * @return JsonNode with municipality data
     */
    private static JsonNode readAreaDataFromTheAPIURL(ObjectMapper objectMapper) {
        JsonNode areas = null;
        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return areas;
    }


}


