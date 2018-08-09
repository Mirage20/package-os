package org.ballerinalang.os;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BLangVMErrors;
import org.ballerinalang.bre.bvm.BLangVMStructs;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.util.BLangConstants;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.StructureTypeInfo;

/**
 * Utility functions for OS package.
 */
public class Utils {
    public static BMap<String, BValue> createError(Context context, String errorMessage) {
        PackageInfo ioPkg = context.getProgramFile().getPackageInfo(BLangConstants.BALLERINA_BUILTIN_PKG);
        StructureTypeInfo error = ioPkg.getStructInfo(BLangVMErrors.STRUCT_GENERIC_ERROR);
        return BLangVMStructs.createBStruct(error, errorMessage);
    }
}
