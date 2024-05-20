package com.arnisfet.algorithm;


import com.arnisfet.collection.LinkedNode;
import com.vividsolutions.jts.geom.*;
import java.util.LinkedList;

import static java.lang.Math.abs;

/**
 * Clipping polygon algorithm. Takes two polygons and makes an intersection.
 */
public class GreinerHormannClipper {
    public static LinkedNode<Vertex> creatingNodes(LineString src) {
        LinkedNode<Vertex> result = null;
        for (int i = 0; i < src.getNumPoints() - 1; i++) {
            if (result == null) {
                result = new LinkedNode<>(new Vertex(src.getPointN(i).getCoordinate()));
                result.setFirst(result);
                result.setPrev(result);
                result.setNext(result);
            } else {
                LinkedNode<Vertex> insertion = new LinkedNode<>(new Vertex(src.getPointN(i).getCoordinate()));
                result.add(insertion);
            }
        }
        return result;
    }

    /**
     * Function checker for intersection
     * @param q1 - First coordinate;
     * @param q2 - Second coordinate;
     * @param p1 - Third coordinate;
     * @param p2 - Fourth coordinate;
     * @return
     */
    public static boolean intersectOrNot (Coordinate q1, Coordinate q2, Coordinate p1, Coordinate p2) {
        double minq = Math.min(q1.x, q2.x);
        double maxq = Math.max(q1.x, q2.x);
        double minp = Math.min(p1.x, p2.x);
        double maxp = Math.max(p1.x, p2.x);

        if( minp > maxq )
            return false;
        if( maxp < minq )
            return false;

        minq = Math.min(q1.y, q2.y);
        maxq = Math.max(q1.y, q2.y);
        minp = Math.min(p1.y, p2.y);
        maxp = Math.max(p1.y, p2.y);

        if( minp > maxq )
            return false;
        if( maxp < minq )
            return false;
        return true;
    }

    /**
     * Main algorithm of searching intersections. There are the whole phases.
     * Takes a pair of coordinates and finds an intersection between them.
     * @param polygon1
     * @param polygon2
     * @return New intersected polygon
     */
    public static Geometry intersection(Polygon polygon1, Polygon polygon2) {
        if (polygon2.contains(polygon1))
            return polygon1;
        LinkedNode<Vertex> source = creatingNodes(polygon1.getExteriorRing());
        LinkedNode<Vertex> clip = creatingNodes(polygon2.getExteriorRing());

        do {
            Coordinate p1 = source.getData().getCoordinate();
            Coordinate p2 = source.getNext().getData().getCoordinate();
            do {
              if (clip.getData().isIntersection() || clip.getNext().getData().isIntersection()) {
                  clip = clip.getNext();
                  continue;
              }
              Coordinate p3 = clip.getData().getCoordinate();
              Coordinate p4 = clip.getNext().getData().getCoordinate();
              if (intersectOrNot(p1, p2, p3, p4)) {
                  Vertex newVertex = vertex(p1, p2, p3, p4);
                  if (newVertex != null){
                      addNewVertex(source, clip, newVertex);
                      source = source.getNext();
                      clip = clip.getNext();
                  }
              }
                clip = clip.getNext();
            } while (clip != clip.getFirst());
            source = source.getNext();
        } while (source != source.getFirst());
        evenOddRule(source);
        return buildPolygone(source);
    }

    /**
     * Creates 2 Vertexes for each list and pushes them with a defined neighbour
     * @param source
     * @param clip
     * @param insertion
     */
    private static void addNewVertex(LinkedNode<Vertex> source, LinkedNode<Vertex> clip, Vertex insertion) {
        Vertex copyInsertion = new Vertex(insertion);
        LinkedNode<Vertex> newNodeSource = new LinkedNode<>(insertion);
        LinkedNode<Vertex> newNodeClip = new LinkedNode<>(copyInsertion);
        newNodeSource.setNeighbour(newNodeClip);
        newNodeClip.setNeighbour(newNodeSource);
        source.push(source, newNodeSource);
        clip.push(clip, newNodeClip);
    }

