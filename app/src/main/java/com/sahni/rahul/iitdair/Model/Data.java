package com.sahni.rahul.iitdair.Model;

public class Data {

    private float temperature;

    private float humidity;

    public Data(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Data() {
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
