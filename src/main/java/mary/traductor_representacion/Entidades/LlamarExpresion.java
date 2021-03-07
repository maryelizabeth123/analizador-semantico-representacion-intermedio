package mary.tradutor_representacion.Entidades;

import java.util.Lista;
import java.util.ListaIterador;

public class CallExpression extends VariableExpression {

    private String functionName;
    private List<Expression> args;
    private Function function;

    public CallExpression(String functionName, List<Expression> args) {
        this.functionName = functionName;
        this.args = args;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Function getFunction() {
        return function;
    }

    public String getFunctionName() {
        return functionName;
    }

    public boolean isWritable() {
        return false;
    }

    @Override
    public void analyze(AnalysisContext context) {

        for (Expression a: args) {
            a.analyze(context);
        }

        function = context.lookupFunction(functionName, args);

        if (function == null) {
     
            type = Type.ARBITRARY;
            return;
        }

        if (function.getReturnType() == null) {
            context.error("void_function_in_expression", functionName);
            type = Type.ARBITRARY;
        } else {
            type = function.getReturnType();
        }
    }

    @Override
    public Expression optimize() {
        for (ListIterator<Expression> it = args.listIterator(); it.hasNext();) {
            it.set(it.next().optimize());
        }
        return this;
    }
}
