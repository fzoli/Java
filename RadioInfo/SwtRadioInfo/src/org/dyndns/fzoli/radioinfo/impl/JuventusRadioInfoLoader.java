package org.dyndns.fzoli.radioinfo.impl;

import java.io.File;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;

/**
 *
 * @author zoli
 */
public class JuventusRadioInfoLoader extends RadioInfoLoader {

    public JuventusRadioInfoLoader(RadioInfoView view, File storeFile) {
        super(view, storeFile);
    }
    
    @Override
    protected String getHttpUrl() {
        return "http://www.juventus.hu/test2.php";
    }
    
    @Override
    protected JuventusRadioInfo createRadioInfo(byte[] src) {
        return new JuventusRadioInfo(this, src);
    }
    
}
