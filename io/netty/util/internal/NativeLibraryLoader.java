package io.netty.util.internal;

import java.util.Set;
import java.util.Collection;
import java.util.EnumSet;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.AccessController;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;

public final class NativeLibraryLoader {
    private static final InternalLogger logger;
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final File WORKDIR;
    private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;
    
    public static void loadFirstAvailable(final ClassLoader loader, final String... names) {
        final List<Throwable> suppressed = (List<Throwable>)new ArrayList();
        final int length = names.length;
        int i = 0;
        while (i < length) {
            final String name = names[i];
            try {
                load(name, loader);
                return;
            }
            catch (Throwable t) {
                suppressed.add(t);
                NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying next name...", name, t);
                ++i;
                continue;
            }
            break;
        }
        final IllegalArgumentException iae = new IllegalArgumentException("Failed to load any of the given libraries: " + Arrays.toString((Object[])names));
        ThrowableUtil.addSuppressedAndClear((Throwable)iae, suppressed);
        throw iae;
    }
    
    private static String calculatePackagePrefix() {
        final String maybeShaded = NativeLibraryLoader.class.getName();
        final String expected = "io!netty!util!internal!NativeLibraryLoader".replace('!', '.');
        if (!maybeShaded.endsWith(expected)) {
            throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", new Object[] { expected, maybeShaded }));
        }
        return maybeShaded.substring(0, maybeShaded.length() - expected.length());
    }
    
    public static void load(final String originalName, final ClassLoader loader) {
        final String name = calculatePackagePrefix().replace('.', '_') + originalName;
        final List<Throwable> suppressed = (List<Throwable>)new ArrayList();
        try {
            loadLibrary(loader, name, false);
        }
        catch (Throwable ex) {
            suppressed.add(ex);
            NativeLibraryLoader.logger.debug("{} cannot be loaded from java.libary.path, now trying export to -Dio.netty.native.workdir: {}", name, NativeLibraryLoader.WORKDIR, ex);
            final String libname = System.mapLibraryName(name);
            final String path = "META-INF/native/" + libname;
            InputStream in = null;
            OutputStream out = null;
            File tmpFile = null;
            Label_0142: {
                if (loader == null) {
                    final URL url = ClassLoader.getSystemResource(path);
                    break Label_0142;
                }
                URL url = loader.getResource(path);
                try {
                    if (url == null) {
                        if (!PlatformDependent.isOsx()) {
                            final FileNotFoundException fnf = new FileNotFoundException(path);
                            ThrowableUtil.addSuppressedAndClear((Throwable)fnf, suppressed);
                            throw fnf;
                        }
                        final String fileName = path.endsWith(".jnilib") ? ("META-INF/native/lib" + name + ".dynlib") : ("META-INF/native/lib" + name + ".jnilib");
                        if (loader == null) {
                            url = ClassLoader.getSystemResource(fileName);
                        }
                        else {
                            url = loader.getResource(fileName);
                        }
                        if (url == null) {
                            final FileNotFoundException fnf2 = new FileNotFoundException(fileName);
                            ThrowableUtil.addSuppressedAndClear((Throwable)fnf2, suppressed);
                            throw fnf2;
                        }
                    }
                    final int index = libname.lastIndexOf(46);
                    final String prefix = libname.substring(0, index);
                    final String suffix = libname.substring(index, libname.length());
                    tmpFile = File.createTempFile(prefix, suffix, NativeLibraryLoader.WORKDIR);
                    in = url.openStream();
                    out = (OutputStream)new FileOutputStream(tmpFile);
                    final byte[] buffer = new byte[8192];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    out.flush();
                    closeQuietly((Closeable)out);
                    out = null;
                    loadLibrary(loader, tmpFile.getPath(), true);
                }
                catch (UnsatisfiedLinkError e) {
                    try {
                        if (tmpFile != null && tmpFile.isFile() && tmpFile.canRead() && !canExecuteExecutable(tmpFile)) {
                            NativeLibraryLoader.logger.info("{} exists but cannot be executed even when execute permissions set; check volume for \"noexec\" flag; use -Dio.netty.native.workdir=[path] to set native working directory separately.", tmpFile.getPath());
                        }
                    }
                    catch (Throwable t) {
                        suppressed.add(t);
                        NativeLibraryLoader.logger.debug("Error checking if {} is on a file store mounted with noexec", tmpFile, t);
                    }
                    ThrowableUtil.addSuppressedAndClear((Throwable)e, suppressed);
                    throw e;
                }
                catch (Exception e2) {
                    final UnsatisfiedLinkError ule = new UnsatisfiedLinkError("could not load a native library: " + name);
                    ule.initCause((Throwable)e2);
                    ThrowableUtil.addSuppressedAndClear((Throwable)ule, suppressed);
                    throw ule;
                }
                finally {
                    closeQuietly((Closeable)in);
                    closeQuietly((Closeable)out);
                    if (tmpFile != null && (!NativeLibraryLoader.DELETE_NATIVE_LIB_AFTER_LOADING || !tmpFile.delete())) {
                        tmpFile.deleteOnExit();
                    }
                }
            }
        }
    }
    
