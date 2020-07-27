package douglasdwyer.royaljson.rules;

public class PrimitiveLongSerializationRule extends LongSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return long.class;
    }
}