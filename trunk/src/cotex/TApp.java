/*
 * TApp.java
 *
 * Created on April 3, 2007, 2:35 PM
 */

package cotex;

//look and feel
import java.awt.Graphics;
import net.infonode.gui.laf.*;

// docking util
import net.infonode.docking.*;
import net.infonode.docking.util.*;
import net.infonode.docking.theme.*;
import net.infonode.docking.properties.*;
import net.infonode.util.Direction;

import java.util.List;
import java.util.Iterator;

import java.awt.Color;
import java.awt.SystemColor;

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

        mNode.execute( new TExitCmd() );
    }//GEN-LAST:event_formWindowClosing

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private TNode mNode;
    private TConfig mConfig;
    private TConsolePanel mConsole;
    private RootWindow mDockingRootWindow;
    
    private java.util.ArrayList<View> mDockingViews;
    private View mConsoleDockingView;
    
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
            
            //g.setColor(java.awt.Color.WHITE);
            //g.fillOval(x, y, ICON_SIZE, ICON_SIZE);
            
            
            //g.setColor(oldColor);
        }
    };
    
    public void run() {
        try {
            initLoggers();

            mConfig = new TConfig("cotex.config.xml");
            
            mNode = TNodeFactory.createInstance( mConfig.getSetting("General", "NodeType"), mConfig );

            initGui();
            
            initLayout();
            
            initLookAndFeel();
            
            this.setVisible(true);
            
            mNode.getModel().startUp();
            mNode.getView().startUp();
        
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
                "Failed to startup Cotex\n Unknown exception\n" + e.getStackTrace() );
            
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
        
        mDockingViews = new java.util.ArrayList<View>();
        
        while( iter.hasNext() ) {
         
            javax.swing.JComponent comp = (javax.swing.JComponent)iter.next();
            
            View dockingView = new View(comp.getName(), DEFAULT_ICON, comp);
            
            dockingView.getWindowProperties().setCloseEnabled(false);
            dockingView.getWindowProperties().setDragEnabled(false);
            //dockingView.getWindowProperties().setMinimizeEnabled(false);
            dockingView.getWindowProperties().setUndockEnabled(false);
            
            dockingViewMap.addView( comp.getName(), dockingView );
            
            mDockingViews.add(dockingView);
        }
        
        mConsoleDockingView = new View(mConsole.getName(), DEFAULT_ICON, mConsole);
        dockingViewMap.addView(mConsole.getName(), mConsoleDockingView );
        
        mDockingRootWindow = DockingUtil.createRootWindow(dockingViewMap, false);
        mDockingRootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        mDockingRootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        
        // add to main frame
        this.getContentPane().add(mDockingRootWindow, java.awt.BorderLayout.CENTER);
    }

    private void initLayout() {
        
        // init layout
        boolean retry = true;
        
        while(retry) {
            
            try {

               DockingWindow currWnd = mDockingViews.get(0);

               for(int i=1; i<mDockingViews.size(); ++i) {

                    currWnd = new SplitWindow(
                        true,
                        0.3f,
                        currWnd,
                        mDockingViews.get(i) );

                }

                DockingWindow layout = new SplitWindow(
                    false,
                    0.6f,
                    currWnd,
                    mConsoleDockingView );

                mDockingRootWindow.setWindow(layout);
                
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
        
            // apply docking theme
            DockingWindowsTheme dockingTheme = new ShapedGradientDockingTheme();
            //DockingWindowsTheme dockingTheme = new ClassicDockingTheme();

            mDockingRootWindow.getRootWindowProperties().addSuperObject( dockingTheme.getRootWindowProperties() );

            // enable title bar style
            RootWindowProperties titleBarStyleProperties =
                PropertiesUtil.createTitleBarStyleRootWindowProperties();

            mDockingRootWindow.getRootWindowProperties().addSuperObject(
                titleBarStyleProperties);

            ///*
            InfoNodeLookAndFeelTheme lnfTheme = InfoNodeLookAndFeelThemes.getDarkBlueGreenTheme();
            
            lnfTheme.setBackgroundColor( new Color(144, 144, 144) );
            lnfTheme.setSelectedMenuBackgroundColor( new Color(96, 96, 96) );
            lnfTheme.setSelectedTextBackgroundColor( new Color(96, 96, 96) );
            
            lnfTheme.setControlColor( new Color(110, 110, 110) );
            //lnfTheme.setControlColor( SystemColor.control );
            lnfTheme.setShadingFactor( 0.25f );
            
            //TLogManager.logMessage("bg = " + lnfTheme.getInactiveInternalFrameTitleBackgroundColor().toString() );
            //TLogManager.logMessage("grad = " + lnfTheme.getInactiveInternalFrameTitleGradientColor().toString() );
            
            lnfTheme.setActiveInternalFrameTitleGradientColor( new Color(160, 160, 160) );
            lnfTheme.setActiveInternalFrameTitleBackgroundColor( new Color(96, 96, 96) );
            
            lnfTheme.setInactiveInternalFrameTitleGradientColor( new Color(120, 120, 120) );
            lnfTheme.setInactiveInternalFrameTitleBackgroundColor( new Color(96, 96, 96) );
            /**/
            
            //InfoNodeLookAndFeelTheme theme = InfoNodeLookAndFeelThemes.getBlueIceTheme();
            
            javax.swing.UIManager.setLookAndFeel(
                new InfoNodeLookAndFeel(lnfTheme) );
            
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
            
        }
        catch(Exception e) {
            
            TLogManager.logError("TApp: initLookAndFeel failed, reason: " + e.toString() ); 
            
        }
    }
}
