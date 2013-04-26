package first;

import java.io.IOException;
import java.net.URISyntaxException;
import net.jxta.exception.PeerGroupException;

public class Main {
    
    public static void main(String[] args) throws IOException, URISyntaxException, PeerGroupException, ClassNotFoundException {
        String param = "client";
        if (args.length >= 1) {
            String s = args[0];
            if (s.equals("client") || s.equals("server")) {
                param = s;
            }
        }
        if (param.equals("client")) PassivePeer.main(args);
        else MainRendezvousRelayPeer.main(args);
    }
    
}