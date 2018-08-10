package org.ballerinalang.os.nativeimpl.file;


import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.os.Utils;
import org.ballerinalang.os.wrappers.file.FileWrapper;

/**
 * Extern function mirage/os:ls.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.0.0",
        functionName = "ls",
        returnType = {@ReturnType(type = TypeKind.ARRAY, elementType = TypeKind.STRING),
                @ReturnType(type = TypeKind.OBJECT, structType = "error", structPackage = "ballerina.builtin")},
        isPublic = true
)
public class ListFiles extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        try {
            context.setReturnValues(FileWrapper.listFiles(context));
        } catch (Exception e) {
            context.setReturnValues(Utils.createError(context, e.getMessage()));
        }
    }
}
