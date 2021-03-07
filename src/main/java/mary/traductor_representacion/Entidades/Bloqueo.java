package mary.tradutor_representacion.Entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Block extends Entity {

    private List<Statement> statements;
    private SymbolTable table = null;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

  
    public List<Type> getTypes() {
        List<Type> result = new ArrayList<Type>();
        for (Statement s: statements) {
            if (s instanceof Declaration) {
                Declarable d = ((Declaration)s).getDeclarable();
                if (d instanceof Type) {
                    result.add((Type)d);
                }
            }
        }
        return result;
    }

    public List<Function> getFunctions() {
        List<Function> result = new ArrayList<Function>();
        for (Statement s: statements) {
            if (s instanceof Declaration) {
                Declarable d = ((Declaration)s).getDeclarable();
                if (d instanceof Function) {
                    result.add((Function)d);
                }
            }
        }
        return result;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void createTable(SymbolTable parent) {
        if (parent != null) {
           
        }
        table = new SymbolTable(parent);
    }

    @Override
    public void analyze(AnalysisContext context) {
        List<Type> types = getTypes();
        List<Function> functions = getFunctions();

        if (table == null) {
            table = new SymbolTable(context.getTable());
        }

        for (Type type: types) {
            table.insert(type, context.getLog());
        }

        for (Type type: types) {
            type.analyze(context.withTable(table));
        }

        for (Function function: functions) {
            function.analyzeSignature(context.withTable(table));
            table.insert(function, context.getLog());
        }

        for (Statement s: statements) {
            if (s instanceof Declaration) {
                Declarable d = ((Declaration)s).getDeclarable();
                if (d instanceof Variable) {
                    table.insert(d, context.getLog());
                }
                if (d instanceof Type) {
             
                    continue;
                }
            }
            s.analyze(context.withTable(table));
        }
    }

    public void optimize() {
        for (ListIterator<Statement> it = statements.listIterator(); it.hasNext();) {
            Statement original = it.next();
            Statement optimized = original.optimize();
            if (optimized == null) {
                it.remove();
            } else if (optimized != original) {
                it.set(optimized);
            }
        }
    }
}
