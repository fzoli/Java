package first;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import net.jxta.exception.PeerGroupException;
import net.jxta.logging.Logging;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

public class PassivePeer {
    
    public static final String NETWORK_NAME = "PassivePeer";
    public static final String LAN_URI = "tcp://192.168.10.1:1230";
    public static final String wAN_URI = "tcp://91.82.50.54:1230";
    
    public static void main(String[] args) throws IOException, URISyntaxException, PeerGroupException, ClassNotFoundException {
        System.setProperty(Logging.JXTA_LOGGING_PROPERTY, Level.OFF.toString());
        NetworkManager manager = new NetworkManager(NetworkManager.ConfigMode.EDGE, NETWORK_NAME, new File(new File(".cache"), NETWORK_NAME).toURI());
        manager.setUseDefaultSeeds(false);
        NetworkConfigurator configurator = manager.getConfigurator();
        configurator.setHttpEnabled(false);
        configurator.setTcpIncoming(false);
        URI lan = new URI(LAN_URI);
        URI wan = new URI(wAN_URI);
        configurator.addSeedRelay(lan);
        configurator.addSeedRelay(wan);
        configurator.addSeedRendezvous(lan);
        configurator.addSeedRendezvous(wan);
        manager.startNetwork();
        PeerGroup netPeerGroup = manager.getNetPeerGroup();
        PipeAdvertisement pipeAdv = MainRendezvousRelayPeer.createSocketAdvertisement();
        boolean connected = manager.waitForRendezvousConnection(5000);
        System.out.println("P2P hálózatra kapcsolódva.\nRendezvous peer " + (connected ? "megtalálva." : "nem található, sok sikert! (Mázlid lesz, ha megtalálod a szervert...)"));
        Socket socket = null;
        boolean found = true;
        try {
            socket = new JxtaSocket(netPeerGroup,
                        // nincs PeerID megadva
                        null,
                        pipeAdv,
                        // 5 mp időtúllépés
                        5000,
                        // megbízható kapcsolat legyen
                        true);
        }
        catch (SocketTimeoutException ex) {
            found = false;
            System.err.println("Hiba: Nem található meg a szerver.");
        }
        if (found) {
            InputStream stream = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(stream);
            System.out.println("A szerver üzenete: " + (String)in.readObject());
            in.close();
            stream.close();
            socket.close();
        }
        manager.stopNetwork();
        System.exit(found ? 0 : -1);
    }
    
}