package org.ballerinalang.os.nativeimpl.process;


import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.os.wrappers.process.ProcessWrapper;
import org.ballerinalang.util.exceptions.BallerinaException;

import java.io.IOException;

/**
 * Extern function wso2/os:Command.init.
 */
@BallerinaFunction(
        orgName = "wso2",
        packageName = "os:0.0.0",
        functionName = "init",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = "Command", structPackage = "wso2/os:0.0.0")
)
public class Init extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        BMap<String, BValue> bStruct = (BMap<String, BValue>) context.getRefArgument(0);
        String command = bStruct.get("command").stringValue();
        String workingDir = bStruct.get("workingDir").stringValue();
        String[] env = ((BStringArray) bStruct.get("envp")).getStringArray();
        ProcessWrapper process = new ProcessWrapper(context, command, workingDir, env);
        bStruct.addNativeData(ProcessWrapper.INSTANCE_KEY, process);
        try {
            process.start();
        } catch (IOException e) {
            throw new BallerinaException("Error while creating new process.", e);
        }
    }
}
