package io.github.vincemann.subtitlebuddy;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeLibraryLocator;
import com.github.kwhat.jnativehook.NativeSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Use this class for loading jnativehook native libs from within the image.
 * Expects the libs in the same format as within the jar (usually the libs are extracted from within the jar).
 * Libs must be "mounted" at $pwd/lib/jnativehook.
 * Just copy them from the extracted jar into the image.
 * Must ensure that the appLauncher script of the jlink image always has its current working dir set to the root of the image!
 */
public class JLinkJNativeLibraryLocator implements NativeLibraryLocator {
    private static final Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());

    /**
     * Perform default procedures to interface with the native library. These procedures include
     * unpacking and loading the library into the Java Virtual Machine.
     */
    public Iterator<File> getLibraries() {
        List<File> libraries = new ArrayList<File>(1);

        String libName = System.getProperty("jnativehook.lib.name", "JNativeHook");

        // Get the package name for the GlobalScreen.
        String basePackage = GlobalScreen.class.getPackage().getName().replace('.', '/');

        System.out.println("base package: " + basePackage);

        String libNativeArch = NativeSystem.getArchitecture().toString().toLowerCase();

        System.out.println("arch: " + libNativeArch);
        String libNativeName = System
                .mapLibraryName(libName) // Get what the system "thinks" the library name should be.
                .replaceAll("\\.jnilib$", "\\.dylib"); // Hack for OS X JRE 1.6 and earlier.
        System.out.println("libNativeName: " + libNativeName);

        String currentWorkingDir = System.getProperty("user.dir");

        System.out.println("working dir: " + currentWorkingDir);
        // Resource path for the native library.
        String libPath = currentWorkingDir + "/lib/jnativehook/" +
                NativeSystem.getFamily().toString().toLowerCase() +
                '/' + libNativeArch + '/' + libNativeName;

        System.out.println("Resource path: " + libPath);

        File libFile = new File(libPath);

        if (!libFile.exists()) {
            throw new RuntimeException("Unable to locate JNI library at " + libFile.getPath() + "!\n");
        }

        log.fine("Loading library: " + libFile.getPath() + ".\n");
        libraries.add(libFile);

        return libraries.iterator();
    }
}

