package org.ballerinalang.os.nativeimpl.process;


import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.os.Utils;
import org.ballerinalang.os.wrappers.process.ProcessWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extern function mirage/os:pipe.
 */
@BallerinaFunction(
        orgName = "mirage",
        packageName = "os:0.0.0",
        functionName = "pipe",
        args = {@Argument(name = "processes", type = TypeKind.ARRAY, elementType = TypeKind.OBJECT)},
        returnType = {@ReturnType(type = TypeKind.ARRAY, elementType = TypeKind.BYTE), @ReturnType(type = TypeKind.INT),
                @ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class Pipe extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        BRefValueArray args = (BRefValueArray) context.getRefArgument(0);
        List<ProcessWrapper> processList = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            BMap<String, BValue> processStruct = (BMap<String, BValue>) args.get(i);
            processList.add((ProcessWrapper) processStruct.getNativeData(ProcessWrapper.INSTANCE_KEY));
        }
        try {
            context.setReturnValues(ProcessWrapper.pipe(processList));
        } catch (IOException | InterruptedException e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
