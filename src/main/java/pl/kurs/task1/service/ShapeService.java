package pl.kurs.task1.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.task1.datatype.Shape;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ShapeService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Shape findShapeWithLargestArea(List<Shape> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            throw new IllegalArgumentException("Shapes list cannot be null or empty.");
        }
        return shapes.stream()
                .max(Comparator.comparingDouble(Shape::calculateArea))
                .orElse(null);
    }

    public Shape findShapeWithLargestPerimeterOfType(List<Shape> shapes, Class<?> type) {
        if (shapes == null || shapes.isEmpty()) {
            throw new IllegalArgumentException("Shapes list cannot be null or empty.");
        }
        return shapes.stream()
                .filter(type::isInstance)
                .max(Comparator.comparingDouble(Shape::calculatePerimeter))
                .orElseThrow(() -> new IllegalArgumentException("No shapes of the specified type '" + type.getSimpleName() + "'."));
    }

    public void exportShapesToJson(List<Shape> shapes, String filePath) throws IOException {
        objectMapper.writerFor(new TypeReference<List<Shape>>() {}).writeValue(new File(filePath), shapes);
    }

    public List<Shape> importShapesFromJson(String filePath) throws IOException {
        return objectMapper.readerFor(new TypeReference<List<Shape>>() {}).readValue(new File(filePath));
    }

}
