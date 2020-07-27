package douglasdwyer.royaljson;

import javax.json.*;

/** This class represents a rule to use during object serialization. Whenever an object of the specified type is encountered, and no more-specific rule exists, this rule is used for serialization/deserialization. **/
public abstract class RoyalSerializationRule {
	/** Retrieves the class or interface type that this object supports serializing. **/
    public abstract Class<?> getSupportedClass();
    /** Converts the given object to a JSON structure. **/
    public abstract JsonValue serialize(RoyalJsonSerializer serializer, Object obj);
    /** Converts the given JSON structure to an object. **/
    public abstract Object deserialize(RoyalJsonSerializer serializer, Class<?> objType, JsonValue obj);

    /** Converts an object to its JSON structure using the specified serializer. This method should be called recursively if it is necessary to serialize the subobjects inside the fields of the object being serialized. **/
    protected JsonValue writeObject(RoyalJsonSerializer serializer, Object obj) {
        return serializer.writeObject(obj);
    }

    /** Converts a JSON structure to its Java object using the specified serializer. This method should be called recursively if it is necessary to deserialize the subobjects inside the fields of the JSON being deserialized. **/
    protected Object readObject(RoyalJsonSerializer serializer, Class<?> objType, JsonValue obj) {
        return serializer.readObject(objType, obj);
    }
}
