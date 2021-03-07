package mary.tradutor_representacion.Entidades;

public abstract class VariableExpression extends Expression {

    public abstract boolean isWritable();

    public void assertWritable(AnalysisContext context) {
        if (!isWritable()) {
            context.error("read_only_error");
        }
    }
}
