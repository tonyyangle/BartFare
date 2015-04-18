package com.example.tony.bartfare;

import java.util.ArrayList;

/**
 * Created by Tony on 15/3/18.
 */
 class Stations {
    private String fullNa;
    private String abbrNa;
    private String fullNa2;
    private String abbrNa2;

    private ArrayList<String> listA;
    private ArrayList<String> listB;

    public Stations () {

    }

    public Stations(String fullNa, String fullNa2) {
        this.fullNa = fullNa;
        this.fullNa2 = fullNa2;
    }
    public void setListA(ArrayList<String> arrayList) {
        this.listA = arrayList;
    }
    public ArrayList<String> getListA() {
        return listA;
    }
    public void setListB(ArrayList<String> arrayList) {
        this.listB = arrayList;
    }
    public ArrayList<String> getListB() {
        return listB;
    }

    public void setFullNa(String fullNa) {
        this.fullNa = fullNa;
    }
    public String getFullNa() {
        return fullNa;
    }
    public void setFullNa2(String fullNa2) {
        this.fullNa2 = fullNa2;
    }
    public String getFullNa2() {
        return fullNa2;
    }

    public String change(String string) {
        for (int i = 0; i < listA.size(); i++) {
            if (string.equals(listA.get(i))) {
                abbrNa = listB.get(i);
            }
        }
        return abbrNa;

    }



}
