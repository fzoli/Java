package org.dyndns.fzoli.radioinfo.desktop;

import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.desktop.resource.R;
import org.dyndns.fzoli.radioinfo.impl.JuventusRadioInfoLoader;

/**
 *
 * @author zoli
 */
public class JuventusRadioInfoForm extends RadioInfoForm {

    public JuventusRadioInfoForm() {
        super(R.FILE_JUVENTUS_ICON);
    }

    public JuventusRadioInfoForm(String storeFileName) {
        super(storeFileName, R.FILE_JUVENTUS_ICON);
    }
    
    @Override
    protected RadioInfoLoader createLoader(String storeFileName) {
        return new JuventusRadioInfoLoader(this, R.getSourceFile(storeFileName));
    }
    
}
