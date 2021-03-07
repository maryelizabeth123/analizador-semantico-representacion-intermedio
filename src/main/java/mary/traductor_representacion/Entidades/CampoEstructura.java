package mary.tradutor_representacion.Entidades;

public class StructField extends Entity {

    private String name;
    private String typename;
    private Type type;

    public static final StructField ARBITRARY = new StructField("<unknown>", Type.ARBITRARY.getName());
    static {ARBITRARY.type = Type.ARBITRARY;}

    public StructField(String name, String typename) {
        this.name = name;
        this.typename = typename;
    }

    public String getName() {
        return name;
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
    }
}
