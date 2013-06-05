package org.dyndns.fzoli.radioinfo.impl;

import java.io.File;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;

/**
 *
 * @author zoli
 */
public class ClassRadioInfoLoader extends RadioInfoLoader {

    public ClassRadioInfoLoader(RadioInfoView view, File storeFile) {
        super(view, storeFile);
    }
    
    @Override
    protected String getHttpUrl() {
        return "http://classfm.hu/front_inc/ezek_mennek.php?thispoll=poll";
    }
    
    @Override
    protected ClassRadioInfo createRadioInfo(byte[] src) {
        return new ClassRadioInfo(this, src);
    }
    
}
