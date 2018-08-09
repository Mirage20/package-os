package org.ballerinalang.os.wrappers.process;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BLangVMStructs;
import org.ballerinalang.model.types.BArrayType;
import org.ballerinalang.model.types.BTupleType;
import org.ballerinalang.model.types.BTypes;
import org.ballerinalang.model.values.BByteArray;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.os.wrappers.file.FileWrapper;
import org.ballerinalang.stdlib.io.channels.base.Channel;
import org.ballerinalang.stdlib.io.utils.IOConstants;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.StructureTypeInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wraps the Java Process API for Ballerina operations.
 */
public class ProcessWrapper {

    public static final String INSTANCE_KEY = "Process";

    private final String command;
    private final String workingDir;
    private final String[] env;
    private Process processInstance;

    public ProcessWrapper(Context context, String command, String workingDir, String[] env) {
        this.command = command;
        if (workingDir.trim().isEmpty()) {
            this.workingDir = FileWrapper.getWorkingDir(context).stringValue();
        } else {
            this.workingDir = workingDir;
        }
        List<String> envNonEmpty = new ArrayList<>();
        for (String property : env) {
            if (!property.trim().isEmpty()) {
                envNonEmpty.add(property);
            }
        }
        this.env = envNonEmpty.isEmpty() ? null : envNonEmpty.toArray(new String[envNonEmpty.size()]);
    }

    public void start() throws IOException {
        Path dir = Paths.get(workingDir);
        processInstance = Runtime.getRuntime().exec(command, env, dir.toFile());
    }

    public BMap<String, BValue> getStdoutChannel(Context context) {
        ByteChannel byteChannel = new ReadableByteChannel(processInstance.getInputStream());
        Channel channel = new ProcessChannel(byteChannel);
        PackageInfo timePackageInfo = context.getProgramFile().getPackageInfo("ballerina/io");
        final StructureTypeInfo structInfo = timePackageInfo.getStructInfo("ByteChannel");
        BMap<String, BValue> channelStruct = BLangVMStructs.createBStruct(structInfo);
        channelStruct.addNativeData(IOConstants.BYTE_CHANNEL_NAME, channel);
        return channelStruct;
    }

    public BMap<String, BValue> getStderrChannel(Context context) {
        ByteChannel byteChannel = new ReadableByteChannel(processInstance.getErrorStream());

        Channel channel = new ProcessChannel(byteChannel);
        PackageInfo timePackageInfo = context.getProgramFile().getPackageInfo("ballerina/io");
        final StructureTypeInfo structInfo = timePackageInfo.getStructInfo("ByteChannel");
        BMap<String, BValue> channelStruct = BLangVMStructs.createBStruct(structInfo);
        channelStruct.addNativeData(IOConstants.BYTE_CHANNEL_NAME, channel);
        return channelStruct;
    }

    public BMap<String, BValue> getStdinChannel(Context context) {
        ByteChannel byteChannel = new WritableByteChannel(processInstance.getOutputStream());
        Channel channel = new ProcessChannel(byteChannel);
        PackageInfo timePackageInfo = context.getProgramFile().getPackageInfo("ballerina/io");
        final StructureTypeInfo structInfo = timePackageInfo.getStructInfo("ByteChannel");
        BMap<String, BValue> channelStruct = BLangVMStructs.createBStruct(structInfo);
        channelStruct.addNativeData(IOConstants.BYTE_CHANNEL_NAME, channel);
        return channelStruct;
    }

    public int waitFor() throws IOException, InterruptedException {
        return processInstance.waitFor();
    }

    public static BRefValueArray pipe(List<ProcessWrapper> processList) throws IOException, InterruptedException {
        Process pFrom;
        Process pTo;
        for (int i = 0; i < processList.size(); i++) {
            pFrom = processList.get(i).processInstance;
            if (i + 1 < processList.size()) {
                pTo = processList.get(i + 1).processInstance;
                new Thread(new ProcessPipe(pFrom, pTo)).start();
            }
        }
        Process pFinal = processList.get(processList.size() - 1).processInstance;
        int exitCode = pFinal.waitFor();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int len;
        while ((len = pFinal.getInputStream().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, len);
        }
        buffer.flush();
        BRefValueArray tuple = new BRefValueArray(
                new BTupleType(Arrays.asList(new BArrayType(BTypes.typeByte), BTypes.typeInt))
        );
        tuple.add(0, new BByteArray(Arrays.copyOf(buffer.toByteArray(), buffer.toByteArray().length)));
        tuple.add(1, new BInteger(exitCode));
        return tuple;
    }

    public void kill() {
        processInstance.destroy();
    }
}

