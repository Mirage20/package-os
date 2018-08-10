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
 * Extern function mirage/os:chmod.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.0.0",
        functionName = "chmod",
        args = {@Argument(name = "file", type = TypeKind.STRING),
                @Argument(name = "permissions", type = TypeKind.STRING)},
        returnType = {@ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class ChangePermission extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        String file = context.getStringArgument(0);
        String permissions = context.getStringArgument(1);
        try {
            FileWrapper.changePermission(context, file, permissions);
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
