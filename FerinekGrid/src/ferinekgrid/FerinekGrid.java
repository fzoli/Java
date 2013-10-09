package ferinekgrid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

/**
 * Példa a barátomnak a GridLayout használatára az amőba játékon keresztül.
 */
public class FerinekGrid extends JFrame {
    
    public static enum Symbol {EMPTY, X, O}
    
    private class GridLabel extends JLabel {
        
        private static final int LINE_WIDTH = 1;
        private static final double PADDING = 0.08;
        
        private Symbol symbol = Symbol.EMPTY;
        
        private final MouseListener mouseListener = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                refreshCursor();
            }
            
        };
        
        public GridLabel(int x, int y) {
//            setText(" " + (x+1) + ";" + (y+1) + " ");
//            setFont(new Font(getFont().getName(), getFont().getStyle(), 8));
            setForeground(Color.GRAY);
            setHorizontalAlignment(CENTER);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, LINE_WIDTH));
            setOpaque(true);
            addMouseListener(mouseListener);
        }

        public Symbol getSymbol() {
            return symbol;
        }
        
        public void setSymbol(Symbol s) {
            if (s == null) s = Symbol.EMPTY;
            symbol = s;
            repaint();
            refreshCursor();
        }
        
        private void refreshCursor() {
            if (symbol == Symbol.EMPTY && !finished) setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else setCursor(Cursor.getDefaultCursor());
        }
        
        private Rectangle getPaintArea() {
            return new Rectangle(LINE_WIDTH + (int)(getWidth() * PADDING), LINE_WIDTH + (int)(getHeight() * PADDING), getWidth()-(2 * LINE_WIDTH + 1) - (int)(2 * getWidth() * PADDING), getHeight() - (2 * LINE_WIDTH + 1) - (int)(2 * getHeight() * PADDING));
        }
        
        private void paintSymbol(Graphics2D g, Rectangle area) {
            if (symbol == Symbol.EMPTY) return;
            
            Object defAnt = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Stroke defStroke = g.getStroke();
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(LINE_WIDTH * 2 + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            
            switch (symbol) {
                case X:
                    paintSymbolX(g, area);
                    break;
                case O:
                    paintSymbolO(g, area);
            }
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, defAnt);
            g.setStroke(defStroke);
        }
        
        private void paintSymbolX(Graphics2D g, Rectangle area) {
            g.setColor(Color.RED);
            g.drawLine(area.x, area.y, area.width + area.x, area.height + area.y);
            g.drawLine(area.x, area.height + area.y, area.width + area.x, area.y);
        }
        
        private void paintSymbolO(Graphics2D g, Rectangle area) {
            g.setColor(Color.BLUE);
            g.drawOval(area.x, area.y, area.width, area.height);
        }
        
        @Override
        public void paintComponent(Graphics bg) {
            super.paintComponent(bg);
            Graphics2D g = (Graphics2D) bg;
            Rectangle area = getPaintArea();
            paintSymbol(g, area);
        }
        
    }
    
    private int stepCounter = 0;
    
    private Symbol winner;
    private boolean finished = false;
    
    /**
     * NxN-es rács mérete.
     */
    private final int GRIDS = 15;
    
    /**
     * Nyeréshez egymás mellett ennyi azonos színű jelnek kell lenni.
     */
    private final int WIN_COUNT = 5;
    
    private final GridLabel[][] LABELS = new GridLabel[GRIDS][GRIDS];
    private final List<Point[]> LINE_GROUPS = createLineIndexGroups();
    
    private final JPanel GRID_PANEL = new JPanel() {
        
        {
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setLayout(new GridLayout(GRIDS, GRIDS));
            for (int y = 0; y < GRIDS; y++) {
                for (int x = 0; x < GRIDS; x++) {
                    final GridLabel lb = new GridLabel(x, y);
                    LABELS[x][y] = lb;
                    add(lb);
                    lb.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent me) {
                            step(lb);
                        }

                    });
                }
            }
        }
        
        @Override
        public Dimension getPreferredSize() {
            Container parent = getParent();
            if (parent == null) parent = FerinekGrid.this.getContentPane();
            Dimension parentSize = parent.getSize();
            int size = Math.min(parentSize.width, parentSize.height);
            return new Dimension(size, size);
        }
        
    };
    
    private final JPanel GRID_PARENT_PANEL = new JPanel() {
        {
            setLayout(new GridBagLayout());
            add(GRID_PANEL, new GridBagConstraints());
        }
    };
    
    
    private final JLabel LB_MSG = new JLabel(), LB_STEPS = new JLabel();
    
    private final JButton BT_RESTART = new JButton("Újrakezd") {
        {
            setFocusable(false);
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    restart();
                }
                
            });
        }
    };
    
    private final JToolBar TOOL_BAR = new JToolBar() {
        {
            updateToolBar();
            setFloatable(false);
            add(BT_RESTART);
            addSeparator();
            add(LB_STEPS);
            addSeparator();
            add(LB_MSG);
        }
    };
    
    public FerinekGrid(int defSize) {
        setTitle("Amőba");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(TOOL_BAR, c);
        
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        add(GRID_PARENT_PANEL, c);
        
        GRID_PARENT_PANEL.setPreferredSize(new Dimension(defSize, defSize));
        pack();
    }
    
    private List<Point[]> createLineIndexGroups() {
        List<Point[]> lines = new ArrayList<>();
        boolean reverse = false;
        int stp = 1, rcnt = 0, scnt = 0;
        while (stp != 0) {
            if (stp >= WIN_COUNT) {
                Point[] line1 = new Point[stp];
                Point[] line2 = new Point[stp];
                for (int i = 0; i < stp; i++) {
                    line1[i] = new Point(!reverse ? i : i + rcnt, reverse ? i : GRIDS - stp + i);
                    line2[i] = new Point(!reverse ? i : i + rcnt, !reverse ? scnt-i : GRIDS - i - 1);
                }
                lines.add(line1);
                lines.add(line2);
            }
            if (stp == GRIDS) reverse = true;
            if (reverse) rcnt++;
            else scnt++;
            stp += reverse ? -1 : 1;
        }
        for (int i = 0; i < GRIDS; i++) {
            Point[] line1 = new Point[GRIDS];
            Point[] line2 = new Point[GRIDS];
            for (int j = 0; j < GRIDS; j++) {
                line1[j] = new Point(i, j);
                line2[j] = new Point(j, i);
            }
            lines.add(line1);
            lines.add(line2);
        }
        return lines;
    }
    
    private boolean chkWinner(Symbol s) {
        int c;
        for (Point[] line : LINE_GROUPS) {
            c = 0;
            for (Point p : line) {
                GridLabel lb = LABELS[p.x][p.y];
                if (lb.getSymbol() == s) {
                    c++;
                    if (c >= WIN_COUNT) return true;
                }
                else {
                    c = 0;
                }
            }
        }
        return false;
    }
    
    private void updateToolBar() {
        String msg;
        if (finished) {
            if (winner == null) msg = "Döntetlen";
            else msg = winner + " nyert";
        }
        else {
            msg = (stepCounter % 2 != 0 ? Symbol.O : Symbol.X) + " következik";
        }
        LB_MSG.setText(msg + '.');
        LB_STEPS.setText(Integer.toString(stepCounter));
    }
    
    private void step(GridLabel lb) {
        synchronized (STEP_LOCK) {
            if (finished) return;
            if (lb.getSymbol() != Symbol.EMPTY) {
                SoundUtils.tone(400,100);
            }
            else {
                stepCounter++;
                lb.setSymbol(stepCounter % 2 == 0 ? Symbol.O : Symbol.X);
                boolean oWinner = chkWinner(Symbol.O);
                boolean xWinner = !oWinner && chkWinner(Symbol.X);
                if (oWinner || xWinner) {
                    if (oWinner) winner = Symbol.O;
                    else winner = Symbol.X;
                    finished = true;
                    SoundUtils.tone(1400,100);
                }
                else {
                    boolean hasEmpty = false;
                    for (int y = 0; y < GRIDS; y++) {
                        for (int x = 0; x < GRIDS; x++) {
                            if (LABELS[x][y].getSymbol() == Symbol.EMPTY) {
                                hasEmpty = true;
                                break;
                            }
                        }
                    }
                    if (!hasEmpty) {
                        finished = true;
                    }
                }
                updateToolBar();
            }
        }
    }
    
    private void restart() {
        synchronized (STEP_LOCK) {
            stepCounter = 0;
            winner = null;
            finished = false;
            for (int y = 0; y < GRIDS; y++) {
                for (int x = 0; x < GRIDS; x++) {
                    LABELS[x][y].setSymbol(Symbol.EMPTY);
                }
            }
            updateToolBar();
        }
    }
    
    /**
     * Megakadályozza, hogy az újrakezdés művelet alatt léphessen a felhasználó.
     */
    private final Object STEP_LOCK = new Object();
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new FerinekGrid(400).setVisible(true);
    }
    
}
