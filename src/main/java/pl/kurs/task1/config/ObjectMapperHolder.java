package pl.kurs.task1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import pl.kurs.task1.datatype.Shape;
import pl.kurs.task1.deserializer.ShapeDeserializer;
import pl.kurs.task1.serializer.ShapeSerializer;

@Getter
public enum ObjectMapperHolder {
    INSTANCE;

    private final ObjectMapper objectMapper;

    ObjectMapperHolder() {
        this.objectMapper = create();
    }

    private static ObjectMapper create() {
        ObjectMapper om = new ObjectMapper();

        ShapeSerializer serializer = new ShapeSerializer(Shape.class);
        ShapeDeserializer deserializer = new ShapeDeserializer(Shape.class);
        SimpleModule simpleModule = new SimpleModule("ShapeSerializerAndDeserializer");
        simpleModule.addSerializer(serializer);
        simpleModule.addDeserializer(Shape.class, deserializer);
        om.registerModule(simpleModule);
        return om;
    }

}
