package org.dyndns.fzoli.radioinfo;

/**
 * Platformtól függetlenül a GUI működéséhez szükséges minimális elvárásokat tartalmazó interfész.
 * @author zoli
 */
public interface RadioInfoView {
    
    /**
     * A felület állapotait tartalmazó felsorolás.
     */
    public enum Status {
        
        /**
         * Adatok betöltése folyamatban.
         * A felületen a mentés és a frissítés tiltottak, mivel értelmetlen lenne a használatuk.
         * Az adatokat megjelenítő panel is láthatatlan ezen idő alatt, mivel még nincs mit megjeleníteni.
         * Az egyetlen használható funkció - ellentétben a többi állapottal - a folyamat megszakítása.
         * Ehhez a státuszhoz ajánlott folyamatjelzőt is alkalmazni
         * (pl. indikátor), mely csak ezen státusz ideje alatt látszik.
         */
        LOADING,
        
        /**
         * A betöltés megszakítva a felhasználó által.
         * Akkor hasznos, ha a betöltés valami miatt már jó ideje nem ér véget és a felhasználó szeretné újraindítani a betöltést.
         * A betöltés megismétlése a frissítés opció használatával lehetséges, de a betöltés alatt ez az opció tíltva van, ezért
         * előbb meg kell szakítani a betöltést, hogy a frissítés opció használható legyen.
         * Ebben az állapotban a mentés és a megszakítás opció tíltva van, mivel nincs mit menteni vagy megszakítani.
         * Az adatokat megjelenítő panel is láthatatlan ezen idő alatt, mivel nincs is mit megjeleníteni.
         */
        INTERRUPTED,
        
        /**
         * A betöltés sikeresen végetért.
         * A felületen a frissítés opció biztosan elérhető, a mentés opció akkor érhető el,
         * ha meg van adva egy fájl, amibe lehet menteni és ebben a fájlban az adott zene nem szerepel.
         * Mivel a megszakítás csak a LOADING állapotban érhető el, ezért a funkció ezen állapotban is tíltva van.
         * Ez az egyetlen állapot, melyben van megjeleníthető adat, ezért az adatokat megjelernítő panel ebben az állapotban látható.
         */
        SUCCESS,
        
        /**
         * A weboldal nem érhető el.
         * Mint minden hiba esetén, a mentés opció nem érhető el, az adatokat megjelenítő panel nem látszik.
         */
        UNAVAILABLE(true),
        
        /**
         * A weboldal kódja megváltozott.
         * Akkor fordulhat elő, ha a weboldal kódját megváltóztatják és a program nem tudja többé a HTML forráskódot értelmezni.
         * Ez esetben a program frissítésre szorul.
         */
        CHANGED(true),
        
        /**
         * A HTTP szerver nem küldött adatot.
         * Akkor fordulhat elő, ha a szerver nem adatot küld, hanem mondjuk átirányítás parancsot a fejlécben definiálva.
         */
        NO_DATA(true);

        /**
         * Megadja, hogy a státusz hibát jelent-e.
         */
        private final boolean ERROR;

        /**
         * Konstruktor nem hibát jelentő státuszhoz.
         */
        private Status() {
            this(false);
        }
        
        /**
         * Konstruktor.
         * @param error true esetén hiba típusú státusz jön létre
         */
        private Status(boolean error) {
            ERROR = error;
        }

        /**
         * Megadja, hogy a státusz hibát jelent-e.
         * Hasznos, ha a felület külön jelzi a hibát a többi üzenettől
         * vagy ha az üzenetek mellé ikon is van társítva.
         */
        public boolean isError() {
            return ERROR;
        }
        
    }
    
    /**
     * Beállítja a felület állapotát a paraméterben megadottra.
     * Ha a paraméter null, a metódus nem csinál semmit, egyébként az állapottól függően frissül a felület.
     */
    public void setStatus(Status status);
    
    /**
     * Frissíti a felületen a zeneadatokat a paraméterben megadott információ alapján.
     * Ha a paraméter null vagy az adat érvénytelen, az adatokat megjelenítő panel nem látható,
     * egyébként az objektumban tárolt információ jelenik meg.
     */
    public void setRadioInfo(RadioInfo info);
    
}
