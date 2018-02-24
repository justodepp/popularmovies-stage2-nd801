package com.deeper.popularmovies.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WorkaroundGson implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonArray() && !(type.getRawType() == List.class || type.getRawType() == LinkedList.class ) ) {
                    Log.d(getClass().getSimpleName(), "Errore " + jsonElement.getClass().getSimpleName() + " " + type.getRawType().getSimpleName() + " " + type.getType().getClass().getSimpleName() + " " + jsonElement.toString());
                    return null;
                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}