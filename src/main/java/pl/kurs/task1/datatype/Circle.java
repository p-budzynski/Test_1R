package pl.kurs.task1.datatype;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Circle implements Shape {
    private final double radius;

    private Circle(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("The radius length must be greater than zero.");
        }
        this.radius = radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    public static Circle createCircle(double radius) {
        return new Circle(radius);
    }

}
