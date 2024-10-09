package pl.kurs.task1.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.kurs.task1.datatype.Circle;
import pl.kurs.task1.datatype.Rectangle;
import pl.kurs.task1.datatype.Shape;
import pl.kurs.task1.datatype.Square;

import java.io.IOException;

public class ShapeSerializer extends StdSerializer<Shape> {
    public ShapeSerializer(Class<Shape> t) {
        super(t);
    }

    @Override
    public void serialize(Shape shape, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        if (shape instanceof Square square) {
            serializeSquare(square, jsonGenerator);
        } else if (shape instanceof Circle circle) {
            serializeCircle(circle, jsonGenerator);
        } else if (shape instanceof Rectangle rectangle) {
            serializeRectangle(rectangle, jsonGenerator);
        }

        jsonGenerator.writeEndObject();
    }

    private void serializeSquare(Square square, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStringField("type", "square");
        jsonGenerator.writeStringField("side", String.valueOf(square.getSide()));
    }

    private void serializeCircle(Circle circle, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStringField("type", "circle");
        jsonGenerator.writeStringField("radius", String.valueOf(circle.getRadius()));
    }

    private void serializeRectangle(Rectangle rectangle, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStringField("type", "rectangle");
        jsonGenerator.writeStringField("width", String.valueOf(rectangle.getWidth()));
        jsonGenerator.writeStringField("height", String.valueOf(rectangle.getHeight()));
    }

}
