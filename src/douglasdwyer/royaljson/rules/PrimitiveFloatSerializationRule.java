package douglasdwyer.royaljson.rules;

public class PrimitiveFloatSerializationRule extends FloatSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return float.class;
    }
}