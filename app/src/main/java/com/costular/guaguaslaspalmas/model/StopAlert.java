package com.costular.guaguaslaspalmas.model;

/**
 * Created by diego on 26/05/15.
 */
public class StopAlert extends Stop{

    private int minutes;
    private int routeId;

    public StopAlert(int id, double longitude, double latitude, int order, String name, int code, int routeId, int minutes) {
        super(id, longitude, latitude, order, name, code, routeId);
        this.minutes = minutes;
        this.routeId = routeId;
    }

    public StopAlert(Stop stop, int routeId, int minutes) {
        super(stop.getId(), stop.getLongitude(), stop.getLatitude(), stop.getOrder(), stop.getName(), stop.getCode(), stop.getRoute());
        this.minutes = minutes;
        this.routeId = routeId;
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

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
