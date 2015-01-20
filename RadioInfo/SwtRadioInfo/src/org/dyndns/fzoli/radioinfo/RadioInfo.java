package org.dyndns.fzoli.radioinfo;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

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
        Music music;
        try {
            music = createMusic(src);
        }
        catch (Exception ex) {
            music = null;
        }
        this.MUSIC = music;
        this.LOADER = loader;
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
     * - Ha a mentés nem érhető el, {@link SaveResult#DISABLED} értékkel tér vissza.
     * - Ha már el van mentve a zene, {@link SaveResult#ALREADY_SAVED} értékkel tér vissza.
     * A mentés úgy történik, hogy megnyitja append módban a szöveges fájlt
     * és bele írja a zene egyesített szövegét,
     * ami után Windows sorjelet tesz a program OS-től függetlenül.
     * Ez után a stream lezáródik és ha nem keletkezett kivétel,
     * akkor {@link SaveResult#SUCCESS} értékkel tér vissza,
     * egyébként {@link SaveResult#ERROR} értékkel.
     * @see SaveResult#isSaved()
     */
    public SaveResult save() {
        if (!isSaveAvailable()) return SaveResult.DISABLED;
        if (isSaved()) return SaveResult.ALREADY_SAVED;
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(LOADER.getStoreFile(), true), Charset.forName("UTF-8").newEncoder());
            out.write(getMusic().getText() + "\r\n");
            out.flush();
            out.close();
        }
        catch (Exception ex) {
            return SaveResult.ERROR;
        }
        return SaveResult.SUCCESS;
    }
    
    /**
     * Definiálja a mentés metódus lefutásának az eredményeit.
     */
    public enum SaveResult {
        
        /** Sikeres mentés. */
        SUCCESS(true),
        
        /** Sikertelen mentés. */
        ERROR,
        
        /** Nem történt mentés, mert már el lett mentve. */
        ALREADY_SAVED(true),
        
        /** A mentés funkció nem érhető el. */
        DISABLED;

        private final boolean SAVED;
        
        private SaveResult() {
            this(false);
        }

        private SaveResult(boolean saved) {
            SAVED = saved;
        }
        
        /**
         * Megadja, hogy az elmentésre kért adat benne van-e a fájlban.
         * @return true, ha a mentés sikerült és bele került a fájlba az adat
         * vagy ha már a mentés előtt is benne volt a fájlban; egyébként false
         */
        public boolean isSaved() {
            return SAVED;
        }
        
    }
    
    /**
     * String szűrő segédmetódus és újsor eltávolító.
     * @param text az eredeti, szűrendő szöveg
     * @param from az a szövegrészlet, melytől kezdődni fog a szűrt szöveg
     * @param to az a szövegrészlet, melyre végződni fog a szűrt szöveg
     * @return a szűrt szöveg
     */
    protected static String filter(String text, String from, String to) {
        if (text == null || text.trim().length() == 0) return "";
        text = text.replaceAll("\\n|\\r", "");
        text = text.substring(Math.max(from == null ? 0 : text.indexOf(from), 0));
        text = text.substring(0, Math.min(to == null ? text.length() : text.indexOf(to) + to.length(), text.length()));
        return text;
    }
    
    /**
     * A megadott bájt tömböt alakítja át szöveggé a kért karakterkódolást használva.
     * @param src egy bájt tömb, mely karakterkódokat tartalmaz
     * @param encode a bájt tömb karakterkódolása (pl. utf8, iso8859-2)
     * @return a bájt tömbnek megfelelő szöveg (a kért kódolással, vagy érvénytelen kódolás esetén az alapértelmezett kódolással)
     */
    protected static String toString(byte[] src, String encode) {
        try {
            return new String(src, encode);
        }
        catch (Exception ex) {
            return new String(src);
        }
    }
    
    /**
     * A megadott bájt tömböt alakítja át szöveggé és megformázza azt.
     * @param src egy bájt tömb, mely karakterkódokat tartalmaz
     * @param encode a bájt tömb karakterkódolása (pl. utf8, iso8859-2)
     * @param from az a szövegrészlet, melytől kezdődni fog a szűrt szöveg
     * @param to az a szövegrészlet, melyre végződni fog a szűrt szöveg
     * @return a bájt tömbnek megfelelő formázott szöveg
     */
    protected static String toString(byte[] src, String encode, String from, String to) {
        return filter(toString(src, encode), from, to);
    }
    
}
