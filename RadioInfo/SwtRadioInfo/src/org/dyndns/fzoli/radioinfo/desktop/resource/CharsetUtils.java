package org.dyndns.fzoli.radioinfo.desktop.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author zoli
 */
public class CharsetUtils {
    
    private final String[] charsets;

    public CharsetUtils() {
        this(getCharsets());
    }
    
    public CharsetUtils(String[] charsets) {
        if (charsets == null) throw new NullPointerException("Charset array can not be null");
        this.charsets = charsets;
    }
    
    public boolean saveToFile(String str, File out, boolean append) throws IOException {
        if (str == null || out == null) return false;
        Charset charset = out.isFile() ? detectCharset(out) : Charset.defaultCharset();
        if (charset == null) return false;
        byte[] bytes = translate(str, Charset.defaultCharset().name(), charset.name());
        new FileOutputStream(out, append).write(bytes);
        return true;
    }
    
    public Charset detectCharset(File f) {
        return detectCharset(f, charsets);
    }
    
    public static Charset detectCharset(File f, String[] charsets) {

        Charset charset = null;

        for (String charsetName : charsets) {
            charset = detectCharset(f, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }

        return charset;
    }

    private static Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
    
    private static String[] getCharsets() {
        ArrayList<String> ls = new ArrayList<String>();
        Iterator<String> it = Charset.availableCharsets().keySet().iterator();
        while (it.hasNext()) {
            ls.add(it.next());
        }
        String[] lsarr = new String[ls.size()];
        ls.toArray(lsarr);
        return lsarr;
    }
    
    public static byte[] translate(String str, String inEnc, String outSrc) {
        Charset inCharset = Charset.forName(inEnc);
        Charset outCharset = Charset.forName(outSrc);
        ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());
        CharBuffer data = inCharset.decode(inputBuffer);
        ByteBuffer outputBuffer = outCharset.encode(data);
        return outputBuffer.array();
    }
    
}
