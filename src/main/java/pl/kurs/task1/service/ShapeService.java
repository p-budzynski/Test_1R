package pl.kurs.task1.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.task1.config.ObjectMapperHolder;
import pl.kurs.task1.datatype.Shape;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ShapeService {
    public Shape findShapeWithLargestArea(List<Shape> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            throw new IllegalArgumentException("Shapes list cannot be null or empty.");
        }
        return shapes.stream()
                .max(Comparator.comparingDouble(Shape::calculateArea))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find shape with largest area."));
    }

    public Shape findShapeWithLargestPerimeterOfType(List<Shape> shapes, Class<?> type) {
        if (shapes == null || shapes.isEmpty()) {
            throw new IllegalArgumentException("Shapes list cannot be null or empty.");
        }
        return shapes.stream()
                .filter(shape -> shape.getClass().equals(type))
                .max(Comparator.comparingDouble(Shape::calculatePerimeter))
                .orElseThrow(() -> new IllegalArgumentException("No shapes of the specified type."));
    }

    public void exportShapesToJson(List<Shape> shapes, String filePath) throws IOException {
        ObjectMapper objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        objectMapper.writeValue(new File(filePath), shapes);
    }

    public List<Shape> importShapesFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        return objectMapper.readValue(new File(filePath), new TypeReference<>() {
        });
    }

}
