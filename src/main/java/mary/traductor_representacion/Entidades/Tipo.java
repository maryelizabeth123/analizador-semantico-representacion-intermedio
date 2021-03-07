package mary.tradutor_representacion.Entidades;

public class Type extends Declarable {

    public static final Type INT = new Type("int");
    public static final Type REAL = new Type("real");
    public static final Type BOOLEAN = new Type("boolean");
    public static final Type CHAR = new Type("char");
    public static final Type STRING = new Type("string");

    public static final Type ARBITRARY = new Type("<arbitrary>");

    public static final Type NULL_TYPE = new Type("<type_of_null>");

    public static final Type ARRAY_OR_STRING = new Type("<array_or_string>");

    private ArrayType arrayOfThisType = null;

    Type(String name) {
        super(name);
    }

    public boolean isReference() {
        return this == STRING
            || this instanceof ArrayType
            || this instanceof StructType
            || this == ARRAY_OR_STRING
            || this == ARBITRARY;
    }

    public boolean isArithmetic() {
        return this == INT || this == REAL;
    }

    public boolean isString() {
        return this == STRING;
    }

    public boolean isArray() {
        return this instanceof ArrayType;
    }

    public Type array() {
        if (arrayOfThisType == null) {
            arrayOfThisType = new ArrayType(this);
        }
        return arrayOfThisType;
    }

    @Override
    public void analyze(AnalysisContext context) {
    }
}
