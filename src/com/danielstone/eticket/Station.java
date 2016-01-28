package com.danielstone.eticket;

import javax.swing.text.Style;

public class Station {
    String NAME;
    int POSITION;

    public Station(String name, int position) {
        this.NAME = name;
        this.POSITION = position;
    }

    public String getNAME() {
        return NAME;
    }

    public int getPOSITION() {
        return POSITION;
    }
}
