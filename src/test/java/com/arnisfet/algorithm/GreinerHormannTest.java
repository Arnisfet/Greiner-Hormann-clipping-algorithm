package com.arnisfet.algorithm;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.Assert;
import org.junit.jupiter.api.Test;

public class GreinerHormannTest {
    @Test
    public void TestIntersection() throws ParseException {
        String firstWKT = "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))";
        String secondWKT = "POLYGON((5 5, 15 5, 15 15, 5 15, 5 5))";
        String resultWKT = "POLYGON((5 10, 10 10, 10 5, 5 5, 5 10))";

        WKTReader reader = new WKTReader();
        Polygon firstPolygon = (Polygon) reader.read(firstWKT);
        Polygon secondPolygon = (Polygon) reader.read(secondWKT);
        Polygon result = (Polygon) reader.read(resultWKT);

        Polygon intersection = (Polygon) GreinerHormannClipper.intersection(firstPolygon, secondPolygon);

        Assert.equals(result, intersection);
    }
}