    /** Second phase of the algorithm.
     * Takes each intersection and set an entry_exit flag. If it is an even intersection, then it is entry,
     * if it is an odd, then it is exit.
     * @param source
     */
    private static void evenOddRule(LinkedNode<Vertex> source) {
        Integer num = 1;
        do {
            if (source.getData().isIntersection() ) {
                if (num % 2 != 0) {
                    source.getData().setEntry_exit(true);
                    source.getNeighbour().getData().setEntry_exit(true);
                    num++;
                }
                 else {
                    source.getData().setEntry_exit(false);
                    source.getNeighbour().getData().setEntry_exit(false);
                    num++;
                }
            }
            source = source.getNext();
        } while (source != source.getFirst());
    }

    /** Determinant of two parallelograms on the coordinate system.
     * @param a
     * @param b
     * @param c
     * @param d
     * @return An area of the shape (determinant);
     */
    private static double det(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    /**
     * Third phase of the algorithm. Creates a new polygon using intersections in two lists.
     * @param source
     * @return
     */
    private static Polygon buildPolygone(LinkedNode<Vertex> source) {
        LinkedList<Coordinate> coordsArray = new LinkedList<>();
        LinkedNode<Vertex> pointer = pointer = findIntersection(source);
        while (isAnyIntersection(source)) {
            if (pointer.getData().isEntry_exit()) {
                do {
                    coordsArray.add(pointer.getData().getCoordinate());
                    pointer = pointer.getNext();
                } while (!pointer.getData().isIntersection());
            }
            else {
                pointer.getData().setIntersection(false);
                while (!pointer.getData().isIntersection()) {
                    coordsArray.add(pointer.getData().getCoordinate());
                    pointer = pointer.getPrev();
                }
            }
                pointer.getData().setIntersection(false);
                pointer = pointer.getNeighbour();
                pointer.getData().setIntersection(false);

        }
        coordsArray.add(coordsArray.getFirst());
        GeometryFactory factory = new GeometryFactory();
        Coordinate[] coordinatesArray = coordsArray.toArray(new Coordinate[0]);
        return factory.createPolygon(coordinatesArray);
    }

    /**
     * Find first intersection in the list
     * @param source
     * @return
     */
    public static LinkedNode<Vertex> findIntersection(LinkedNode<Vertex> source) {
        LinkedNode<Vertex> pointer = source;
        do {
            if (pointer.getData().isIntersection())
                return pointer;
            pointer = pointer.getNext();
        } while (pointer != source.getFirst());
        return null;
    }

    /**
     * Looking for first intersection in the list
     * @param source
     * @return
     */
    public static boolean isAnyIntersection(LinkedNode<Vertex> source) {
        LinkedNode<Vertex> pointer = source;
        do {
            if (pointer.getData().isIntersection())
                return true;
            pointer = pointer.getNext();
        } while (pointer != source.getFirst());
        return false;
    }

    /** First phase of the algorithm. Searching for intersections
     * Calculating determinants and check the intersection
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @return
     */
    public static Vertex vertex(Coordinate p1, Coordinate p2, Coordinate p3, Coordinate p4) {
        double x1 = p1.x, y1 = p1.y;
        double x2 = p2.x, y2 = p2.y;
        double x3 = p3.x, y3 = p3.y;
        double x4 = p4.x, y4 = p4.y;

        double denominator = det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
        double det1And2 = det(x1, y1, x2, y2);
        double det3And4 = det(x3, y3, x4, y4);

        if (denominator == 0) {
            return null; // Parallel lines
        }

        double x = det(det1And2, x1 - x2, det3And4, x3 - x4) / denominator;
        double y = det(det1And2, y1 - y2, det3And4, y3 - y4) / denominator;

        Vertex vertex = new Vertex(new Coordinate(x, y));
        vertex.setIntersection(true);
        return vertex;
    }
}
