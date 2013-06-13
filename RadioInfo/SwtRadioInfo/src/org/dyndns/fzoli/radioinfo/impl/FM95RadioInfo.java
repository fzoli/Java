package org.dyndns.fzoli.radioinfo.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dyndns.fzoli.radioinfo.Music;
import org.dyndns.fzoli.radioinfo.RadioInfo;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;

/**
 *
 * @author zoli
 */
public class FM95RadioInfo extends RadioInfo {

    private static class FM95Music extends Music {
        
        public FM95Music(String value) {
            super(seperate(value));
        }
        
        private static String[] seperate(String value) {
            String a = null, b;
            int i = value.lastIndexOf(" - ");
            if (i != -1) {
                a = value.substring(0, i);
                b = value.substring(i + 3, value.length());
            }
            else {
                b = value;
            }
            return new String[] {a, b};
        }
        
    }
    
    private static final Pattern PATTERN = Pattern.compile("</font><font.*>(.*)</td>");
    
    public FM95RadioInfo(RadioInfoLoader loader, byte[] src) {
        super(loader, src);
    }

    @Override
    protected Music createMusic(byte[] src) {
        Matcher m = PATTERN.matcher(toString(src, "iso8859-2", "<table", "</table>"));
        if (m.find()) {
            return new FM95Music(m.group(1));
        }
        return null;
    }
    
}
