package mary.tradutor_representacion.Entidades;

public class RealLiteral extends Literal {

    private Double value;

    public RealLiteral(String lexeme) {
        super(lexeme);
    }

    public Double getValue() {
        return value;
    }

    @Override
    public void analyze(AnalysisContext context) {
        type = Type.REAL;
        try {
            value = Double.parseDouble(getLexeme());
        } catch (NumberFormatException e) {
            context.error("bad_real", getLexeme());
        }
    }

    static RealLiteral fromValue(double value) {
        RealLiteral result = new RealLiteral(Double.toString(value));
        result.value = value;
        return result;
    }
}
