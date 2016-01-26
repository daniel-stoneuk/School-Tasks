package com.danielstone.subway;

public class Ingredient {

    String NAME;
    Integer PRICE;
    Integer INDEX;
    boolean MULTI;

    public Ingredient(String name, Integer price, Integer index, boolean MULTI) {
        this.NAME = name;
        this.PRICE = price;
        this.INDEX = index;
    }

    public Ingredient(String name, Integer price, boolean MULTI) {
        this.NAME = name;
        this.PRICE = price;
        this.MULTI = MULTI;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public String getNAME() {
        return NAME;
    }

    public Integer getINDEX() {
        return INDEX;
    }

    public boolean isMULTI() {
        return MULTI;
    }

    public void setINDEX(Integer INDEX) {
        this.INDEX = INDEX;
    }
}
