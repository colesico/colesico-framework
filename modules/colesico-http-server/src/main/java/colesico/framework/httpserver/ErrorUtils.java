package colesico.framework.httpserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class ErrorUtils {
    public static String getRootCauseMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        Throwable root = th;
        Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        while (root.getCause() != null && visited.add(root)) {
            root = root.getCause();
        }
        return root.getClass().getSimpleName() + ": " + root.getMessage();
    }

    public static String toStackTrace(Throwable cause) {
        if (cause == null) return "";
        StringWriter sw = new StringWriter(1024);
        final PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
