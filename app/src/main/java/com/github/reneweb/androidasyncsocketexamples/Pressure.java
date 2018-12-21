package com.github.reneweb.androidasyncsocketexamples;

public class Pressure {
    private String pressure;
    private String date;

    public Pressure (String press,String date){
        this.pressure = press;
        this.date = date;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String press) {
        this.pressure = press;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
