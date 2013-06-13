package org.dyndns.fzoli.radioinfo.desktop;

import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.desktop.resource.R;
import org.dyndns.fzoli.radioinfo.impl.FM95RadioInfoLoader;

/**
 *
 * @author zoli
 */
public class FM95RadioInfoForm extends RadioInfoForm {

    public FM95RadioInfoForm() {
        super(R.FILE_FM95_ICON);
    }

    public FM95RadioInfoForm(String storeFileName) {
        super(storeFileName, R.FILE_FM95_ICON);
    }

    @Override
    protected RadioInfoLoader createLoader(String storeFileName) {
        return new FM95RadioInfoLoader(this, R.getSourceFile(storeFileName));
    }
    
}
