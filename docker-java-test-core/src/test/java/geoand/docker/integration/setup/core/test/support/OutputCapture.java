package geoand.docker.integration.setup.core.test.support;

import org.hamcrest.Matcher;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

/**
 * Created by gandrianakis on 1/12/2015.
 *
 * This class is copied from Spring Boot's test facilities
 * and is used to capture logging output
 */
public class OutputCapture implements TestRule {

    private CaptureOutputStream captureOut;

    private CaptureOutputStream captureErr;

    private ByteArrayOutputStream copy;

    private List<Matcher<? super String>> matchers = new ArrayList<Matcher<? super String>>();

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                captureOutput();
                try {
                    base.evaluate();
                }
                finally {
                    try {
                        if (!OutputCapture.this.matchers.isEmpty()) {
                            String output = OutputCapture.this.toString();
                            assertThat(output, allOf(OutputCapture.this.matchers));
                        }
                    }
                    finally {
                        releaseOutput();
                    }
                }
            }
        };
    }

    protected void captureOutput() {
        AnsiOutputControl.get().disableAnsiOutput();
        this.copy = new ByteArrayOutputStream();
        this.captureOut = new CaptureOutputStream(System.out, this.copy);
        this.captureErr = new CaptureOutputStream(System.err, this.copy);
        System.setOut(new PrintStream(this.captureOut));
        System.setErr(new PrintStream(this.captureErr));
    }

    protected void releaseOutput() {
        AnsiOutputControl.get().enabledAnsiOutput();
        System.setOut(this.captureOut.getOriginal());
        System.setErr(this.captureErr.getOriginal());
        this.copy = null;
    }

    public void flush() {
        try {
            this.captureOut.flush();
            this.captureErr.flush();
        }
        catch (IOException ex) {
            // ignore
        }
    }

    @Override
    public String toString() {
        flush();
        return this.copy.toString();
    }

    private static class CaptureOutputStream extends OutputStream {

        private final PrintStream original;

        private final OutputStream copy;

        CaptureOutputStream(PrintStream original, OutputStream copy) {
            this.original = original;
            this.copy = copy;
        }

        @Override
        public void write(int b) throws IOException {
            this.copy.write(b);
            this.original.write(b);
            this.original.flush();
        }

        @Override
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.copy.write(b, off, len);
            this.original.write(b, off, len);
        }

        public PrintStream getOriginal() {
            return this.original;
        }

        @Override
        public void flush() throws IOException {
            this.copy.flush();
            this.original.flush();
        }

    }

    /**
     * Allow AnsiOutput to not be on the test classpath.
     */
    private static class AnsiOutputControl {

        public void disableAnsiOutput() {
        }

        public void enabledAnsiOutput() {
        }

        public static AnsiOutputControl get() {
            return new AnsiOutputControl();
        }

    }

}