package douglasdwyer.royaljson.rules;

public class PrimitiveCharSerializationRule extends CharSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return char.class;
    }
}