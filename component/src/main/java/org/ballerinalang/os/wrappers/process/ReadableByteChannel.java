package org.ballerinalang.os.wrappers.process;

import org.ballerinalang.stdlib.io.utils.BallerinaIOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

/**
 * Read only implementation of the ByteChannel. Used for creating channels for stdout/stderr pipe.
 */
public class ReadableByteChannel implements ByteChannel {
    private InputStream in;
    private java.nio.channels.ReadableByteChannel inChannel;

    ReadableByteChannel(InputStream in) {
        this.in = in;
        this.inChannel = Channels.newChannel(in);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return inChannel.read(dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        throw new BallerinaIOException("Unsupported operation.");
    }

    @Override
    public boolean isOpen() {
        return inChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        inChannel.close();
        in.close();
    }
}
