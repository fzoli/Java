package org.dyndns.fzoli.radioinfo.desktop.resource;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Az SWT libraryt betöltő osztály.
 * @author zoli
 */
public class SwtLoader {

    /**
     * Betölti az SWT libraryt külső forrásból az adott OS-hez.
     * Ha az SWT már be van töltve, a metódus nem tesz semmit.
     * A metódus a lib nevű könyvtárban keresi meg az OS-hez tartozó fájlt az alábbi fájlok egyike közül:
     * swt-win32.jar swt-win64.jar swt-linux32.jar swt-linux64.jar swt-osx32.jar swt-osx64.jar
     * Ha az OS nem tartozik a három elterjedt közé (pl. Solaris) akkor az swt.jar fájlt tölti be, ha az létezik.
     * @return true, ha a betöltés sikerült vagy már be volt töltve, egyébként false
     */
    public static boolean loadSWT() {
        boolean available;
        try {
            Class.forName("org.eclipse.swt.SWT", false, SwtLoader.class.getClassLoader());
            available = true;
        }
        catch (ClassNotFoundException ex) {
            available = false;
        }
        if (!available) {
            File swtJar = getSwtJar();
            if (swtJar == null) {
                return false;
            }
            try {
                URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                method.setAccessible(true);
                method.invoke(sysloader, new Object[]{swtJar.toURI().toURL()});
            }
            catch (Throwable t) {
                return false;
            }
        }
        return true;
    }

    /**
     * Megkeresi az adott OS-hez illő jar fájlt.
     * @return a fájl vagy null, ha nem található a rendszerhez illő fájl
     */
    private static File getSwtJar() {
        String osName = System.getProperty("os.name").toLowerCase();
        String swtFileNameArchPart = System.getProperty("os.arch").toLowerCase().contains("64") ? "64" : "32";
        String swtFileNameOsPart = osName.contains("win") ? "win" : osName.contains("mac") ? "osx" : osName.contains("linux") || osName.contains("nix") ? "linux" : null;
        String swtFileName = swtFileNameOsPart != null ? "swt-" + swtFileNameOsPart + swtFileNameArchPart + ".jar" : "swt.jar";
        File swtFile = new File("lib", swtFileName).getAbsoluteFile();
        if (!swtFile.exists()) {
            try {
                File srcFile = new File(SwtLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                swtFile = new File(new File(srcFile.getParentFile(), "lib"), swtFileName);
                if (!swtFile.exists()) {
                    swtFile = null;
                }
            }
            catch (Exception ex) {
                swtFile = null;
            }
        }
        return swtFile;
    }
    
}
