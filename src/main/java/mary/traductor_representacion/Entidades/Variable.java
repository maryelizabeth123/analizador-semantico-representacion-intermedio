package mary.tradutor_representacion.Entidades;

public class Variable extends Declarable {

    private String typename;
    private Expression initializer;
    private Type type;

    public static final Variable ARBITRARY = new Variable("<unknown>", Type.ARBITRARY);

    public Variable(String name, String typename, Expression initializer) {
        super(name);
        this.typename = typename;
        this.initializer = initializer;
    }

    public Variable(String name, Type type) {
        super(name);
        this.typename = type.getName();
        this.initializer = null;
        this.type = type;
    }

    public Expression getInitializer() {
        return initializer;
    }

    public String getTypename() {
        return typename;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void analyze(AnalysisContext context) {
        type = context.lookupType(typename);

        if (initializer != null) {
            initializer.analyze(context);
            initializer.assertAssignableTo(type, "variable_initialization_type_mismatch", context);
        }
    }

    @Override
    public void optimize() {
        if (initializer != null) {
            initializer = initializer.optimize();
        }
    }
}
