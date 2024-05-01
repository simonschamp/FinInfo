package com.example.recyclerview_in_fragment_project;

import java.util.ArrayList;

public class MunicipalityData {
    String cityName;
    ArrayList<PopulationData> populationData;
    WeatherData weatherData;
    WorkData workData;
    public MunicipalityData(ArrayList<PopulationData> populationData, WeatherData weatherData, WorkData workData, String cityName) {
        this.populationData = populationData;
        this.weatherData = weatherData;
        this.workData = workData;
        this.cityName = cityName;
    }

    public ArrayList<PopulationData> getPopulationData() {
        return populationData;
    }

    public void setPopulationData(ArrayList<PopulationData> populationData) {
        this.populationData = populationData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WorkData getWorkData() {
        return workData;
    }

    public void setWorkData(WorkData workData) {
        this.workData = workData;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
