package mary.tradutor_representacion.Entidades;

public abstract class Literal extends Expression {

    private String lexeme;

    public static final Literal NULL = NullLiteral.INSTANCE;

    public Literal(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
}
