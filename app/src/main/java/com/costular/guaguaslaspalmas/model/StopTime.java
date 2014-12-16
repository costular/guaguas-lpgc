package com.costular.guaguaslaspalmas.model;

/**
 * Created by Diego on 27/11/2014.
 */
public class StopTime {

    private String name;
    private String number;
    private String minutes;
    private String color;

    public StopTime(String name, String number, String minutes, String color) {
        this.name = name;
        this.number = number;
        this.minutes = minutes;
        this.color = color;
    }

    public StopTime() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

