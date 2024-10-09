package pl.kurs.task1.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pl.kurs.task1.datatype.Shape;
import pl.kurs.task1.datatype.ShapeFactory;

import java.io.IOException;

public class ShapeDeserializer extends StdDeserializer<Shape> {
    private final ShapeFactory shapeFactory = new ShapeFactory();

    public ShapeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Shape deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String type = node.get("type").asText();

        switch (type) {
            case "square":
                double side = node.get("side").asDouble();
                return shapeFactory.createSquare(side);

            case "circle":
                double radius = node.get("radius").asDouble();
                return shapeFactory.createCircle(radius);

            case "rectangle":
                double width = node.get("width").asDouble();
                double height = node.get("height").asDouble();
                return shapeFactory.createRetangle(width, height);

            default:
                throw new JsonMappingException("Unknown shape type: " + type);
        }
    }
}
