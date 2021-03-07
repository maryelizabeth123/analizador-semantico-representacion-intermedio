package mary.traductor_representacion;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import edu.lmu.cs.xlg.carlos.entities.Entity.AnalysisContext;
import edu.lmu.cs.xlg.carlos.entities.Program;
import edu.lmu.cs.xlg.carlos.syntax.Parser;
import edu.lmu.cs.xlg.translators.CarlosToJavaScriptTranslator;
import edu.lmu.cs.xlg.util.Log;

public class Compiler {

    private Log log = new Log("Carlos", new PrintWriter(System.err, true));

    public static void main(String[] args) throws IOException {

        Compiler compiler = new Compiler();
        String option;
        String baseFileName;

        if (args.length == 1) {
            option = "-js";
            baseFileName = args[0];
        } else if (args.length == 2) {
            option = args[0];
            baseFileName = args[1];
        } else {
            compiler.log.message("usage");
            return;
        }

        Reader reader = new FileReader(baseFileName);
        try {
            if (option.equals("-syn")) {
                Program program = compiler.checkSyntax(reader);
                program.printSyntaxTree("", "", new PrintWriter(System.out, true));
            } else if (option.equals("-sem")) {
                Program program = compiler.checkSemantics(reader);
                program.printEntities(new PrintWriter(System.out, true));
            } else if (option.equals("-opt")) {
                Program program = compiler.produceOptimizedSemanticGraph(reader);
                program.printEntities(new PrintWriter(System.out, true));
            } else if (option.equals("-js")) {
                compiler.generateJavaScript(reader, new PrintWriter(new FileWriter(baseFileName + ".js")));
            } else {
                compiler.log.message("usage");
            }
        } catch (Exception e) {
            compiler.log.exception(e);
        }
    }

    public Program checkSyntax(Reader reader) throws IOException {
        log.clearErrors();
        Parser parser = new Parser(reader);
        try {
            log.message("checking_syntax");
            return parser.parse(reader, log);
        } finally {
            reader.close();
        }
    }

    public Program checkSemantics(Reader reader) throws IOException {
        Program program = checkSyntax(reader);
        if (log.getErrorCount() > 0) {
            return null;
        }
        return checkSemantics(program);
    }

 
    public Program checkSemantics(Program program) throws IOException {
        log.message("checking_semantics");
        program.analyze(AnalysisContext.makeGlobalContext(log));
        return program;
    }


    public Program produceOptimizedSemanticGraph(Reader reader) throws IOException {
        Program program = checkSemantics(reader);
        if (log.getErrorCount() > 0) {
            return null;
        }
        log.message("optimizing");
        program.optimize();
        return program;
    }

    public void generateJavaScript(Reader reader, PrintWriter writer) throws IOException {
        Program program = produceOptimizedSemanticGraph(reader);
        if (log.getErrorCount() > 0) {
            return;
        }
        log.message("writing");
        new CarlosToJavaScriptTranslator().translateProgram(program, writer);
        writer.close();
    }

    public int getErrorCount() {
        return log.getErrorCount();
    }

    public void setQuiet(boolean quiet) {
        log.setQuiet(quiet);
    }
}
