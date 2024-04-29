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
    private TextView editMunicipalityName;
    Button btn;

    private List<String> arr;
    private AdapterItem adapterItem;
    private RecyclerView recyclerView;

    public FirstFragment() {
        // // Required empty public constructor
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
        // editMunicipalityName = view.findViewById(R.id.editMunicipalityName);

        MainActivity activity = (MainActivity) getActivity();
        String dataString= activity.getPopulationData();

        txtPopulation.setText(dataString);

       /* Button searchButton = view.findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonClick(v);
            }
        }); */


        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemsBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemsBars.left, systemsBars.top, systemsBars.right, systemsBars.bottom);

            return insets;


        });

        recyclerView = view.findViewById(R.id.recyclerView);
        arr = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            //arr.add(Integer.toString(i).concat(". Nick Steven"));

        }
        adapterItem = new AdapterItem(arr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItem);
        return view;

    }

}



