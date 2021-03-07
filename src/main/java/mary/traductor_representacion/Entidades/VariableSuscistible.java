package mary.tradutor_representacion.Entidades;

public class SubscriptedVariable extends VariableExpression {

    private VariableExpression sequence;
    private Expression index;

    public SubscriptedVariable(VariableExpression v, Expression e) {
        this.sequence = v;
        this.index = e;
    }

    public VariableExpression getSequence() {
        return sequence;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public void analyze(AnalysisContext context) {
        sequence.analyze(context);
        index.analyze(context);

        sequence.assertArrayOrString("[]", context);
        index.assertInteger("[]", context);
        type = (sequence.type.isString()) ? Type.CHAR
                : sequence.type.isArray() ? ArrayType.class.cast(sequence.type).getBaseType()
                : Type.ARBITRARY;
    }

    public boolean isWritable() {
        return sequence.type.isArray();
    }
}
