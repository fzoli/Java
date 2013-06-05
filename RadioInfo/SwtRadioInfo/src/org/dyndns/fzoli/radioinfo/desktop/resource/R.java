package org.dyndns.fzoli.radioinfo.desktop.resource;

import java.io.File;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A desktop alkalmazás erőforrásait kezelő osztály.
 * @author zoli
 */
public class R {

    /**
     * A values.properties fájl értékeit betöltő objektum.
     */
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(R.class.getPackage().getName().concat(".values"));
    
    /**
     * Az alkalmazás nevét tárolja.
     */
    public static final String APP_NAME = getValue("app_name");
    
    /**
     * A values.properties fájl egyik kulcsa.
     */
    public static final String KEY_ADDRESS = "address",
            KEY_ARTIST = "artist",
            KEY_REFRESH_TIP = "refresh_tip",
            KEY_SAVE_TIP = "save_tip",
            KEY_LOADING = "loading",
            KEY_INTERRUPTED = "interrupted",
            KEY_SUCCESS = "success",
            KEY_UNAVAILABLE = "unavailable",
            KEY_CHANGED = "changed",
            KEY_NO_DATA = "no_data",
            KEY_NO_SWT = "no_swt",
            KEY_ERROR = "error",
            KEY_SAVE_ERROR = "save_error";
    
    /**
     * Az egyik fájl neve, mely beolvasható.
     */
    public static final String FILE_SAVE = "Save-icon.png",
            FILE_REFRESH = "Refresh-icon.png",
            FILE_ICON = "ClassFM-logo.png";

    /**
     * A kért kulcshoz társított szöveget adja vissza a values.properties fájlból.
     * @param key az egyik kulcs a values.properties fájlból
     * @throws NullPointerException ha a kulcs null
     * @throws MissingResourceException ha a kulcs nincs a fájlban
     */
    public static String getValue(String key) {
        return BUNDLE.getObject(key).toString();
    }
    
    /**
     * A kért fájlnévhez létrehoz egy bejövő folyamot, hogy a fájl tartalmát ki lehessen olvasni.
     * @param filename a kért fájl neve
     * @return a stream vagy null, ha nincs ilyen fájlnév
     * @throws NullPointerException ha a fájlnév null
     */
    public static InputStream getStream(String filename) {
        return R.class.getResourceAsStream(filename);
    }

    public static File getSourceFile(String filename) {
        File storeFile;
        try {
            File storeDir = new File(R.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            storeFile = new File(storeDir, filename);
        }
        catch (Exception ex) {
            storeFile = new File(filename);
        }
        return storeFile;
    }
    
}
