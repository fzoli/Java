package org.dyndns.fzoli.radioinfo.desktop;

import org.dyndns.fzoli.radioinfo.RadioInfo;
import org.dyndns.fzoli.radioinfo.Music;
import org.dyndns.fzoli.radioinfo.RadioInfoLoader;
import org.dyndns.fzoli.radioinfo.RadioInfoView;
import org.dyndns.fzoli.radioinfo.desktop.resource.R;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 *
 * @author zoli
 */
public abstract class RadioInfoForm implements RadioInfoView {

    private Display display;
    private Shell shell;
    
    private ToolItem tiRefresh;
    private ToolItem tiSave;
    
    private Composite center;
    private Label lbArtistValue;
    private Label lbAddressValue;
    
    private Label lbMsg;
    private ProgressBar progressBar;

    private final RadioInfoLoader LOADER;

    public RadioInfoForm() {
        this("music.txt");
    }
    
    public RadioInfoForm(String storeFileName) {
        LOADER = createLoader(storeFileName);
        createContents();
        refresh();
    }

    public void open() {
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        interrupt();
    }

    @Override
    public void setStatus(final Status status) {
        if (status == null || shell.isDisposed()) return;
        final String key;
        switch (status) {
            case LOADING:
                key = R.KEY_LOADING;
                break;
            case INTERRUPTED:
                key = R.KEY_INTERRUPTED;
                break;
            case SUCCESS:
                key = R.KEY_SUCCESS;
                break;
            case CHANGED:
                key = R.KEY_CHANGED;
                break;
            case NO_DATA:
                key = R.KEY_NO_DATA;
                break;
            default:
                key = R.KEY_UNAVAILABLE;
        }
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                center.setVisible(status == Status.SUCCESS);
                progressBar.setVisible(status == Status.LOADING);
                if (status == Status.LOADING) {
                    tiRefresh.setEnabled(false);
                    tiSave.setEnabled(false);
                }
                else {
                    tiRefresh.setEnabled(true);
                }
                lbMsg.setText(R.getValue(key));
                relayout();
            }
            
        });
    }

    @Override
    public void setRadioInfo(final RadioInfo info) {
        if (shell.isDisposed()) return;
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                if (info != null && info.isValid()) {
                    Music music = info.getMusic();
                    setText(lbArtistValue, music.getArtist());
                    setText(lbAddressValue, music.getAddress());
                    tiSave.setEnabled(info.isSaveAvailable() && !info.isSaved());
                }
                else {
                    lbArtistValue.setText("-");
                    lbAddressValue.setText("-");
                    tiSave.setEnabled(false);
                }
                relayout();
            }
            
        });
    }

    private void setText(Label lb, String txt) {
        lb.setText(txt.replace("&", "&&"));
    }
    
    private void save() {
        if (LOADER != null && LOADER.getRadioInfo() != null) {
            if (LOADER.getRadioInfo().save()) tiSave.setEnabled(false);
            else MessageDialog.openError(shell, R.getValue(R.KEY_ERROR), R.getValue(R.KEY_SAVE_ERROR));
        }
    }
    
    private void interrupt() {
        if (LOADER != null) LOADER.interrupt();
    }
    
    private void refresh() {
        if (LOADER != null) LOADER.loadRadioInfo();
    }
    
    private void relayout() {
        shell.layout();
        center.layout();
        Point oldSize = shell.getSize();
        Point reqSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        shell.setMinimumSize(reqSize);
        shell.setSize(new Point(Math.max(oldSize.x, reqSize.x), Math.max(oldSize.y, reqSize.y)));
    }
    
    protected abstract RadioInfoLoader createLoader(String storeFileName);
    
    private void createContents() {
        Display.setAppName(R.APP_NAME);
        display = Display.getDefault();
        shell = new Shell(display);
        shell.setText(R.APP_NAME);
        shell.setImage(new Image(display, R.getStream(R.FILE_ICON)));

        shell.addListener(SWT.KeyDown, new Listener() {

            @Override
            public void handleEvent(Event e) {
                switch (e.keyCode) {
                    case SWT.ESC:
                        interrupt();
                        break;
                    case SWT.F5:
                        refresh();
                        break;
                    case 's':
                        if ((e.stateMask & SWT.CTRL) != 0) save();
                        break;
                }
            }
            
        });
        
        GridLayout glSh = new GridLayout(1, false);
        glSh.marginWidth = 0;
        glSh.marginHeight = 0;
        glSh.horizontalSpacing = 0;
        glSh.verticalSpacing = 0;

        shell.setLayout(glSh);

        ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT | SWT.NO_FOCUS);
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        tiSave = new ToolItem(toolBar, SWT.PUSH);
        tiSave.setImage(new Image(display, R.getStream(R.FILE_SAVE)));
        tiSave.setToolTipText(R.getValue(R.KEY_SAVE_TIP));
        
        tiSave.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                save();
            }
            
        });
        
        new ToolItem(toolBar, SWT.SEPARATOR);

        tiRefresh = new ToolItem(toolBar, SWT.PUSH);
        tiRefresh.setImage(new Image(display, R.getStream(R.FILE_REFRESH)));
        tiRefresh.setToolTipText(R.getValue(R.KEY_REFRESH_TIP));
        
        tiRefresh.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                refresh();
            }
            
        });

        new Label(shell, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        center = new Composite(shell, SWT.NONE);
        center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        center.setLayout(new GridLayout(2, false));

        Label lbArtist = new Label(center, SWT.NONE);
        lbArtist.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
        lbArtist.setText(R.getValue(R.KEY_ARTIST) + ":");

        lbArtistValue = new Label(center, SWT.NONE);
        lbArtistValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
        lbArtistValue.setText("-");

        Label lbAddress = new Label(center, SWT.NONE);
        lbAddress.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
        lbAddress.setText(R.getValue(R.KEY_ADDRESS) + ":");

        lbAddressValue = new Label(center, SWT.NONE);
        lbAddressValue.setText("-");

        new Label(shell, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Composite statusbar = new Composite(shell, SWT.NONE);
        statusbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        statusbar.setLayout(new GridLayout(2, false));

        lbMsg = new Label(statusbar, SWT.NONE);
        lbMsg.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        lbMsg.setText(R.getValue(R.KEY_LOADING));

        GridData gdPb = new GridData();
        gdPb.widthHint = 50;
        gdPb.horizontalIndent = 10;

        progressBar = new ProgressBar(statusbar, SWT.INDETERMINATE);
        progressBar.setLayoutData(gdPb);

        shell.pack();
        shell.setMinimumSize(shell.getSize());
    }
    
}
