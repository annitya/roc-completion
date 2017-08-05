package Completions.Entities;

import com.google.gson.*;
import java.lang.reflect.Type;

public class DefaultValueDeserializer implements JsonDeserializer<DefaultValue>
{
    @Override
    public DefaultValue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        Gson gson = new GsonBuilder().create();
        Object o = gson.fromJson(jsonElement, Object.class);

        return new DefaultValue(o);
    }
}
