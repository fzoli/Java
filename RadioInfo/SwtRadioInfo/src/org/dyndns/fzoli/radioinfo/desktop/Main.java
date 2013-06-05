package org.dyndns.fzoli.radioinfo.desktop;

import org.dyndns.fzoli.radioinfo.desktop.resource.R;
import org.dyndns.fzoli.radioinfo.desktop.resource.SwtLoader;

/**
 * A desktop alkalmazás indító osztálya.
 * @author zoli
 */
public class Main {
    
    /**
     * A desktop alkalmazás belépési pontja.
     * Betölti az adott operációsrendszerhez az SWT libraryt, majd létrehozza
     * a desktop oldalra írt SWT felületet és megjeleníti azt.
     * Ha az SWT betöltése nem sikerül, hibaüzenetet jelenít meg, mely bezárása
     * után az alkalmazás 1-es hibakóddal ér véget.
     */
    public static void main(String[] args) {
        if (SwtLoader.loadSWT()) {
            new ClassRadioInfoForm().open();
        }
        else {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ex) {
                ;
            }
            javax.swing.JOptionPane.showMessageDialog(null, R.getValue(R.KEY_NO_SWT), R.APP_NAME, javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
}
