package timemeter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import static timemeter.Timemeter.STORAGE;

public class DetailsFrame extends Window {

    static final Rectangle MAX_RECT = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    
    private long startTime = STORAGE.getTimeSum();
    private long additionalTime = 0;
    private int tickCounter = 0;
    
    private final Timer TIMER = new Timer(1000, new ActionListener() {
        
        final int SAFE_COUNT = 600;
        
        int counter = 0;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            tickCounter++;
            additionalTime += TIMER.getDelay();
            String txt = createTimeText(startTime + additionalTime);
            LB_TIMER.setText(txt);
            Timemeter.setTrayToolTip(txt);
            if (counter % 5 == 0) {
                Timemeter.getSummaryFrame().refreshHours();
                Timemeter.getSummaryFrame().refreshCurrency();
            }
            if (++counter >= SAFE_COUNT) {
                startStop(false, true);
                startStop(true, true);
                counter = 0;
            }
        }
        
    });
    
    private final JLabel LB_TIMER = new JLabel(createTimeText(STORAGE.getTimeSum()));
    
    private final JButton BT_START_STOP = new JButton("Start") {
        {
            setFocusable(false);
            Dimension d1 = new JButton("Stop").getPreferredSize();
            Dimension d2 = getPreferredSize();
            setPreferredSize(new Dimension(Math.max(d1.width, d2.width), d2.height));
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    startStop(!STORAGE.isRunning());
                }
                
            });
        }
    };
    
    private final ActionListener AL_MENU_HIDER = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            POPUP_MENU.setVisible(false);
        }

    };
    
    private final JPopupMenu POPUP_MENU = new JPopupMenu() {
        {
            if (!Timemeter.isTrayAvailable()) {
                add(createMenuItem("Nulláz", new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        timeReset();
                    }

                }));
            }
            add(createMenuItem(Timemeter.isTrayAvailable() ? "Bezár" : "Kilép", new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (Timemeter.isTrayAvailable()) DetailsFrame.this.setVisible(false);
                    else System.exit(0);
                }
                
            }));
        }
    };
    
    private final MouseAdapter DRAG_LISTENER = new MouseAdapter() {

        Point lastPoint, lastLocation;
        int lastBtn;

        @Override
        public void mousePressed(MouseEvent e) {
            lastBtn = e.getButton();
            lastPoint = e.getLocationOnScreen();
            if (e.getButton() == MouseEvent.BUTTON3) {
                POPUP_MENU.setInvoker(e.getComponent());
                POPUP_MENU.setLocation(e.getLocationOnScreen());
                POPUP_MENU.setVisible(!POPUP_MENU.isVisible());
            }
            else {
                POPUP_MENU.setVisible(false);
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (lastLocation != null && lastBtn == MouseEvent.BUTTON1) {
                STORAGE.setFrameLocation(lastLocation, false);
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastBtn != MouseEvent.BUTTON1) return;
            Point point = e.getLocationOnScreen();
            Point diff = new Point(point.x - lastPoint.x, point.y - lastPoint.y);
            Point loc = DetailsFrame.this.getLocation();
            Point nl = new Point(loc.x + diff.x, loc.y + diff.y);
            if (isLocationCorrect(nl)) {
                lastLocation = nl;
                DetailsFrame.this.setLocation(lastLocation);
            }
            lastPoint = point;
        }
        
    };
    
    public DetailsFrame() {
        super(null);
        if (STORAGE.isRunning()) startStop(true);
        setBackground(Color.WHITE);
        addMouseListener(DRAG_LISTENER);
        addMouseMotionListener(DRAG_LISTENER);
        setAlwaysOnTop(true);
        setLayout(new FlowLayout());
        add(LB_TIMER);
        add(BT_START_STOP);
        pack();
        setLocation(STORAGE.getFrameLocation(this));
    }

    public double getAllHours() {
        return getHours(startTime + additionalTime);
    }

    public double getAdditionalHours() {
        return getHours(tickCounter * TIMER.getDelay());
    }
    
    private double getHours(long l) {
        return l / (1000.0 * 60 * 60);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        super.paint(g);
    }
    
    public boolean isLocationCorrect(Point p) {
        Dimension size = getSize();
        Rectangle nr = new Rectangle(p.x, p.y, size.width, size.height);
        return MAX_RECT.contains(nr);
    }
    
    void startStop(boolean start) {
        startStop(start, false);
    }
    
    private void startStop(boolean start, boolean safe) {
        if (!safe || start && safe) STORAGE.setTimer(start, true);
        if (start) TIMER.start();
        else TIMER.stop();
        tickCounter = 0;
        if (!safe) {
            BT_START_STOP.setText(start ? "Stop" : "Start");
            Timemeter.paintTray(start);
            SummaryFrame.refresh();
        }
    }
    
    void timeReset() {
        JFrame dummy = new JFrame();
        dummy.setIconImage(Timemeter.getTrayImage());
        if (startTime + additionalTime == 0 || JOptionPane.showOptionDialog(dummy, "Biztos, hogy nullázni akarja a számlálót?", "Megerősítés", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Igen","Nem"}, "Nem") != 0) return;
//        boolean running = STORAGE.isRunning();
        startStop(false);
        STORAGE.getIntervals().clear();
        STORAGE.save();
        startTime = additionalTime = 0;
        SummaryFrame.refresh();
        String txt = createTimeText(0);
        LB_TIMER.setText(txt);
        Timemeter.setTrayToolTip(txt);
//        if (running) startStop(true);
    }
    
    private JMenuItem createMenuItem(String s, ActionListener al) {
        final JMenuItem mi = new JMenuItem(s);
        mi.addActionListener(AL_MENU_HIDER);
        mi.addActionListener(al);
        return mi;
    }
    
    public static String createTimeText(long time) {
        long hours = time / (1000 * 60 * 60);
        time -= hours * (1000 * 60 * 60);
        long minutes = time / (1000 * 60);
        time -= minutes * (1000 * 60);
        long seconds = time / 1000;
        return String.format("%03d:%02d:%02d", hours, minutes, seconds);
    }
    
}
