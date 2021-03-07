package mary.tradutor_representacion.Entidades;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.lmu.cs.xlg.util.Log;

public class SymbolTable extends Entity {

    Map<String, Entity> map = new HashMap<String, Entity>();

    SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        this.map = new HashMap<String, Entity>();
        this.parent = parent;
    }


    public void insert(Declarable d, Log log) {
        Object oldValue = map.put(d.getName(), d);

        if (oldValue == null) {
            return;

        } else if (!(oldValue instanceof Function && d instanceof Function)) {
            log.error("redeclared_identifier", d.getName());

        } else {
            ((Function)d).setOverload((Function)oldValue);
        }
    }

    public Type lookupType(String name, Log log) {
        if (name.endsWith("[]")) {
            return lookupType(name.substring(0,name.length()-2), log).array();
        }

        Object value = map.get(name);
        if (value == null) {
            if (parent == null) {
                log.error("type_not_found", name);
                return Type.ARBITRARY;
            } else {
                return parent.lookupType(name, log);
            }
        } else if (value instanceof Type) {
            return (Type)value;
        } else {
            log.error("not_a_type", name);
            return Type.ARBITRARY;
        }
    }

    public Variable lookupVariable(String name, Log log) {
        Object value = map.get(name);
        if (value == null) {
            if (parent == null) {
                log.error("variable_not_found", name);
                return Variable.ARBITRARY;
            } else {
                return parent.lookupVariable(name, log);
            }
        } else if (value instanceof Variable) {
            return (Variable)value;
        } else {
            log.error("not_a_variable", name);
            return Variable.ARBITRARY;
        }
    }

    public Function lookupFunction(String name, List<Expression> args, Log log) {
        Object value = map.get(name);

        if (value == null) {
            if (parent == null) {
                log.error("function_not_found", name);
                return null;
            } else {
                return parent.lookupFunction(name, args, log);
            }

        } else if (value instanceof Function) {
            Function candidate = null;
            for (Function f = (Function)value; f != null; f = f.getOverload()) {
                if (f.canBeCalledWith(args)) {
                    if (candidate != null) {
                        log.error("multiple_callables", f.getName());
                        return null;
                    }
                    candidate = f;
                }
            }
            if (candidate != null) {
                return candidate;
            }

            log.error("non_matching_args", name, args.size() + "");
            return null;

        } else {
            log.error("not_a_function", name);
            return null;
        }
    }

    public Set<Object> getEntitiesByClass(Class<?> c) {
        Set<Object> result = new HashSet<Object>();
        for (Object value: map.values()) {
            if (c.isInstance(value)) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public void analyze(AnalysisContext context) {

    }
}
