/*
 * TApp.java
 *
 * Created on April 3, 2007, 2:35 PM
 */

package cotex;

//look and feel
import net.infonode.gui.laf.*;

// docking util
import net.infonode.docking.*;
import net.infonode.docking.util.*;
import net.infonode.docking.theme.*;
import net.infonode.docking.properties.*;
import net.infonode.util.Direction;

import java.util.List;
import java.util.Iterator;

/**
 *
 * @author  Ming
 */
public class TApp extends javax.swing.JFrame {
    
    /** Creates new form TApp */
    public TApp() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        mNode.getModel().execute( new TExitCmd() );
    }//GEN-LAST:event_formWindowClosing

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private TNode mNode;
    private TConfig mConfig;
    private TConnectionManager mConnectionMgr;
    private TConsolePanel mConsole;
    
    private static final javax.swing.Icon DEFAULT_ICON = new javax.swing.Icon() {
        
        public final int ICON_SIZE = 4;
        
        public int getIconHeight() {
            return ICON_SIZE;
        }
        
        public int getIconWidth() {
            return ICON_SIZE;
        }
        
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            //java.awt.Color oldColor = g.getColor();
            
            //g.setColor(java.awt.Color.BLACK);
            //g.fillOval(x, y, ICON_SIZE, ICON_SIZE);
            
            //g.setColor(oldColor);
        }
    };
    
    public void run() {
        try {
            initLoggers();

            mConfig = new TConfig("cotex.config.xml");

            mConnectionMgr = new TConnectionManager( mConfig.getSetting("General", "ConnectionType") );

            mNode = TNodeFactory.createInstance( mConfig.getSetting("General", "NodeType") );

            initGui();
            
            initLookAndFeel();
            
            this.setVisible(true);
        
            TLogManager.logMessage("Application started");
        }
        catch(TException e) {
            
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Failed to startup Cotex\nReason:\n" + e.getMessage() );
            
            System.exit(1);
            
        }
        catch(Exception e) {
            
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Failed to startup Cotex\n Unknown exception\n" + e.toString() );
            
            System.exit(1);
        } 
    }
    
    private void initLoggers() {
        
        mConsole = new TConsolePanel();
        
        TLogManager.addLogger(mConsole);
        
    }

    private void initGui() {
        
        this.setTitle("Cotex");
        this.setResizable(true);
        this.setSize(800, 600);
        
        java.util.List components = mNode.getView().getGuiComponents();
        
        Iterator iter = components.iterator();
        
        StringViewMap dockingViewMap = new StringViewMap();
        
        java.util.ArrayList<View> views = new java.util.ArrayList<View>();
        
        while( iter.hasNext() ) {
         
            javax.swing.JComponent comp = (javax.swing.JComponent)iter.next();
            
            View dockingView = new View(comp.getName(), DEFAULT_ICON, comp);
            
            dockingView.getWindowProperties().setCloseEnabled(false);
            dockingView.getWindowProperties().setDragEnabled(false);
            //dockingView.getWindowProperties().setMinimizeEnabled(false);
            dockingView.getWindowProperties().setUndockEnabled(false);
            
            dockingViewMap.addView( comp.getName(), dockingView );
            
            views.add(dockingView);
        }
        
        View consoleView = new View(mConsole.getName(), DEFAULT_ICON, mConsole);
        dockingViewMap.addView(mConsole.getName(), consoleView );
        
        RootWindow dockingRootWindow = DockingUtil.createRootWindow(dockingViewMap, false);
        dockingRootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        dockingRootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        
        // apply docking theme
        DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        //DockingWindowsTheme theme = new ClassicDockingTheme();
        
        dockingRootWindow.getRootWindowProperties().addSuperObject( theme.getRootWindowProperties() );
        
        // enable title bar style
        RootWindowProperties titleBarStyleProperties =
            PropertiesUtil.createTitleBarStyleRootWindowProperties();
        
        dockingRootWindow.getRootWindowProperties().addSuperObject(
            titleBarStyleProperties);
        
        // add to main frame
        this.getContentPane().add(dockingRootWindow, java.awt.BorderLayout.CENTER);
        
        // init layout
        boolean retry = true;
        
        while(retry) {
            
            try {

               DockingWindow currWnd = views.get(0);

               for(int i=1; i<views.size(); ++i) {

                    currWnd = new SplitWindow(
                        true,
                        0.3f,
                        currWnd,
                        views.get(i) );

                }

                DockingWindow layout = new SplitWindow(
                    false,
                    0.8f,
                    currWnd,
                    consoleView );

                dockingRootWindow.setWindow(layout);
                
                retry = false;
                
            }
            catch (Exception e) {
                //TLogManager.logError( e.toString() );
                retry = true;
            }
        }
    }

    private void initLookAndFeel() {
        
        try {
            
            ///*
            InfoNodeLookAndFeelTheme theme = InfoNodeLookAndFeelThemes.getDarkBlueGreenTheme();
            
            theme.setBackgroundColor( new java.awt.Color(144, 144, 144) );
            theme.setSelectedMenuBackgroundColor( new java.awt.Color(96, 96, 96) );
            theme.setSelectedTextBackgroundColor( new java.awt.Color(96, 96, 96) );
            
            theme.setControlColor( new java.awt.Color(128, 128, 128) );
            theme.setShadingFactor( 0.25f );
            
            //theme.setActiveInternalFrameTitleGradientColor(  theme.getInactiveInternalFrameTitleGradientColor() );
            //theme.setActiveInternalFrameTitleBackgroundColor( new java.awt.Color(96, 96, 96) );
            /**/
            
            //InfoNodeLookAndFeelTheme theme = InfoNodeLookAndFeelThemes.getBlueIceTheme();
            
            javax.swing.UIManager.setLookAndFeel(
                new InfoNodeLookAndFeel(theme) );
            
                    
            //javax.swing.UIManager.setLookAndFeel(
            //    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
            
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
            
        }
        catch(Exception e) {
            
            TLogManager.logError("TApp: initLookAndFeel failed, reason: " + e.toString() ); 
            
        }
    }
}
