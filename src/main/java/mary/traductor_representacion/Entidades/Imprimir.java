package mary.tradutor_representacion.Entidades;

import java.util.List;
import java.util.ListIterator;

public class PrintStatement extends Statement {

    private List<Expression> args;

    public PrintStatement(List<Expression> args) {
        this.args = args;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public void analyze(AnalysisContext context) {
        for (Expression arg : args) {
            arg.analyze(context);
        }
    }

    @Override
    public Statement optimize() {
        for (ListIterator<Expression> it = args.listIterator(); it.hasNext();) {
            it.set(it.next().optimize());
        }
        return this;
    }
}
