package org.ballerinalang.os.nativeimpl.file;


import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.os.wrappers.file.FileWrapper;

/**
 * Extern function mirage/os:pwd.
 */
@BallerinaFunction(
        orgName = "mirage", packageName = "os:0.0.0",
        functionName = "pwd",
        returnType = {@ReturnType(type = TypeKind.STRING)},
        isPublic = true
)
public class GetCwd extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        context.setReturnValues(FileWrapper.getWorkingDir(context));
    }
}
