package timemeter;

import java.awt.BasicStroke;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static timemeter.IntervalStorage.isLocked;
import static timemeter.IntervalStorage.setLocked;

public class Timemeter {
    
    static final IntervalStorage STORAGE = new IntervalStorage();
    
    private static boolean trayAvailable = SystemTray.isSupported();
    
    private static CheckboxMenuItem miStartStop;
    private static TrayIcon trayIcon;
    private static Graphics2D grTray;
    private static Image imgTray;
    
    private static SummaryFrame frSummary;
    private static DetailsFrame frDetails;
    
    static SummaryFrame getSummaryFrame() {
        return frSummary;
    }
    
    static DetailsFrame getDetailsFrame() {
        return frDetails;
    }
    
    static Image getTrayImage() {
        return imgTray;
    }
    
    static boolean isTrayAvailable() {
        return trayAvailable;
    }
    
    static void setTrayToolTip(String s) {
        if (trayAvailable) trayIcon.setToolTip("Timemeter (" + s + ")");
    }
    
    static void paintTray(boolean on) {
        if (!trayAvailable) return;
        int padding = 3;
        Dimension iconSize = SystemTray.getSystemTray().getTrayIconSize();
        Ellipse2D.Float oval = new Ellipse2D.Float(padding, padding, iconSize.width - (1 + 2 * padding), iconSize.height - (1 + 2 * padding));
        grTray.setColor(on ? new Color(160, 220, 40) : new Color(240, 120, 0));
        grTray.fill(oval);
        grTray.setColor(Color.black);
        grTray.draw(oval);
        trayIcon.setImage(imgTray);
        miStartStop.setState(on);
    }
    
    public static void main(String[] args) throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("This application is not able to run without GUI.");
            return;
        }
        if (!isLocked()) setLocked(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                STORAGE.setTimer(false, false, false)
                       .setFrameVisible(frDetails == null ? STORAGE.isFrameVisible() : frDetails.isVisible(), false)
                       .save();
                if (!isLocked()) setLocked(false);
            }
            
        }));
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        if (isLocked()) {
            JOptionPane.showMessageDialog(null, "Az alkalmazás már el lett indítva.", "Figyelem!", JOptionPane.WARNING_MESSAGE);
            System.exit(1);
            return;
        }
        if (trayAvailable) {
            SystemTray tray = SystemTray.getSystemTray();
            Dimension iconSize = tray.getTrayIconSize();
            imgTray = new BufferedImage(iconSize.width, iconSize.height, BufferedImage.TYPE_INT_ARGB);
            grTray = (Graphics2D) imgTray.getGraphics();
            grTray.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{7.0f, 2.0f}, 0.0f));
            grTray.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            trayIcon = new TrayIcon(imgTray);
            setTrayToolTip(DetailsFrame.createTimeText(STORAGE.getTimeSum()));
            miStartStop = new CheckboxMenuItem("Mérés", STORAGE.isRunning());
            miStartStop.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    frDetails.startStop(!STORAGE.isRunning());
                }
                
            });
            paintTray(STORAGE.isRunning());
            trayIcon.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    frDetails.setVisible(!frDetails.isVisible());
                }
                
            });
            MenuItem miReset = new MenuItem("Nulláz");
            miReset.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    frDetails.timeReset();
                }
                
            });
            MenuItem miExit = new MenuItem("Kilép");
            miExit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
                
            });
            MenuItem miSummary = new MenuItem("Összegzés");
            miSummary.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    frSummary.setVisible(true);
                }
                
            });
            PopupMenu menu = new PopupMenu();
            menu.add(miStartStop);
            menu.add(miReset);
            menu.addSeparator();
            menu.add(miSummary);
            menu.addSeparator();
            menu.add(miExit);
            trayIcon.setPopupMenu(menu);
            try {
                tray.add(trayIcon);
            }
            catch (Exception ex) {
                trayAvailable = false;
            }
        }
        frDetails = new DetailsFrame();
        frDetails.setVisible(trayAvailable ? STORAGE.isFrameVisible() : true);
        SummaryFrame.refresh();
        frSummary = new SummaryFrame();
    }
    
}
