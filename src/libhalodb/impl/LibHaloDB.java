package libhalodb.impl;

import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import com.oracle.svm.core.c.CConst;

public final class LibHaloDB {
    @CEntryPoint(name = "halodb_open")
    public static byte halodbOpen(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String path = CTypeConversion.toJavaString(s);
        Boolean result = libhalodb.halodbOpen(path);
        return CTypeConversion.toCBoolean(result);
    }

    @CEntryPoint(name = "halodb_close")
    public static byte halodbClose(@CEntryPoint.IsolateThreadContext long isolateId) {
        Boolean result = libhalodb.halodbClose();
        return CTypeConversion.toCBoolean(result);
    }

    @CEntryPoint(name = "halodb_size")
    public static long halodbSize(@CEntryPoint.IsolateThreadContext long isolateId) {
        return libhalodb.halodbSize();
    }

    @CEntryPoint(name = "halodb_put")
    public static byte halodbPut(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer ks, @CConst CCharPointer vs) {
        String key = CTypeConversion.toJavaString(ks);
        String val = CTypeConversion.toJavaString(vs);
        Boolean result = libhalodb.halodbPut(key, val);
        return CTypeConversion.toCBoolean(result);
    }

    @CEntryPoint(name = "halodb_get")
    public static @CConst CCharPointer halodbGet(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        String result = libhalodb.halodbGet(key);
        CTypeConversion.CCharPointerHolder holder = CTypeConversion.toCString(result);
        CCharPointer value = holder.get();
        return value;
    }

    @CEntryPoint(name = "halodb_delete")
    public static byte halodbDelete(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        Boolean result = libhalodb.halodbDelete(key);
        return CTypeConversion.toCBoolean(result);
    }
}
