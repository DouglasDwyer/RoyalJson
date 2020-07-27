package douglasdwyer.royaljson.rules;

public class PrimitiveBooleanSerializationRule extends BooleanSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return boolean.class;
    }
}