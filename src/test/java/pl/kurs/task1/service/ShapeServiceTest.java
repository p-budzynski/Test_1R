package pl.kurs.task1.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kurs.task1.datatype.Circle;
import pl.kurs.task1.datatype.Rectangle;
import pl.kurs.task1.datatype.Shape;
import pl.kurs.task1.datatype.Square;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ShapeServiceTest {
    private static final ShapeService shapeService = new ShapeService();
    private static List<Shape> shapes;
    private Path tempFileSerializer;
    private Path tempFileDeserializer;
    private Path tempFileWithUnknownType;
    private Path tempFileRectangle;

    @BeforeAll
    static void setUp() {
        shapes = Arrays.asList(
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
    }

    @BeforeEach
    void createTempFile() throws IOException {
        tempFileSerializer = Files.createTempFile("shapesSerializer", ".json");
        tempFileDeserializer = Files.createTempFile("shapesDeserializer", ".json");
        tempFileWithUnknownType = Files.createTempFile("shapesWithUnknownType", ".json");
        tempFileRectangle = Files.createTempFile("rectangle", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFileSerializer);
        Files.deleteIfExists(tempFileDeserializer);
        Files.deleteIfExists(tempFileWithUnknownType);
        Files.deleteIfExists(tempFileRectangle);
    }

    @Test
    void shouldReturnShapeWithLargestArea() {
        //when
        Shape largestAreaShape = shapeService.findShapeWithLargestArea(shapes);

        //then
        assertThat(largestAreaShape.calculateArea()).isEqualTo(900.0);
    }

    @ParameterizedTest
    @MethodSource("provideEmptyShapesCollections")
    void shouldThrowExceptionWhenPassedEmptyListOrNullOnMethodFindShapeWithLargestArea(List<Shape> emptyShapes) {
        //when then
        assertThatThrownBy(() -> shapeService.findShapeWithLargestArea(emptyShapes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Shapes list cannot be null or empty.");
    }

    @Test
    void shouldReturnShapeWithLargestPerimeterOfTypeCircle() {
        //when
        Shape largestPerimeterCircle = shapeService.findShapeWithLargestPerimeterOfType(shapes, Circle.class);

        //then
        assertThat(largestPerimeterCircle).isInstanceOf(Circle.class);
        assertThat(largestPerimeterCircle.calculatePerimeter()).isEqualTo(56.548667764616276);
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoCircleInTheListOnMethodFindShapeWithLargestPerimeterOfType() {
        //given
        List<Shape> shapesWithoutCircles = Arrays.asList(
                Rectangle.createRectangle(5, 5),
                Square.createSquare(5)
        );

        //when then
        assertThatThrownBy(() -> shapeService.findShapeWithLargestPerimeterOfType(shapesWithoutCircles, Circle.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No shapes of the specified type.");
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoRectangleInTheListOnMethodFindShapeWithLargestPerimeterOfType() {
        //given
        List<Shape> shapesWithoutCircles = Arrays.asList(
                Circle.createCircle(7),
                Square.createSquare(5)
        );

        //when then
        assertThatThrownBy(() -> shapeService.findShapeWithLargestPerimeterOfType(shapesWithoutCircles, Rectangle.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No shapes of the specified type.");
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoSquareInTheListOnMethodFindShapeWithLargestPerimeterOfType() {
        //given
        List<Shape> shapesWithoutCircles = Arrays.asList(
                Rectangle.createRectangle(5, 5),
                Circle.createCircle(3)
        );

        //when then
        assertThatThrownBy(() -> shapeService.findShapeWithLargestPerimeterOfType(shapesWithoutCircles, Square.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No shapes of the specified type.");
    }

    @ParameterizedTest
    @MethodSource("provideEmptyShapesCollections")
    void shouldThrowExceptionWhenPassedEmptyListOrNullOnMethodFindShapeWithLargestPerimeterOfType(List<Shape> emptyShapes) {
        //when then
        assertThatThrownBy(() -> shapeService.findShapeWithLargestPerimeterOfType(emptyShapes, Shape.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Shapes list cannot be null or empty.");
    }

    @Test
    void shouldExportShapesToJson() throws IOException {
        //given
        List<Shape> shapeList = Arrays.asList(
                Circle.createCircle(5),
                Square.createSquare(9),
                Rectangle.createRectangle(2,7)
        );

        //when
        shapeService.exportShapesToJson(shapeList, tempFileSerializer.toString());
        String actualJson = Files.readString(tempFileSerializer);
        String expectedJson = """
                [
                {"type":"circle","radius":"5.0"},
                {"type":"square","side":"9.0"},
                {"type":"rectangle","width":"2.0","height":"7.0"}
                ]
                """;

        //then
        assertThat(actualJson).isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    void shouldImportShapesFromJson() throws IOException {
        //given
        String expectedJson = """
                [
                {"type":"circle","radius":"5.0"},
                {"type":"square","side":"9.0"},
                {"type":"rectangle","width":"2.0","height":"7.0"}
                ]
                """;
        Files.writeString(tempFileDeserializer, expectedJson);

        //when
        List<Shape> shapesFromJson = shapeService.importShapesFromJson(tempFileDeserializer.toString());

        //then
        assertThat(shapesFromJson.size()).isEqualTo(3);

        assertThat(shapesFromJson.get(0)).isInstanceOf(Circle.class);
        assertThat(((Circle) shapesFromJson.get(0)).getRadius()).isEqualTo(5);

        assertThat(shapesFromJson.get(1)).isInstanceOf(Square.class);
        assertThat(((Square) shapesFromJson.get(1)).getSide()).isEqualTo(9);

        assertThat(shapesFromJson.get(2)).isInstanceOf(Rectangle.class);
        assertThat(((Rectangle) shapesFromJson.get(2)).getWidth()).isEqualTo(2);
        assertThat(((Rectangle) shapesFromJson.get(2)).getHeight()).isEqualTo(7);
    }

    @Test
    void shouldThrowsExceptionWhenJsonFileHasUnknownTypeForMethodImportShapesFromJson() throws IOException {
        //given
        String expectedJson = """
                [
                {"type":"triangle","height":"5.0"},
                {"type":"square","side":"9.0"},
                {"type":"rectangle","width":"2.0","height":"7.0"}
                ]
                """;
        Files.writeString(tempFileWithUnknownType, expectedJson);

        //when then
        assertThatThrownBy(() -> shapeService.importShapesFromJson(tempFileWithUnknownType.toString()))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Unknown shape type: triangle");
    }

    private static Stream<List<Shape>> provideEmptyShapesCollections() {
        return Stream.of(null, List.of());
    }

}
