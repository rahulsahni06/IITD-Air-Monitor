package com.sahni.rahul.iitdair.UI.Model;

public class InternalHeading {
    private String heading;
    private float coordX;
    private float coordY;

    public InternalHeading(String heading, float coordX, float coordY) {
        this.heading = heading;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public String getHeading() {
        return heading;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

}
