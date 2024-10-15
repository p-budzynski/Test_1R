package pl.kurs.task1.app;

import pl.kurs.task1.datatype.*;
import pl.kurs.task1.service.ShapeService;

import java.util.Arrays;
import java.util.List;

public class ShapeRunner {
    public static void main(String[] args) throws Exception {
        ShapeFactory shapeFactory = new ShapeFactory();
        ShapeService shapeService = new ShapeService();

        Square sq1 = shapeFactory.createSquare(10);
        Square sq2 = shapeFactory.createSquare(10);

        List<Shape> shapeList = List.of(
                sq1,
                sq2,
                shapeFactory.createSquare(20),
                shapeFactory.createCircle(60),
                shapeFactory.createRectangle(15, 22)
        );

        System.out.println(sq1 == sq2);

        shapeService.exportShapesToJson(shapeList, "C:\\JavaTest\\json_test.json");
        List<Shape> shapesFromJson = shapeService.importShapesFromJson("C:\\JavaTest\\json_test.json");

        System.out.println(shapesFromJson);

        System.out.println(shapeService.findShapeWithLargestArea(shapeList));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList, Square.class));

        List<Shape> shapeList2 = Arrays.asList(
                shapeFactory.createCircle(5),
                shapeFactory.createCircle(7),
                shapeFactory.createCircle(9),
                shapeFactory.createRectangle(5, 5),
                shapeFactory.createRectangle(10, 10),
                shapeFactory.createRectangle(20, 20),
                shapeFactory.createSquare(10),
                shapeFactory.createSquare(20),
                shapeFactory.createSquare(30)
        );

        System.out.println(shapeList2);
        System.out.println(shapeService.findShapeWithLargestArea(shapeList2));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Circle.class));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Rectangle.class));
        System.out.println(shapeService.findShapeWithLargestPerimeterOfType(shapeList2, Square.class));

    }
}
