package mary.tradutor_representacion.Entidades;

public class ReturnStatement extends Statement {

    private Expression returnExpression;

    public ReturnStatement(Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    @Override
    public void analyze(AnalysisContext context) {
        if (context.getFunction() == null) {
           
            context.error("return_outside_function");

        } else if (context.getFunction().getReturnType() == null) {
       
            if (returnExpression != null) {
                context.error("return_value_not_allowed");
            }

        } else if (returnExpression == null) {
  
            context.error("return_value_required");

        } else {
        
            returnExpression.analyze(context);
            returnExpression.assertAssignableTo(context.getFunction().getReturnType(),
                    "return statement_type_mismatch", context);
        }
    }

    @Override
    public Statement optimize() {
        if (returnExpression != null) {
            returnExpression = returnExpression.optimize();
        }
        return this;
    }
}
