# Greiner-Hormann clipper algorithm
This is Java implementation of Greiner-Hormann polygon clipping algorithm



## Main information
This project was written using JDK 1.8 and JTS Geom library

Operations:
- **intersect**

## How to use

Just look at the test I wrote. It is quite easy!

```
        String firstWKT = "POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))";
        String secondWKT = "POLYGON((5 5, 15 5, 15 15, 5 15, 5 5))";

        WKTReader reader = new WKTReader();
        Polygon firstPolygon = (Polygon) reader.read(firstWKT);
        Polygon secondPolygon = (Polygon) reader.read(secondWKT);

        Polygon intersection = (Polygon) GreinerHormannClipper.intersection(firstPolygon, secondPolygon);
```
## Known issues

1. As it should be known, Greiner Hormann clipper does not work with coincident edges.
   Therefore, there is a necessity to improve this code if you need best.
2. Be aware of order of points for your polygon. Algorithm would not get a correct result if you reverse an order one of the polygons.
3. It uses classic vector intersection algorithm of finding intersections, it takes every edge and finds intersections which is not pretty fast.
4. My realisation was made for finding only one intersection between two polygons. Do not try to get a list of intersections.
   I have got no time to improve it, but you can it is not so difficult to include.