    private static void loadLibrary(final ClassLoader loader, final String name, final boolean absolute) {
        Throwable suppressed = null;
        try {
            try {
                final Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
                loadLibraryByHelper(newHelper, name, absolute);
                NativeLibraryLoader.logger.debug("Successfully loaded the library {}", name);
                return;
            }
            catch (UnsatisfiedLinkError e) {
                suppressed = (Throwable)e;
                NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
            }
            catch (Exception e2) {
                suppressed = (Throwable)e2;
                NativeLibraryLoader.logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e2);
            }
            NativeLibraryUtil.loadLibrary(name, absolute);
            NativeLibraryLoader.logger.debug("Successfully loaded the library {}", name);
        }
        catch (UnsatisfiedLinkError ule) {
            if (suppressed != null) {
                ThrowableUtil.addSuppressed((Throwable)ule, suppressed);
            }
            throw ule;
        }
    }
    
    private static void loadLibraryByHelper(final Class<?> helper, final String name, final boolean absolute) throws UnsatisfiedLinkError {
        final Object ret = AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    final Method method = helper.getMethod("loadLibrary", new Class[] { String.class, Boolean.TYPE });
                    method.setAccessible(true);
                    return method.invoke(null, new Object[] { name, absolute });
                }
                catch (Exception e) {
                    return e;
                }
            }
        });
        if (!(ret instanceof Throwable)) {
            return;
        }
        final Throwable t = (Throwable)ret;
        assert !(t instanceof UnsatisfiedLinkError) : new StringBuilder().append(t).append(" should be a wrapper throwable").toString();
        final Throwable cause = t.getCause();
        if (cause instanceof UnsatisfiedLinkError) {
            throw (UnsatisfiedLinkError)cause;
        }
        final UnsatisfiedLinkError ule = new UnsatisfiedLinkError(t.getMessage());
        ule.initCause(t);
        throw ule;
    }
    
    private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper) throws ClassNotFoundException {
        try {
            return Class.forName(helper.getName(), false, loader);
        }
        catch (ClassNotFoundException e1) {
            if (loader == null) {
                throw e1;
            }
            try {
                final byte[] classBinary = classToByteArray(helper);
                return AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Class<?>>() {
                    public Class<?> run() {
                        try {
                            final Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE });
                            defineClass.setAccessible(true);
                            return defineClass.invoke(loader, new Object[] { helper.getName(), classBinary, 0, classBinary.length });
                        }
                        catch (Exception e) {
                            throw new IllegalStateException("Define class failed!", (Throwable)e);
                        }
                    }
                });
            }
            catch (ClassNotFoundException e2) {
                ThrowableUtil.addSuppressed((Throwable)e2, (Throwable)e1);
                throw e2;
            }
            catch (RuntimeException e3) {
                ThrowableUtil.addSuppressed((Throwable)e3, (Throwable)e1);
                throw e3;
            }
            catch (Error e4) {
                ThrowableUtil.addSuppressed((Throwable)e4, (Throwable)e1);
                throw e4;
            }
        }
    }
    
    private static byte[] classToByteArray(final Class<?> clazz) throws ClassNotFoundException {
        String fileName = clazz.getName();
        final int lastDot = fileName.lastIndexOf(46);
        if (lastDot > 0) {
            fileName = fileName.substring(lastDot + 1);
        }
        final URL classUrl = clazz.getResource(fileName + ".class");
        if (classUrl == null) {
            throw new ClassNotFoundException(clazz.getName());
        }
        final byte[] buf = new byte[1024];
        final ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        InputStream in = null;
        try {
            in = classUrl.openStream();
            int r;
            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
            return out.toByteArray();
        }
        catch (IOException ex) {
            throw new ClassNotFoundException(clazz.getName(), (Throwable)ex);
        }
        finally {
            closeQuietly((Closeable)in);
            closeQuietly((Closeable)out);
        }
    }
    
    private static void closeQuietly(final Closeable c) {
        if (c != null) {
            try {
                c.close();
            }
            catch (IOException ex) {}
        }
    }
    
    private NativeLibraryLoader() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
        final String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
        if (workdir != null) {
            File f = new File(workdir);
            f.mkdirs();
            try {
                f = f.getAbsoluteFile();
            }
            catch (Exception ex) {}
            WORKDIR = f;
            NativeLibraryLoader.logger.debug(new StringBuilder().append("-Dio.netty.native.workdir: ").append(NativeLibraryLoader.WORKDIR).toString());
        }
        else {
            WORKDIR = PlatformDependent.tmpdir();
            NativeLibraryLoader.logger.debug(new StringBuilder().append("-Dio.netty.native.workdir: ").append(NativeLibraryLoader.WORKDIR).append(" (io.netty.tmpdir)").toString());
        }
        DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertyUtil.getBoolean("io.netty.native.deleteLibAfterLoading", true);
    }
    
    private static final class NoexecVolumeDetector {
        private static boolean canExecuteExecutable(final File file) throws IOException {
            if (PlatformDependent.javaVersion() < 7) {
                return true;
            }
            if (file.canExecute()) {
                return true;
            }
            final Set<PosixFilePermission> existingFilePermissions = (Set<PosixFilePermission>)Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
            final Set<PosixFilePermission> executePermissions = (Set<PosixFilePermission>)EnumSet.of((Enum)PosixFilePermission.OWNER_EXECUTE, (Enum)PosixFilePermission.GROUP_EXECUTE, (Enum)PosixFilePermission.OTHERS_EXECUTE);
            if (existingFilePermissions.containsAll((Collection)executePermissions)) {
                return false;
            }
            final Set<PosixFilePermission> newPermissions = (Set<PosixFilePermission>)EnumSet.copyOf((Collection)existingFilePermissions);
            newPermissions.addAll((Collection)executePermissions);
            Files.setPosixFilePermissions(file.toPath(), (Set)newPermissions);
            return file.canExecute();
        }
    }
}
