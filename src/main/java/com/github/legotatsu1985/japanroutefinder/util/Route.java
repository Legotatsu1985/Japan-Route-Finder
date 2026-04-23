package com.github.legotatsu1985.japanroutefinder.util;

import org.jetbrains.annotations.NotNull;

public class Route {
    private String origin;
    private String destination;
    private String timeRestriction;
    private String remark;
    private String route;

    public Route(@NotNull String origin, @NotNull String destination, @NotNull String route) {
        this.origin = origin;
        this.destination = destination;
        this.route = route;
    }

    public Route(@NotNull String origin, @NotNull String destination, @NotNull String route, String timeRestriction, String remark) {
        this(origin, destination, route);
        this.timeRestriction = timeRestriction;
        this.remark = remark;
    }

    public void setTimeRestriction(String timeRestriction) {this.timeRestriction = timeRestriction;}
    public void setRemark(String remark) {this.remark = remark;}

    public String getOrigin() {return origin;}
    public String getDestination() {return destination;}
    public String getTimeRestriction() {return timeRestriction;}
    public String getRemark() {return remark;}
    public String getRoute() {return route;}
}
