package com.costular.guaguaslaspalmas.model;

/**
 * Created by diego on 26/05/15.
 */
public class StopAlert extends Stop{

    private int minutes;
    private int routeId;
    private String number;

    public StopAlert(int id, double longitude, double latitude, int order, String name, int code,
                     int routeId, int minutes, String number) {
        super(id, longitude, latitude, order, name, code, routeId);
        this.minutes = minutes;
        this.routeId = routeId;
        this.number = number;
    }

    public StopAlert(Stop stop, int routeId, int minutes, String number) {
        super(stop.getId(), stop.getLongitude(), stop.getLatitude(), stop.getOrder(),
                stop.getName(), stop.getCode(), stop.getRoute());
        this.minutes = minutes;
        this.routeId = routeId;
        this.number = number;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getNumber() {
        return number;
    }
}
