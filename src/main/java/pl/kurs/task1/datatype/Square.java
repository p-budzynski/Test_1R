package pl.kurs.task1.datatype;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Square implements Shape {
    private final double side;

    private Square(double side) {
        if (side <= 0) {
            throw new IllegalArgumentException("The side length must be greater than zero.");
        }
        this.side = side;
    }

    @Override
    public double calculatePerimeter() {
        return 4 * side;
    }

    @Override
    public double calculateArea() {
        return Math.pow(side, 2);
    }

    public static Square createSquare(double side) {
        return new Square(side);
    }
}
