package org.ballerinalang.os.wrappers.process;

import org.ballerinalang.stdlib.io.channels.base.Channel;
import org.ballerinalang.stdlib.io.channels.base.readers.ChannelReader;
import org.ballerinalang.stdlib.io.channels.base.writers.ChannelWriter;
import org.ballerinalang.stdlib.io.utils.BallerinaIOException;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Ballerina channel implementation for process communication.
 */
public class ProcessChannel extends Channel {

    ProcessChannel(ByteChannel channel) throws BallerinaIOException {
        super(channel, new ChannelReader(), new ChannelWriter());
    }

    @Override
    public void transfer(int i, int i1, WritableByteChannel writableByteChannel) throws IOException {
        throw new BallerinaIOException("Unsupported operation.");
    }

    @Override
    public Channel getChannel() {
        return this;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }
}
