package org.dyndns.fzoli.radioinfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A rádió HTML válaszából készít zene objektumot és kérésre fájlba menti.
 * @author zoli
 */
public abstract class RadioInfo {
    
    /**
     * A zene objektum referenciája.
     */
    private final Music MUSIC;
    
    /**
     * A RadioInfo objektumot létrehozó betöltő referenciája.
     */
    private final RadioInfoLoader LOADER;
    
    /**
     * Konstruktor, amit az első paraméterben átadott objektum használ.
     * @param loader az objektumot létrehozó betöltő referenciája
     * @param src a betöltő által letöltött válasz a HTML szervertől (általában HTML forráskód)
     */
    public RadioInfo(RadioInfoLoader loader, byte[] src) {
        this.LOADER = loader;
        this.MUSIC = createMusic(src);
    }
    
    /**
     * Létrehozza a HTML szerver válasza alapján a zene objektumot.
     */
    protected abstract Music createMusic(byte[] src);
    
    /**
     * A létrehozott zeneobjektum.
     */
    public Music getMusic() {
        return MUSIC;
    }
    
    /**
     * Megadja, hogy érvényes-e az információ.
     * @return true, ha sikerült létrehozni a zene objektumot, egyébként false
     */
    public boolean isValid() {
        return MUSIC != null;
    }
    
    /**
     * Megadja, hogy elérhető-e a mentés funkció.
     * @return true, ha meg van adva a loaderben a store file és sikerült létrehozni a zene objektumot
     */
    public boolean isSaveAvailable() {
        return LOADER.getStoreFile() != null && isValid();
    }
    
    /**
     * Megadja, hogy ehhez az objektumhoz tartozó zene objektum el van-e már mentve a fájlba.
     * Ha a mentés nem érhető el, hamissal tér vissza.
     * Lekéri a zene egyesített szövegét és a fájlt soronként olvasva megnézi,
     * hogy a sor egyezik-e a zene objektum szövegével.
     * Ha benne van kilép a ciklusból és a fájl stream lezárása után igazzal tér vissza,
     * ha a ciklus lefutott találat nélkül és bezárult a fájl stream, hamissal tér vissza.
     * Ha az olvasás közben bármi hiba történt (pl. nem létező fájl, nincs olvasási jog),
     * akkor hamissal tér vissza.
     */
    public boolean isSaved() {
        if (!isSaveAvailable()) return false;
        String musicText = getMusic().getText();
        try {
            String line;
            boolean match = false;
            BufferedReader in = new BufferedReader(new FileReader(LOADER.getStoreFile()));
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase(musicText)) {
                    match = true;
                    break;
                }
            }
            in.close();
            return match;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Megpróbálja elmenteni a zene objektumot a fájlba.
     * Mielőtt a mentés elkezdődik:
     * - Ha a mentés nem érhető el, hamissal tér vissza.
     * - Ha már el van mentve a zene, igazzal tér vissza.
     * A mentés úgy történik, hogy megnyitja append módban a szöveges fájlt
     * és bele írja a zene egyesített szövegét,
     * ami után Windows sorjelet tesz a program OS-től függetlenül.
     * Ez után a stream lezáródik és ha nem keletkezett kivétel, akkor igazzal tér vissza,
     * egyébként hamissal.
     */
    public boolean save() {
        if (!isSaveAvailable()) return false;
        if (isSaved()) return true;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(LOADER.getStoreFile(), true));
            out.write(getMusic().getText() + "\r\n");
            out.flush();
            out.close();
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
}
