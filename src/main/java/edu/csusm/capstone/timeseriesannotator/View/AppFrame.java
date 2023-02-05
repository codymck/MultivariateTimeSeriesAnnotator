package edu.csusm.capstone.timeseriesannotator.View;

import com.formdev.flatlaf.FlatLightLaf;
import edu.csusm.capstone.timeseriesannotator.Controller.*;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JColorChooser;
import java.awt.KeyboardFocusManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Cody McKinney
 */
public class AppFrame extends javax.swing.JFrame {

    public static ToolState appState;
    ChartDisplay chartDisplay;
    public static ArrayList<ChartDisplay> charts;
    public static boolean ctrlpress;

    MultiSplitPane split = new MultiSplitPane();

    /**
     * Creates new form Frame
     */
    public AppFrame() {
        this.charts = new ArrayList<>(6);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        initComponents();
        initialChart();
        setAppState(ToolState.ZOOM);

        KeyEventDispatcher toggleKeyDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_1 -> ZoomButton.doClick();
                        case KeyEvent.VK_2 -> PanButton.doClick();
                        case KeyEvent.VK_3 -> SelectButton.doClick();
                        case KeyEvent.VK_4 -> CommentButton.doClick();
                        case KeyEvent.VK_5 -> MarkerButton.doClick();
                    }
                    if(KeyEvent.CTRL_DOWN_MASK == e.getModifiersEx()){
                        System.out.println("Love you");
                        setCtrlPress(true);
                    }
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if(KeyEvent.CTRL_DOWN_MASK == e.getModifiersEx()){
                        setCtrlPress(false);
                    }
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(toggleKeyDispatcher);

        this.setLocationRelativeTo(null);
    }

    // Creates an empty chart on instantiation of program
    public void initialChart() {
        chartDisplay = new ChartDisplay(this);
        charts.add(chartDisplay);
        split.addComponent(chartDisplay);
        jPanel1.removeAll();
        jPanel1.add(split);
        validate();
        repaint();
    }

    // Adds additional charts to the panel
    public void addChart(ArrayList<ChartDisplay> c) {
        this.charts = c;
        chartDisplay = charts.get(charts.size() - 1);
    }

    // Removes the ChartDisplay from the panel
    public void removeChart(ChartDisplay c) {
        split.removeComponent(c);
        charts.remove(c);
        // jPanel1.remove(c);
        validate();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importChooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ZoomButton = new javax.swing.JToggleButton();
        PanButton = new javax.swing.JToggleButton();
        SelectButton = new javax.swing.JToggleButton();
        CommentButton = new javax.swing.JToggleButton();
        MarkerButton = new javax.swing.JToggleButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        importDataMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenu();
        AddChartMenuItem = new javax.swing.JMenuItem();
        HighlightColor = new javax.swing.JMenuItem();

        importChooser.setCurrentDirectory(new java.io.File("./dataFiles"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multivariate Time Series Annotator");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(0, 0, 1000, 800));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 800));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel2.setMaximumSize(new java.awt.Dimension(55, 55));
        jPanel2.setMinimumSize(new java.awt.Dimension(55, 55));

        buttonGroup1.add(ZoomButton);
        ZoomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/zoomin.png"))); // NOI18N
        ZoomButton.setSelected(true);
        ZoomButton.setToolTipText("Zoom (Press 1)");
        ZoomButton.setRolloverEnabled(false);
        ZoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(PanButton);
        PanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/4dirs.png"))); // NOI18N
        PanButton.setMnemonic('W');
        PanButton.setToolTipText("Move Tool (Press 2)");
        PanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PanButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(SelectButton);
        SelectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/select.png"))); // NOI18N
        SelectButton.setMnemonic('E');
        SelectButton.setToolTipText("Select Tool (Press 3)");
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(CommentButton);
        CommentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/annotate.png"))); // NOI18N
        CommentButton.setMnemonic('R');
        CommentButton.setToolTipText("Comment Tool (Press 4)");
        CommentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(MarkerButton);
        MarkerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shapes.jpeg"))); // NOI18N
        MarkerButton.setMnemonic('T');
        MarkerButton.setToolTipText("Marker Tool (Press 5)");
        MarkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MarkerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ZoomButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SelectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CommentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MarkerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 767, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ZoomButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(PanButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(MarkerButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(SelectButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(CommentButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        menuBar.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        fileMenuItem.setText("File");

        importDataMenuItem.setText("Import Data");
        importDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDataMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(importDataMenuItem);

        menuBar.add(fileMenuItem);

        editMenuItem.setText("Edit");
        menuBar.add(editMenuItem);

        optionsMenuItem.setText("Options");

        AddChartMenuItem.setText("Add Chart");
        AddChartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddChartMenuItemActionPerformed(evt);
            }
        });
        optionsMenuItem.add(AddChartMenuItem);

        HighlightColor.setText("Change Highlight Color");
        HighlightColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HighlightColorActionPerformed(evt);
            }
        });
        optionsMenuItem.add(HighlightColor);

        menuBar.add(optionsMenuItem);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HighlightColorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_HighlightColorActionPerformed
        Color color = JColorChooser.showDialog(this,
                "Select a color", new Color(0, 100, 255, 60));
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }

    }// GEN-LAST:event_HighlightColorActionPerformed

    private void AddChartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RemoveChartMenuItemActionPerformed
        ActionListener addChart = new AddChartAction(split, charts, this);
        addChart.actionPerformed(evt);
    }// GEN-LAST:event_RemoveChartMenuItemActionPerformed

    private void importDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_importDataMenuItemActionPerformed
        ActionListener importAction = new ImportDataAction(importChooser, chartDisplay);
        importAction.actionPerformed(evt);
    }// GEN-LAST:event_importDataMenuItemActionPerformed

    private void ZoomButtonActionPerformed(java.awt.event.ActionEvent evt) {
        AbstractButton aB = (AbstractButton) evt.getSource();
        boolean zSelected = aB.getModel().isSelected();

        setAppState(ToolState.ZOOM);

        if (zSelected) {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).emptyChart.setZoomOutlinePaint(Color.BLACK);
                charts.get(i).emptyChart.setChartState(ToolState.ZOOM);
            }
        }
    }

    private void PanButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setAppState(ToolState.PAN);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.PAN);
        }
    }

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setAppState(ToolState.HIGHLIGHT);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.HIGHLIGHT);
        }
    }

    private void CommentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setAppState(ToolState.COMMENT);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.COMMENT);
        }
    }

    private void MarkerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setAppState(ToolState.MARK);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.MARK);
        }
    }

    private void setAppState(ToolState s) {
        appState = s;
    }

    public static ToolState getAppState() {
        return appState;
    }
    
    public void setCtrlPress(boolean c){
        ctrlpress = c;
    }
    
    public static boolean getCtrlPress(){
        return ctrlpress;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddChartMenuItem;
    private javax.swing.JToggleButton CommentButton;
    private javax.swing.JMenuItem HighlightColor;
    private javax.swing.JToggleButton MarkerButton;
    private javax.swing.JToggleButton PanButton;
    private javax.swing.JToggleButton SelectButton;
    private javax.swing.JToggleButton ZoomButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JFileChooser importChooser;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenuItem;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JMenuItem getImportButton() {
        return importDataMenuItem;
    }

    public javax.swing.JFileChooser getImportChooser() {
        return importChooser;
    }
    

}