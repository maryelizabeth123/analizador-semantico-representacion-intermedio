package mary.tradutor_representacion.Entidades;

import java.util.List;

public class StringLiteral extends Literal {

    private List<Integer> values;

    public StringLiteral(String lexeme) {
        super(lexeme);
    }

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public void analyze(AnalysisContext context) {
        type = Type.STRING;
        values = CharLiteral.codepoints(getLexeme(), 1, getLexeme().length() - 1, context);
    }
}
