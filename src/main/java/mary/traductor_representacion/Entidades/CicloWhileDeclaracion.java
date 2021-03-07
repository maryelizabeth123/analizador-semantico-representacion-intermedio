package mary.tradutor_representacion.Entidades;

public class WhileStatement extends Statement {

    private Expression condition;
    private Block body;

    public WhileStatement(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }

    @Override
    public void analyze(AnalysisContext context) {
        condition.analyze(context);
        condition.assertBoolean("while_condition_not_boolean", context);
        body.analyze(context.withInLoop(true));
    }

    @Override
    public Statement optimize() {
        condition = condition.optimize();
        body.optimize();
        if  (condition.isFalse()) {
            return null;
        }
        return this;
    }
}
