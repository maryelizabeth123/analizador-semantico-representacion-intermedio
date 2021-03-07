package mary.tradutor_representacion.Entidades;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mary.utilidades.Log;

public abstract class Entity {

    private static Map<Entity, Integer> all = new LinkedHashMap<Entity, Integer>();

    public Entity() {
        synchronized (all) {
            all.put(this, all.size());
        }
    }

    public Integer getId() {
        return all.get(this);
    }

    @Override
    public String toString() {
        return "#" + getId();
    }

    public final void printSyntaxTree(String indent, String prefix, PrintWriter out) {

        String classname = getClass().getName();
        String kind = classname.substring(classname.lastIndexOf('.') + 1);
        String line = indent + prefix + "(" + kind + ")";

        Map<String, Entity> children = new LinkedHashMap<String, Entity>();
        for (Map.Entry<String, Object> entry: attributes().entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            } else if (value instanceof Entity) {
                children.put(name, Entity.class.cast(value));
            } else if (value instanceof Iterable<?>) {
                try {
                    int index = 0;
                    for (Object child : (Iterable<?>) value) {
                        children.put(name + "[" + (index++) + "]", (Entity) child);
                    }
                } catch (ClassCastException cce) {
                    line += " " + name + "=" + value;
                }
            } else {
                line += " " + name + "=" + value;
            }
        }
        out.println(line);

        for (Map.Entry<String, Entity> child: children.entrySet()) {
            child.getValue().printSyntaxTree(indent + "  ", child.getKey() + ": ", out);
        }
    }

    public void traverse(Visitor v, Set<Entity> visited) {

        if (visited.contains(this)) {
            return;
        }
        visited.add(this);

        v.onEntry(this);
        for (Map.Entry<String, Object> entry: attributes().entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Entity) {
                Entity.class.cast(value).traverse(v, visited);
            } else if (value instanceof Iterable<?>) {
                for (Object child : (Iterable<?>) value) {
                    if (child instanceof Entity) {
                        Entity.class.cast(child).traverse(v, visited);
                    }
                }
            }
        }
        v.onExit(this);
    }

    public static interface Visitor {
        void onEntry(Entity e);
        void onExit(Entity e);
    }

    private void writeDetailLine(PrintWriter writer) {
        String classname = getClass().getName();
        String kind = classname.substring(classname.lastIndexOf('.') + 1);
        writer.print(this + "\t(" + kind + ")");

        for (Map.Entry<String, Object> entry: attributes().entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value.getClass().isArray()) {
                value = Arrays.asList((Object[]) value);
            }
            writer.print(" " + name + "=" + value);
        }
        writer.println();
    }

    public final void printEntities(final PrintWriter writer) {
        traverse(new Visitor() {
            public void onEntry(Entity e) {
                e.writeDetailLine(writer);
            }
            public void onExit(Entity e) {
                // Intentionally empty
            }
        }, new HashSet<Entity>());
    }

    private Map<String, Object> attributes() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Class<?> c = getClass(); c != Entity.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    try {
                        field.setAccessible(true);
                        result.put(field.getName(), field.get(this));
                    } catch (IllegalAccessException cannotHappen) {
                    }
                }
            }
        }
        return result;
    }

    public static class AnalysisContext {
        private Log log;
        private SymbolTable table;
        private Function function;
        private boolean inLoop;

        private AnalysisContext(Log log, SymbolTable table, Function function, boolean inLoop) {
            this.log = log;
            this.table = table;
            this.function = function;
            this.inLoop = inLoop;
        }

        public static AnalysisContext makeGlobalContext(Log log) {
            AnalysisContext context = new AnalysisContext(log, null, null, false);
            SymbolTable global = new SymbolTable(null);
            global.insert(Type.INT, context.getLog());
            global.insert(Type.REAL, context.getLog());
            global.insert(Type.BOOLEAN, context.getLog());
            global.insert(Type.CHAR, context.getLog());
            global.insert(Type.STRING, context.getLog());
            global.insert(Function.GET_STRING, context.getLog());
            global.insert(Function.SUBSTRING, context.getLog());
            global.insert(Function.SQRT, context.getLog());
            global.insert(Function.PI, context.getLog());
            global.insert(Function.SIN, context.getLog());
            global.insert(Function.COS, context.getLog());
            global.insert(Function.ATAN, context.getLog());
            global.insert(Function.LN, context.getLog());
            return context.withTable(global);
        }

        public AnalysisContext withTable(SymbolTable table) {
            return new AnalysisContext(this.log, table, this.function, this.inLoop);
        }

        public AnalysisContext withFunction(Function function) {
            return new AnalysisContext(this.log, this.table, function, this.inLoop);
        }

        public AnalysisContext withInLoop(boolean inLoop) {
            return new AnalysisContext(this.log, this.table, this.function, inLoop);
        }

        public Log getLog() {
            return log;
        }

        public SymbolTable getTable() {
            return table;
        }

        public Function getFunction() {
            return function;
        }

        public boolean isInLoop() {
            return inLoop;
        }

        public Type lookupType(String name) {
            return getTable().lookupType(name, getLog());
        }

        public Variable lookupVariable(String name) {
            return getTable().lookupVariable(name, getLog());
        }

        public Function lookupFunction(String name, List<Expression> args) {
            return getTable().lookupFunction(name, args, getLog());
        }

        public void error(String errorKey, Object... arguments) {
            log.error(errorKey, arguments);
        }
    }

    public abstract void analyze(AnalysisContext context);
}
