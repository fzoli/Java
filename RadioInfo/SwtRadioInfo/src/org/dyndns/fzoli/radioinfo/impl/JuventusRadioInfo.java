package org.dyndns.fzoli.radioinfo.impl;

import com.google.gson.Gson;
import org.dyndns.fzoli.radioinfo.Music;
import org.dyndns.fzoli.radioinfo.RadioInfo;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;

/**
 *
 * @author zoli
 */
public class JuventusRadioInfo extends RadioInfo {

    private static final Gson GSON = new Gson();
    
    private static class DataObject {
        public String adat2, adat3;
    }
    
    public JuventusRadioInfo(RadioInfoLoader loader, byte[] src) {
        super(loader, src);
    }

    @Override
    protected Music createMusic(byte[] src) {
        DataObject obj = GSON.fromJson(new String(src), DataObject.class);
        int ind = obj.adat3.indexOf("<!");
        return new Music(obj.adat2, ind == -1 ? obj.adat3 : obj.adat3.substring(0, ind));
    }
    
}
