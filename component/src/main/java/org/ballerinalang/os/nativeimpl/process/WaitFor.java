package org.ballerinalang.os.nativeimpl.process;


import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.os.Utils;
import org.ballerinalang.os.wrappers.process.ProcessWrapper;

import java.io.IOException;

/**
 * Extern function wso2/os:Command.waitFor.
 */
@BallerinaFunction(
        orgName = "wso2",
        packageName = "os:0.0.0",
        functionName = "waitFor",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = "Command", structPackage = "wso2/os:0.0.0"),
        returnType = {@ReturnType(type = TypeKind.INT),
                @ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class WaitFor extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        BMap<String, BValue> bStruct = (BMap<String, BValue>) context.getRefArgument(0);
        ProcessWrapper process = (ProcessWrapper) bStruct.getNativeData(ProcessWrapper.INSTANCE_KEY);
        try {
            context.setReturnValues(new BInteger(process.waitFor()));
        } catch (IOException | InterruptedException e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}