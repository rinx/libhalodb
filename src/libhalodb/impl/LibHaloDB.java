package libhalodb.impl;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import com.oracle.svm.core.c.CConst;

public final class LibHaloDB {
    @CEntryPoint(name = "halodb_open")
    public static int halodbOpen(IsolateThread thread, @CConst CCharPointer s) {
        String path = CTypeConversion.toJavaString(s);
        return libhalodb.halodbOpen(path);
    }

    @CEntryPoint(name = "halodb_close")
    public static int halodbClose(IsolateThread thread) {
        return libhalodb.halodbClose();
    }

    @CEntryPoint(name = "halodb_size")
    public static long halodbSize(IsolateThread thread) {
        return libhalodb.halodbSize();
    }

    @CEntryPoint(name = "halodb_put")
    public static int halodbPut(IsolateThread thread, @CConst CCharPointer ks, @CConst CCharPointer vs) {
        String key = CTypeConversion.toJavaString(ks);
        String val = CTypeConversion.toJavaString(vs);
        return libhalodb.halodbPut(key, val);
    }

    @CEntryPoint(name = "halodb_get")
    public static @CConst CCharPointer halodbGet(IsolateThread thread, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        String result = libhalodb.halodbGet(key);
        CTypeConversion.CCharPointerHolder holder = CTypeConversion.toCString(result);
        CCharPointer value = holder.get();
        return value;
    }

    @CEntryPoint(name = "halodb_delete")
    public static int halodbDelete(IsolateThread thread, @CConst CCharPointer s) {
        String key = CTypeConversion.toJavaString(s);
        return libhalodb.halodbDelete(key);
    }

    @CEntryPoint(name = "halodb_pause_compaction")
    public static int halodbPauseCompaction(IsolateThread thread) {
        return libhalodb.halodbPauseCompaction();
    }

    @CEntryPoint(name = "halodb_resume_compaction")
    public static int halodbResumeCompaction(IsolateThread thread) {
        return libhalodb.halodbResumeCompaction();
    }
}
