package org.dyndns.fzoli.radioinfo;

/**
 * Egy zene adatait tároló osztály.
 * @author zoli
 */
public class Music {

    /**
     * Az előadó.
     */
    private final String ARTIST;
    
    /**
     * A szám címe.
     */
    private final String ADDRESS;

    /**
     * Új zene példányosítása.
     * @param artist az előadó
     * @param address a szám címe
     */
    public Music(String artist, String address) {
        this.ARTIST = artist;
        this.ADDRESS = address;
    }

    /**
     * A zene címét adja meg.
     */
    public String getAddress() {
        return ADDRESS;
    }

    /**
     * A zene előadóját adja meg.
     */
    public String getArtist() {
        return ARTIST;
    }

    /**
     * Megadja, hogy a szöveges fájlba hogyan kerüljön bele az adat.
     */
    public String getText() {
        return getArtist() + " - " + getAddress();
    }
    
}
