package org.ballerinalang.os.wrappers.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Data piping thread which implements data transfer between stdout to stdin.
 */
public class ProcessPipe implements Runnable {

    private final Process pFrom;
    private final Process pTo;

    ProcessPipe(Process from, Process to) {
        this.pFrom = from;
        this.pTo = to;
    }

    @Override
    public void run() {
        InputStream stdout = pFrom.getInputStream();
        OutputStream stdin = pTo.getOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = stdout.read(buffer)) > 0) {
                stdin.write(buffer, 0, read);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot pipe between process.", e);
        } finally {
            try {
                stdout.close();
            } catch (IOException ignored) {
            }
            try {
                stdin.close();
            } catch (IOException ignored) {
            }
        }
    }
}
