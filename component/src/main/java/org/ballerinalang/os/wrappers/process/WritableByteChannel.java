package org.ballerinalang.os.wrappers.process;

import org.ballerinalang.stdlib.io.utils.BallerinaIOException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

/**
 * Write only implementation of the ByteChannel. Used for creating channels for stdin pipe.
 */
public class WritableByteChannel implements ByteChannel {
    private OutputStream out;
    private java.nio.channels.WritableByteChannel outChannel;

    WritableByteChannel(OutputStream out) {
        this.out = out;
        this.outChannel = Channels.newChannel(out);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        throw new BallerinaIOException("Unsupported operation.");
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return outChannel.write(src);
    }

    @Override
    public boolean isOpen() {
        return outChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        outChannel.close();
        out.close();
    }
}
