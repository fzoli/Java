package org.dyndns.fzoli.radioinfo.impl;

import java.io.File;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;

/**
 *
 * @author zoli
 */
public class FM95RadioInfoLoader extends RadioInfoLoader {

    public FM95RadioInfoLoader(RadioInfoView view, File storeFile) {
        super(view, storeFile);
    }

    @Override
    protected String getHttpUrl() {
        return "http://radiofm95.hu/netradio_now_fooldal.php";
    }
    
    @Override
    protected FM95RadioInfo createRadioInfo(byte[] src) {
        return new FM95RadioInfo(this, src);
    }

}
