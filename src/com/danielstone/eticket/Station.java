package com.danielstone.eticket;

import javax.swing.text.Style;

/**
 * Created by user on 26/01/2016.
 */
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
}
