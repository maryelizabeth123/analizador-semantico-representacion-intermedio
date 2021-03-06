package mary.tradutor_representacion.Entidades;

public class Declaration extends Statement {

    private Declarable declarable;

    public Declaration(Declarable declarable) {
        this.declarable = declarable;
    }

    public Declarable getDeclarable() {
        return declarable;
    }

    @Override
    public void analyze(AnalysisContext context) {
        declarable.analyze(context);
    }

    @Override
    public Statement optimize() {
        declarable.optimize();
        return this;
    }
}
