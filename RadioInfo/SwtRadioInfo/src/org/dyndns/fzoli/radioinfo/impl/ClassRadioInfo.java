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
public class ClassRadioInfo extends RadioInfo {
    
    private static class ClassMusic extends Music {
        
        public ClassMusic(String artist, String address) {
            super(artist, address);
        }

        @Override
        public String getArtist() {
            String artist = super.getArtist();
            return artist.substring(0, artist.indexOf(" (most megy)"));
        }
        
    }
    
    private static final Pattern PATTERN = Pattern.compile("<article.*<a.*>(.*)</a>.*<span.*>(.*)</span>.*</article>");

    public ClassRadioInfo(RadioInfoLoader loader, byte[] src) {
        super(loader, src);
    }
    
    @Override
    protected Music createMusic(byte[] src) {
        Matcher m = PATTERN.matcher(toString(src, "iso8859-2", "<article", "</article>"));
        if (m.find()) {
            return new ClassMusic(m.group(2), m.group(1));
        }
        return null;
    }
    
}
