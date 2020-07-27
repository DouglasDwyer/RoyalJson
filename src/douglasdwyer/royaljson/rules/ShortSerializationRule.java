package douglasdwyer.royaljson.rules;

import javax.json.*;

import douglasdwyer.royaljson.*;

public class ShortSerializationRule extends RoyalSerializationRule {

    @Override
    public Class<?> getSupportedClass() {
        return Short.class;
    }

    @Override
    public JsonValue serialize(RoyalJsonSerializer serializer, Object obj) {
        return Json.createObjectBuilder().add("x", obj.toString()).build().getValue("/x");
    }

    @Override
    public Object deserialize(RoyalJsonSerializer serializer, Class<?> objType, JsonValue obj) {
        return Short.valueOf(((JsonString)obj).getChars().toString());
    }
}