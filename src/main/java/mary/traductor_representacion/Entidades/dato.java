package mary.tradutor_representacion.Entidades;

public class Case extends Entity {

    private Expression condition;
    private Block body;

    public Case(Expression condition, Block body) {
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
        condition.assertBoolean("if_condition_not_boolean", context);
        body.analyze(context);
    }

    public void optimize() {
        condition = condition.optimize();
        body.optimize();
    }
}
