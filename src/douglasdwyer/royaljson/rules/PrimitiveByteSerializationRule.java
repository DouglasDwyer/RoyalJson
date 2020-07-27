package douglasdwyer.royaljson.rules;

public class PrimitiveByteSerializationRule extends ByteSerializationRule {
    @Override
    public Class<?> getSupportedClass() {
        return byte.class;
    }
}