package com.revolut.test.backend.ricardofuzeto.configuration.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jooq.types.UInteger;

import java.io.IOException;

public class UIntegerTypeAdapter extends TypeAdapter<UInteger> {
    @Override
    public void write(JsonWriter jsonWriter, UInteger uInteger) throws IOException {
        jsonWriter.value(uInteger.intValue());
    }

    @Override
    public UInteger read(JsonReader jsonReader) throws IOException {
        UInteger value = UInteger.valueOf(0);
        if (jsonReader.hasNext()) {
            value = UInteger.valueOf(jsonReader.nextInt());
        }
        return value;
    }
}
