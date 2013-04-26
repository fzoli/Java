package first;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

class ConnectionHandler implements Runnable {

    private final Socket SOCKET;
    
    public ConnectionHandler(Socket socket) {
        SOCKET = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream stream = SOCKET.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(stream);
            out.writeObject("Helló peer!");
            out.flush();
            out.close();
            stream.flush();
            stream.close();
            SOCKET.close();
        } catch (Exception ex) {
            System.err.println("Kliens socket hiba! " + ex.getMessage());
        }        
    }
    
}

public class MainRendezvousRelayPeer {
    
    public static final String NETWORK_NAME = "MyTest";
    public static final int TCP_PORT = 1230;
    public static final PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, NETWORK_NAME.getBytes());
    
    public static PipeAdvertisement createSocketAdvertisement() {
        String socketName = "MySocketTest";
        PipeID socketID = IDFactory.newPipeID(PeerGroupID.defaultNetPeerGroupID, socketName.getBytes());
        PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        advertisement.setName(socketName);
        advertisement.setPipeID(socketID);
        advertisement.setType(PipeService.UnicastType);
        return advertisement;
    }
    
    public static void main(String[] args) throws IOException, PeerGroupException {
        NetworkManager manager = new NetworkManager(NetworkManager.ConfigMode.RENDEZVOUS_RELAY, NETWORK_NAME, new File(new File(".cache"), NETWORK_NAME).toURI());
        manager.setUseDefaultSeeds(false);
        NetworkConfigurator configurator = manager.getConfigurator();
        configurator.setPeerID(PID);
        configurator.setTcpPort(TCP_PORT);
        configurator.setHttpEnabled(false);
        configurator.setTcpEnabled(true);
        configurator.setTcpIncoming(true);
        configurator.setTcpOutgoing(true);
        manager.startNetwork();
        ServerSocket serverSocket = new JxtaServerSocket(manager.getNetPeerGroup(), createSocketAdvertisement(), 10);
        serverSocket.setSoTimeout(0);
        System.out.println("Socket server elindítva.");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Új kapcsolat: " + socket.getInetAddress());
            Thread thread = new Thread(new ConnectionHandler(socket));
            thread.start();
        }
    }
    
}