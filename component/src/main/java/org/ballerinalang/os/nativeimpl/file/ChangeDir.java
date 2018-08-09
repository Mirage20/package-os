package org.ballerinalang.os.nativeimpl.file;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.os.Utils;
import org.ballerinalang.os.wrappers.file.FileWrapper;

/**
 * Extern function wso2/os:cd.
 */
@BallerinaFunction(
        orgName = "wso2", packageName = "os:0.0.0",
        functionName = "cd",
        args = {@Argument(name = "dir", type = TypeKind.STRING)},
        returnType = {@ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class ChangeDir extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        String dir = context.getStringArgument(0);
        try {
            FileWrapper.setWorkingDir(context, dir);
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
