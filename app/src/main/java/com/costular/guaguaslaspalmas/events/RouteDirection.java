package com.costular.guaguaslaspalmas.events;

/**
 * Created by diego on 4/03/15.
 */
public class RouteDirection {
    public static final int IDA = 1;
    public static final int VUELTA = 2;

    private int direction;

    public RouteDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
