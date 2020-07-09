package libhalodb.impl;

import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import com.oracle.svm.core.c.CConst;

public final class LibHaloDB {
    @CEntryPoint(name = "halodb_open")
    public static @CConst CCharPointer halodbOpen(@CEntryPoint.IsolateThreadContext long isolateId) {
        String result = libhalodb.halodbOpen();
        CTypeConversion.CCharPointerHolder holder = CTypeConversion.toCString(result);
        CCharPointer value = holder.get();
        return value;
    }

    @CEntryPoint(name = "halodb_put")
    public static @CConst CCharPointer halodbPut(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer ks, @CConst CCharPointer vs) {
        String key = CTypeConversion.toJavaString(ks);
        String val = CTypeConversion.toJavaString(vs);
        String result = libhalodb.halodbPut(key, val);
        CTypeConversion.CCharPointerHolder holder = CTypeConversion.toCString(result);
        CCharPointer value = holder.get();
        return value;
    }

    @CEntryPoint(name = "halodb_get")
    public static @CConst CCharPointer halodbGet(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        String result = libhalodb.halodbGet(key);
        CTypeConversion.CCharPointerHolder holder = CTypeConversion.toCString(result);
        CCharPointer value = holder.get();
        return value;
    }
}
