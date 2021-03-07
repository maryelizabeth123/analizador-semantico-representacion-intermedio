package mary.tradutor_representacion.Entidades;

public class SimpleVariableReference extends VariableExpression {

    private String name;
    private Variable referent;

    public SimpleVariableReference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Variable getReferent() {
        return referent;
    }

    @Override
    public void analyze(AnalysisContext context) {
        referent = context.lookupVariable(name);
        type = referent.getType();
    }

    public boolean isWritable() {
       return true;
    }
}
