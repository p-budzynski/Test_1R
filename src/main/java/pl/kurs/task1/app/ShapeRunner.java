package pl.kurs.task1.app;

import pl.kurs.task1.datatype.*;
import pl.kurs.task1.service.ShapeService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ShapeRunner {
    public static void main(String[] args) throws IOException {
        ShapeFactory shapeFactory = new ShapeFactory();
        ShapeService shapeService = new ShapeService();

        Square sq1 = shapeFactory.createSquare(10);
        Square sq2 = shapeFactory.createSquare(10);

        List<Shape> shapeList = List.of(
                sq1,
                sq2,
                shapeFactory.createSquare(20),
                shapeFactory.createCircle(60),
                shapeFactory.createRetangle(15, 22)
        );

        System.out.println(sq1 == sq2);

        shapeService.exportShapesToJson(shapeList, "C:\\JavaTest\\json_test.json");
        List<Shape> shapesFromJson = shapeService.importShapesFromJson("C:\\JavaTest\\json_test.json");

        System.out.println(shapesFromJson);

        System.out.println(shapeService.findShapeWithLargestArea(shapeList));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList, Square.class));

        List<Shape> shapeList2 = Arrays.asList(
                Circle.createCircle(5),
                Circle.createCircle(7),
                Circle.createCircle(9),
                Rectangle.createRectangle(5, 5),
                Rectangle.createRectangle(10, 10),
                Rectangle.createRectangle(20, 20),
                Square.createSquare(10),
                Square.createSquare(20),
                Square.createSquare(30)
        );

        System.out.println(shapeList2);
        System.out.println(shapeService.findShapeWithLargestArea(shapeList2));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Circle.class));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Rectangle.class));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Square.class));

        System.out.println(Circle.createCircle(9).calculatePerimeter());

    }
}
