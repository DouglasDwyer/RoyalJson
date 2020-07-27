package douglasdwyer.royaljson.rules;

public class PrimitiveIntSerializationRule extends IntSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return int.class;
    }
}