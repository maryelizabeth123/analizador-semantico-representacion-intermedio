package mary.tradutor_representacion.Entidades;

public class IncrementStatement extends Statement {

    private VariableExpression target;
    private String op;

    public IncrementStatement(VariableExpression target, String op) {
        this.target = target;
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public VariableExpression getTarget() {
        return target;
    }

    @Override
    public void analyze(AnalysisContext context) {
        target.analyze(context);
        target.assertInteger(op, context);
    }

    @Override
    public Statement optimize() {
        target = VariableExpression.class.cast(target.optimize());
        return this;
    }
}
