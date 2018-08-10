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
 * Extern function mirage/os:mkdir.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.0.0",
        functionName = "mkdir",
        args = {@Argument(name = "dir", type = TypeKind.STRING),
                @Argument(name = "createParentDirs", type = TypeKind.BOOLEAN)},
        returnType = {@ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class CreateDir extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        String dir = context.getStringArgument(0);
        boolean createParentDirs = context.getBooleanArgument(0);
        try {
            FileWrapper.createDir(context, dir, createParentDirs);
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
