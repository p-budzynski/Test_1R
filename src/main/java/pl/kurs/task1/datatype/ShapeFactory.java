package pl.kurs.task1.datatype;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShapeFactory {

    private static final Map<String, Shape> cache = new ConcurrentHashMap<>();

    public Square createSquare(double side) {
        String key = "Square:" + side;
        return (Square) cache.computeIfAbsent(key, k -> Square.createSquare(side));
    }

    public Circle createCircle(double radius) {
        String key = "Circle:" + radius;
        return (Circle) cache.computeIfAbsent(key, k -> Circle.createCircle(radius));
    }

    public Rectangle createRectangle(double width, double height) {
        String key = "Rectangle:" + width + ":" + height;
        return (Rectangle) cache.computeIfAbsent(key, k -> Rectangle.createRectangle(width, height));
    }
}
