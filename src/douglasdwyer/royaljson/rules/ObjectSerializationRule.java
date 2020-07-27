package douglasdwyer.royaljson.rules;

import java.lang.reflect.*;

import javax.json.*;

import douglasdwyer.royaljson.*;

public class ObjectSerializationRule extends RoyalSerializationRule {

    @Override
    public Class<?> getSupportedClass() {
        return Object.class;
    }

    @Override
    public JsonObject serialize(RoyalJsonSerializer serializer, Object obj) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for(Field field : obj.getClass().getFields()) {
            int mods = field.getModifiers();
            if(Modifier.isPublic(mods) && !Modifier.isStatic(mods)) {
                Object toSerialize = null;
                try {
                    toSerialize = field.get(obj);
                }
                catch(IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if(toSerialize == null) {
                    builder.add(field.getName() + "@null", JsonObject.NULL);
                }
                else {
                    builder.add(field.getName() + "@" + toSerialize.getClass().getName(), writeObject(serializer, toSerialize));
                }
            }
        }
        return builder.build();
    }

    @Override
    public Object deserialize(RoyalJsonSerializer serializer, Class<?> objType, JsonValue obj) {
        try {
            Object toReturn = objType.getConstructor().newInstance();
            JsonObject jObject = obj.asJsonObject();
            for(String key : jObject.keySet()) {
                String[] parts = key.split("@", -1);
                Field field = objType.getField(parts[0]);
                if(!"null".equals(parts[1])) {
                    Object o = readObject(serializer, Class.forName(parts[1]), jObject.get(key));
                    field.set(toReturn, o);
                }
            }
            return toReturn;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}