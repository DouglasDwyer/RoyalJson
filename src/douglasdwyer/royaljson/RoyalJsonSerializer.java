package douglasdwyer.royaljson;

import java.io.*;
import java.util.*;

import javax.json.*;

import douglasdwyer.royaljson.rules.*;

/** Represents a customizable JSON serializer with builtin polymorphism and support for complex types. **/
public class RoyalJsonSerializer {
    
	/** This is the default list of rules to use when serializing objects to JSON. If a new serialization object is created without a specific ruleset, this one is used. **/
    public static ArrayList<RoyalSerializationRule> defaultRuleset = new ArrayList<RoyalSerializationRule>(Arrays.asList(
    	new ArrayListSerializationRule(),
        new BooleanSerializationRule(),
        new ByteSerializationRule(),
        new CharSerializationRule(),
        new DoubleSerializationRule(),
        new FloatSerializationRule(),
        new IntSerializationRule(),
        new LongSerializationRule(),
        new ObjectSerializationRule(),
        new PrimitiveBooleanSerializationRule(),
        new PrimitiveByteSerializationRule(),
        new PrimitiveCharSerializationRule(),
        new PrimitiveDoubleSerializationRule(),
        new PrimitiveFloatSerializationRule(),
        new PrimitiveIntSerializationRule(),
        new PrimitiveLongSerializationRule(),
        new PrimitiveShortSerializationRule(),
        new ShortSerializationRule(),
        new StringSerializationRule()
    ));

    /** The current ruleset to use, indexed by supported class/interface. **/
    protected final HashMap<Class<?>,RoyalSerializationRule> ruleset;

    /** Creates a new serializer instance with the default serializer ruleset. **/
    public RoyalJsonSerializer() {
        ruleset = new HashMap<Class<?>,RoyalSerializationRule>();
        for(RoyalSerializationRule rule : defaultRuleset) {
            ruleset.put(rule.getSupportedClass(), rule);
        }
    }
    
    /** Creates a new serializer instance with the specified set of rules to employ during serialization. **/
    public RoyalJsonSerializer(ArrayList<RoyalSerializationRule> rules) {
        ruleset = new HashMap<Class<?>,RoyalSerializationRule>();
        for(RoyalSerializationRule rule : rules) {
            ruleset.put(rule.getSupportedClass(), rule);
        }
    }

    /** Serializes an object, converting it to a JSON string. **/
    public String serialize(Object obj) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(obj.getClass().getName(), writeObject(obj));
        return builder.build().toString();
    }
    
    /** Deserializes a JSON string, converting it to an object. **/
    public Object deserialize(String json) {
        try {
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonObject baseObject = reader.readObject();
            Class<?> clazz = Class.forName(baseObject.keySet().toArray(new String[0])[0]);
            return getBestRuleForClass(clazz).deserialize(this, clazz, baseObject.values().toArray(new JsonValue[0])[0]);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /** Deserializes a JSON string, converting it to an object. Casts the object to the specified type. **/
    @SuppressWarnings("unchecked")
	public <T> T deserializeTo(String json) {
    	return (T)deserialize(json);
    }

    /** Generates the JSON representation of an object using recursion and the current ruleset. **/
    JsonValue writeObject(Object obj) {
        return getBestRuleForClass(obj.getClass()).serialize(this, obj);
    }

    /** Generates the Java representation of a JSON string using recursion and the current ruleset. **/
    Object readObject(Class<?> objectType, JsonValue obj) {
        return getBestRuleForClass(objectType).deserialize(this, objectType, obj);
    }

    /** Retrieves the serialization rule that should be used to serialize the specified class.
     * This method iterates through the type hierarchy of the class.
     * If there is an exact class-to-rule match, that rule is used.
     * Otherwise, the class's interfaces are iterated, and the interface rule highest in the inheritance hierarchy is chosen.
     * If no such interface exists, the object's superclasses are iterated, and the first superclass with a matching rule is selected. **/
    protected RoyalSerializationRule getBestRuleForClass(Class<?> objectClass) {
        RoyalSerializationRule bestClassRule = null;
        RoyalSerializationRule bestInterfaceRule = null;
        Class<?> inheritedClass = objectClass;
        while(bestClassRule == null && inheritedClass != null) {
            if(ruleset.containsKey(inheritedClass)) {
                bestClassRule = ruleset.get(inheritedClass);
            }
            inheritedClass = inheritedClass.getSuperclass();
        }
        if(bestClassRule.getClass() == objectClass) {
            return bestClassRule;
        }
        else {
            ArrayList<Class<?>> nextInterfaces = new ArrayList<Class<?>>(Arrays.asList(objectClass.getInterfaces()));
            while(bestInterfaceRule == null && nextInterfaces.size() > 0) {
                Class<?>[] currentInterfaces = nextInterfaces.toArray(new Class<?>[0]);
                nextInterfaces.clear();
                for(Class<?> i : currentInterfaces) {
                    if(ruleset.containsKey(i)) {
                        return ruleset.get(i);
                    }
                    else {
                        nextInterfaces.addAll(Arrays.asList(i.getInterfaces()));
                    }
                }
            }
            return bestClassRule;
        }
    }
}