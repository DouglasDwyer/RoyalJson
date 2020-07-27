package douglasdwyer.royaljson.rules;

import java.util.ArrayList;

import javax.json.*;

import douglasdwyer.royaljson.*;

@SuppressWarnings({"rawtypes","unchecked"})
public class ArrayListSerializationRule extends RoyalSerializationRule {
	@Override
	public Class<?> getSupportedClass() {
		return ArrayList.class;
	}

	@Override
	public JsonValue serialize(RoyalJsonSerializer serializer, Object obj) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		ArrayList array = (ArrayList)obj;
		for(Object o : array) {
			JsonObjectBuilder oBuild = Json.createObjectBuilder();
			if(o == null) {
				oBuild.add("null", JsonObject.NULL);
			}
			else {
				oBuild.add(o.getClass().getName(), writeObject(serializer, o));
			}
			builder.add(oBuild.build());
		}
		return builder.build();
	}

	@Override
	public Object deserialize(RoyalJsonSerializer serializer, Class<?> objType, JsonValue obj) {
		try {
			ArrayList toReturn = (ArrayList)objType.getConstructor().newInstance();
			JsonArray arr = obj.asJsonArray();
			for(JsonValue value : arr) {
				JsonObject o = value.asJsonObject();
				String key = o.keySet().toArray(new String[0])[0];
				Class<?> clazz = Class.forName(key);
				toReturn.add(readObject(serializer, clazz, o.get(key)));
			}
			return toReturn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}			
	}
}