package mary.tradutor_representacion.Entidades;

public class BooleanLiteral extends Literal {

    public static final BooleanLiteral TRUE = new BooleanLiteral("true");
    public static final BooleanLiteral FALSE = new BooleanLiteral("false");

    private BooleanLiteral(String lexeme) {
        super(lexeme);
    }

    @Override
    public void analyze(AnalysisContext context) {
        this.type = Type.BOOLEAN;
    }

    static BooleanLiteral fromValue(boolean value) {
        return value ? TRUE : FALSE;
    }
}
