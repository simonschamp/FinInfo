package com.example.recyclerview_in_fragment_project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;


public class FirstFragment extends Fragment {

    private TextView txtPopulation;
    private TextView txtWeather;
    private TextView txtWorkStatistics;
    private TextView txtEmploymentRate;
    private TextView txtCityInfo;
    ImageView weatherIcon;
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
        weatherIcon = view.findViewById(R.id.weatherIcon);
        txtCityInfo = view.findViewById(R.id.txtCityInfo);

        // Data visualisation
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("2014", 5461440));
        data.add(new ValueDataEntry("2015", 5479461));
        data.add(new ValueDataEntry("2016", 5495219));
        data.add(new ValueDataEntry("2017", 5508140));
        data.add(new ValueDataEntry("2018", 5515461));
        data.add(new ValueDataEntry("2019", 5521537));
        data.add(new ValueDataEntry("2020", 5529468));
        data.add(new ValueDataEntry("2021", 5535992));
        data.add(new ValueDataEntry("2022", 5540745));
        data.add(new ValueDataEntry("2023", 5545475));
        data.add(new ValueDataEntry("2024", 5549886));
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Finland Population");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Year");
        cartesian.yAxis(0).title("Population");
        anyChartView.setChart(cartesian);

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
    @SuppressLint("SetTextI18n")
    public void setMunicipalityData(MunicipalityData municipalityData){
        txtPopulation.setVisibility(View.VISIBLE);
        int municipalityDataSize = municipalityData.getPopulationData().size();
        txtPopulation.setText("Year " +
                municipalityData.getPopulationData().get(municipalityDataSize - 1).getYear() +
                " " + municipalityData.cityName + " population: " +
                municipalityData.getPopulationData().get(municipalityDataSize - 1).getPopulation());

        WeatherData weatherData = municipalityData.getWeatherData();
        String weatherDataAsString = weatherData.getName() + "\n" +
                "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                "Temperature: " + weatherData.getTemperature() + "\n" +
                "Wind speed: " + weatherData.getWindSpeed() + "\n";
        txtWeather.setText(weatherDataAsString);
        if (Double.parseDouble(weatherData.getTemperature()) > 0) {
            weatherIcon.setImageResource(R.drawable.ic_warm_weather);
        } else {
            weatherIcon.setImageResource(R.drawable.ic_cold_weather);
        }

        WorkData workData = municipalityData.workData;
        txtWorkStatistics.setText("Work place self sufficiency:" + workData.getWorkplaceSelfSufficiency().toString());
        txtEmploymentRate.setText("Employment rate:" + workData.getEmploymentRate().toString());
    }

    public void setCityInfo(String info) {
        if(txtCityInfo != null) {
            txtCityInfo.setText(info);
        }
    }

}



