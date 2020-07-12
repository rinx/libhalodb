package libhalodb.impl;

import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import com.oracle.svm.core.c.CConst;

public final class LibHaloDB {
    @CEntryPoint(name = "halodb_open")
    public static int halodbOpen(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String path = CTypeConversion.toJavaString(s);
        return libhalodb.halodbOpen(path);
    }

    @CEntryPoint(name = "halodb_close")
    public static int halodbClose(@CEntryPoint.IsolateThreadContext long isolateId) {
        return libhalodb.halodbClose();
    }

    @CEntryPoint(name = "halodb_size")
    public static long halodbSize(@CEntryPoint.IsolateThreadContext long isolateId) {
        return libhalodb.halodbSize();
    }

    @CEntryPoint(name = "halodb_put")
    public static int halodbPut(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer ks, @CConst CCharPointer vs) {
        String key = CTypeConversion.toJavaString(ks);
        String val = CTypeConversion.toJavaString(vs);
        return libhalodb.halodbPut(key, val);
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
    public static int halodbDelete(@CEntryPoint.IsolateThreadContext long isolateId, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        return libhalodb.halodbDelete(key);
    }
}
