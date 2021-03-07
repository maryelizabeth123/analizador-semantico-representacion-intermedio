package mary.tradutor_representacion.Entidades;

public class NullLiteral extends Literal {

    public static NullLiteral INSTANCE = new NullLiteral();


    private NullLiteral() {
        super("null");
    }

    @Override
    public void analyze(AnalysisContext context) {
        type = Type.NULL_TYPE;
    }
}
