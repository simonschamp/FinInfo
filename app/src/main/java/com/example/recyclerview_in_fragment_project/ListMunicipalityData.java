package com.example.recyclerview_in_fragment_project;

import java.util.ArrayList;

public class ListMunicipalityData {
    private static ListMunicipalityData listMunicipalityData = null;
    private final ArrayList<MunicipalityData> municipalityDatas = new ArrayList<>();
    private ListMunicipalityData() {

    }

    public static ListMunicipalityData getInstance() {
        if (listMunicipalityData == null) {
            listMunicipalityData = new ListMunicipalityData();
        }
        return listMunicipalityData;
    }

    public void addMunicipalityToList(MunicipalityData municipalityData) {
        municipalityDatas.add(municipalityData);
    }

    public ArrayList<MunicipalityData> getMunicipalities() {
        return municipalityDatas;
    }
}
