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
 * Extern function mirage/os:cp.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.1.0",
        functionName = "cp",
        args = {@Argument(name = "src", type = TypeKind.STRING),
                @Argument(name = "dest", type = TypeKind.STRING)},
        returnType = {@ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class CopyFile extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        String src = context.getStringArgument(0);
        String dest = context.getStringArgument(1);
        try {
            FileWrapper.copy(context, src, dest);
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
