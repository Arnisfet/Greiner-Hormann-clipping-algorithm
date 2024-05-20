package com.arnisfet.algorithm;

import com.vividsolutions.jts.geom.Coordinate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vertex {
    Coordinate coordinate;
    boolean intersection;
    boolean entry_exit;

    public Vertex(Coordinate coordinate) {
        this.coordinate = coordinate;
        intersection = false;
        entry_exit = false;
    }
    public Vertex(Vertex other) {
        this.coordinate = other.coordinate;
        this.intersection = other.intersection;
        this.entry_exit = other.entry_exit;
    }
}
