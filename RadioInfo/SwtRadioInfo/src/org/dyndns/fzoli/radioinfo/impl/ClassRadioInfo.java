package org.dyndns.fzoli.radioinfo.impl;

import java.io.UnsupportedEncodingException;
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
    
    private class ClassMusic extends Music {
        
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
        String srcText;
        try {
            srcText = new String(src, "iso8859-2");
        }
        catch (UnsupportedEncodingException ex) {
            srcText = new String(src);
        }
        if (srcText.trim().isEmpty()) {
            return null;
        }
        srcText = srcText.replaceAll("\\n", "");
        srcText = srcText.substring(Math.max(srcText.indexOf("<article"), 0));
        srcText = srcText.substring(0, Math.min(srcText.indexOf("</article>") + 10, srcText.length()));
        Matcher m = PATTERN.matcher(srcText);
        if (m.find()) {
            return new ClassMusic(m.group(2), m.group(1));
        }
        return null;
    }
    
}
