package mary.tradutor_representacion.Entidades;

public class DottedVariable extends VariableExpression {

    private VariableExpression struct;
    private String fieldName;
    private StructField field;

    public DottedVariable(VariableExpression struct, String fieldName) {
        this.struct = struct;
        this.fieldName = fieldName;
    }

    public StructField getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public VariableExpression getStruct() {
        return struct;
    }

    @Override
    public void analyze(AnalysisContext context) {
        struct.analyze(context);

        if (!(struct.type instanceof StructType)) {
            context.error("not_a_struct");
            type = Type.ARBITRARY;
        } else {
            field = ((StructType)struct.type).getField(fieldName, context);

            type = field.getType();
        }
    }

    public boolean isWritable() {
        return true;
    }
}
