package douglasdwyer.royaljson.rules;

public class PrimitiveShortSerializationRule extends ShortSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return short.class;
    }
}