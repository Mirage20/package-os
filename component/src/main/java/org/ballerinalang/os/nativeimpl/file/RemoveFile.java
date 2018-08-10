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
 * Extern function mirage/os:rm.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.0.0",
        functionName = "rm",
        args = {@Argument(name = "file", type = TypeKind.STRING),
                @Argument(name = "recursive", type = TypeKind.BOOLEAN)},
        returnType = {@ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class RemoveFile extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        String file = context.getStringArgument(0);
        boolean recursive = context.getBooleanArgument(0);
        try {
            FileWrapper.remove(context, file, recursive);
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
