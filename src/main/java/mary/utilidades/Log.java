package mary.utilidades;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Log {

    private ResourceBundle bundle;
    private PrintWriter writer;
    private int errorCount = 0;
    private boolean quiet = false;

    public Log(String resourceBundlePrefix, PrintWriter writer) {
        this.bundle = ResourceBundle.getBundle(resourceBundlePrefix);
        this.writer = writer;
    }

    public void clearErrors() {
        errorCount = 0;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public void message(String key, Object... arguments) {
        if (!quiet) {
            try {
                String message = bundle.getString(key);
                writer.println(MessageFormat.format(message, arguments));
            } catch (MissingResourceException e){
                writer.println(key);
            }
        }
    }

    public void error(String errorKey, Object... arguments) {
        errorCount++;
        message(errorKey, arguments);
    }

    public void exception(Throwable t) {
        error(t.getLocalizedMessage());
    }
}
