package pl.kurs.task1.datatype;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Rectangle implements Shape {
    private final double width;
    private final double height;

    private Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("The length of both sides must be greater than zero.");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + width);
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    public static Rectangle createRectangle(double width, double height) {
        return new Rectangle(width, height);
    }

}
