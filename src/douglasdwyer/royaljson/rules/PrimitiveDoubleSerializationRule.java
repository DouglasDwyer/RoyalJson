package douglasdwyer.royaljson.rules;

public class PrimitiveDoubleSerializationRule extends DoubleSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return double.class;
    }
}