package org.dyndns.fzoli.radioinfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.dyndns.fzoli.radioinfo.RadioInfoView.Status;

/**
 * HTTP csatornán keresztül kapcsolódik egy webszerverhez és kérésre külön
 * szálban letölti a weboldal kódját, a megfelelő feldolgozót példányosítja,
 * ami a letöltött kód alapján létrehozza a zene objektumot, amit a felület megjeleníthet.
 * A letöltés közben a konstruktorban átadott felület állapotát is menedzseli.
 * @author zoli
 */
public abstract class RadioInfoLoader {
    
    /**
     * A felület, ami frissül a letöltés közben és után.
     */
    private final RadioInfoView VIEW;
    
    /**
     * A fájl, amibe a zenék adatai mentődnek.
     */
    private final File STORE_FILE;
    
    /**
     * Szálkezeléshez lock objektum, hogy egyszerre csak egy letöltés fusson.
     */
    private final Object LOCK;
    
    /**
     * A HTTP kapcsolatot lebonyolító kliens objektum.
     */
    private final HttpClient CLIENT;
    
    /**
     * Az aktuális Http GET kérés.
     * A kérés megszakításához van szükség a referenciájára.
     * Ha nincs letöltés folyamatban, akkor null.
     */
    private HttpGet request;
    
    /**
     * Az utoljára letöltött zene adatai.
     * Az első letöltés előtt null.
     */
    private RadioInfo info;
    
    /**
     * Konstruktor.
     * @param view a nézet, mely jelzi a letöltés állapotait
     * @param storeFile opcionális fájl, amibe a számok adatai mentődnek
     * @throws NullPointerException ha a view null
     */
    public RadioInfoLoader(RadioInfoView view, File storeFile) {
        if (view == null) throw new NullPointerException("View can not be null");
        VIEW = view;
        STORE_FILE = storeFile;
        LOCK = new Object();
        CLIENT = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(CLIENT.getParams(), 5000);
    }

    /**
     * Megaja, hogy melyik fájlba kell menteni a zene adatait.
     * @return ha a mentés inaktív, akkor null egyébként az objektum referenciája
     */
    public File getStoreFile() {
        return STORE_FILE;
    }

    /**
     * Megadja az utoljára letöltött zene adatait tároló objektumot.
     * @return null, ha még nem történt egy letöltés sem, egyébként az objektum referenciája
     */
    public RadioInfo getRadioInfo() {
        return info;
    }
    
    /**
     * Megadja, hogy honnan legyen letöltve a weboldal kódja.
     */
    protected abstract String getHttpUrl();
    
    /**
     * Létrehozza a megfelelő kódfeldolgozó objektumot, aminek átadja a kódot.
     */
    protected abstract RadioInfo createRadioInfo(byte[] src);
    
    /**
     * Megszakítja az aktuális letöltést.
     * Ha nincs letöltés, nem csinál semmit.
     */
    public void interrupt() {
        if (request != null) request.abort();
    }
    
    /**
     * Letölti a webszerverről az aktuális adatokat és frissíti a felületet.
     */
    public void loadRadioInfo() {
        VIEW.setStatus(Status.LOADING);
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (LOCK) {
                    request = new HttpGet(getHttpUrl());
                    try {
                        HttpResponse response = CLIENT.execute(request);
                        int status = response.getStatusLine().getStatusCode();
                        if (status >= 200 && status < 300) {
                            InputStream in = response.getEntity().getContent();
                            int length;
                            byte[] cache = new byte[1024];
                            ByteArrayOutputStream store = new ByteArrayOutputStream();
                            while ((length = in.read(cache)) != -1) {
                                store.write(cache, 0, length);
                            }
                            info = createRadioInfo(store.toByteArray());
                            if (info.isValid()) {
                                VIEW.setRadioInfo(info);
                                VIEW.setStatus(Status.SUCCESS);
                            }
                            else {
                                VIEW.setStatus(Status.CHANGED);
                            }
                        }
                        else {
                            VIEW.setStatus(Status.NO_DATA);
                        }
                        try {
                            response.getEntity().consumeContent();
                        }
                        catch (Exception ex) {
                            ;
                        }
                    }
                    catch (SocketException ex) {
                        if (!ex.getMessage().equalsIgnoreCase("Socket closed"))
                            VIEW.setStatus(Status.UNAVAILABLE);
                        else
                            VIEW.setStatus(Status.INTERRUPTED);
                    }
                    catch (IOException ex) {
                        if (!ex.getMessage().equalsIgnoreCase("Request already aborted"))
                            VIEW.setStatus(Status.UNAVAILABLE);
                        else
                            VIEW.setStatus(Status.INTERRUPTED);
                    }
                    catch (Exception ex) {
                        VIEW.setStatus(Status.UNAVAILABLE);
                    }
                    finally {
                        request = null;
                    }
                }
            }
            
        }).start();
    }
    
}
