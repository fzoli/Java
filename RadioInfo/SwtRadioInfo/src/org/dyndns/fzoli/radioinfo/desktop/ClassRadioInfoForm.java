package org.dyndns.fzoli.radioinfo.desktop;

import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.desktop.resource.R;
import org.dyndns.fzoli.radioinfo.impl.ClassRadioInfoLoader;

/**
 *
 * @author zoli
 */
public class ClassRadioInfoForm extends RadioInfoForm {

    public ClassRadioInfoForm() {
        super();
    }

    public ClassRadioInfoForm(String storeFileName) {
        super(storeFileName);
    }

    @Override
    protected RadioInfoLoader createLoader(String storeFileName) {
        return new ClassRadioInfoLoader(this, R.getSourceFile(storeFileName));
    }
    
}
