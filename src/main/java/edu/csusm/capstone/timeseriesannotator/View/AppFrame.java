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
    public static boolean ctrlpress = false;

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
                        case KeyEvent.VK_1:
                            ZoomButton.doClick();
                            break;
                        case KeyEvent.VK_2:
                            PanButton.doClick();
                            break;
                        case KeyEvent.VK_3:
                                SelectButton.doClick();
                                break;
                        case KeyEvent.VK_4:
                            CommentButton.doClick();
                            break;
                        case KeyEvent.VK_5:
                            MarkerButton.doClick();
                            break;
                        case KeyEvent.VK_CONTROL:
                            if(!PanButton.isSelected())
                                PanButton.doClick();
                            break;
                    }
//                    if(KeyEvent.CTRL_DOWN_MASK == e.getModifiersEx()){
//                        System.out.println("Love you");
//                        setCtrlPress(true);
//                    }
                }
                if(e.getID() == KeyEvent.KEY_RELEASED) {
                    if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                        selectedButton.doClick();
                    }
                }
//                if (e.getID() == KeyEvent.KEY_RELEASED) {
//                    if(KeyEvent.CTRL_DOWN_MASK == e.getModifiersEx()){
//                        setCtrlPress(false);
//                    }
//                }
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ZoomButton = new javax.swing.JToggleButton();
        PanButton = new javax.swing.JToggleButton();
        SelectButton = new javax.swing.JToggleButton();
        CommentButton = new javax.swing.JToggleButton();
        MarkerButton = new javax.swing.JToggleButton();
        panel1 = new java.awt.Panel();
        RedButton = new javax.swing.JToggleButton();
        OrangeButton = new javax.swing.JToggleButton();
        YellowButton = new javax.swing.JToggleButton();
        GreenButton = new javax.swing.JToggleButton();
        BlueButton = new javax.swing.JToggleButton();
        PurpleButton = new javax.swing.JToggleButton();
        panel2 = new java.awt.Panel();
        SquareButton = new javax.swing.JToggleButton();
        CircleButton = new javax.swing.JToggleButton();
        StarButton = new javax.swing.JToggleButton();
        DiagonalButton = new javax.swing.JToggleButton();
        HorizontalButton = new javax.swing.JToggleButton();
        Vertical = new javax.swing.JToggleButton();
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
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup1.add(ZoomButton);
        ZoomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/zoomin.png"))); // NOI18N
        ZoomButton.setSelected(true);
        ZoomButton.setToolTipText("Zoom (Press 1)");
        ZoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomButtonActionPerformed(evt);
            }
        });
        jPanel2.add(ZoomButton);

        buttonGroup1.add(PanButton);
        PanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/4dirs.png"))); // NOI18N
        PanButton.setMnemonic('W');
        PanButton.setToolTipText("Move Tool (Press 2)");
        PanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PanButtonActionPerformed(evt);
            }
        });
        jPanel2.add(PanButton);

        buttonGroup1.add(SelectButton);
        SelectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/select.png"))); // NOI18N
        SelectButton.setMnemonic('E');
        SelectButton.setToolTipText("Select Tool (Press 3)");
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });
        jPanel2.add(SelectButton);

        buttonGroup1.add(CommentButton);
        CommentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-comment-medical-32.png"))); // NOI18N
        CommentButton.setMnemonic('R');
        CommentButton.setToolTipText("Comment Tool (Press 4)");
        CommentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentButtonActionPerformed(evt);
            }
        });
        jPanel2.add(CommentButton);

        buttonGroup1.add(MarkerButton);
        MarkerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-diversity-50.png"))); // NOI18N
        MarkerButton.setMnemonic('T');
        MarkerButton.setToolTipText("Marker Tool (Press 5)");
        MarkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MarkerButtonActionPerformed(evt);
            }
        });
        jPanel2.add(MarkerButton);

        panel1.setVisible(false);
        panel1.setLayout(new java.awt.GridLayout(2, 0));

        RedButton.setBackground(new java.awt.Color(255, 51, 51));
        buttonGroup2.add(RedButton);
        RedButton.setSelected(true);
        RedButton.setDoubleBuffered(true);
        RedButton.setPreferredSize(new java.awt.Dimension(20, 20));
        RedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedButtonActionPerformed(evt);
            }
        });
        panel1.add(RedButton);
        RedButton.getAccessibleContext().setAccessibleName("RedButton");

        OrangeButton.setBackground(new java.awt.Color(255, 153, 0));
        buttonGroup2.add(OrangeButton);
        OrangeButton.setDoubleBuffered(true);
        OrangeButton.setPreferredSize(new java.awt.Dimension(20, 20));
        OrangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrangeButtonActionPerformed(evt);
            }
        });
        panel1.add(OrangeButton);

        YellowButton.setBackground(new java.awt.Color(255, 255, 51));
        buttonGroup2.add(YellowButton);
        YellowButton.setDoubleBuffered(true);
        YellowButton.setPreferredSize(new java.awt.Dimension(20, 20));
        YellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YellowButtonActionPerformed(evt);
            }
        });
        panel1.add(YellowButton);

        GreenButton.setBackground(new java.awt.Color(0, 255, 0));
        buttonGroup2.add(GreenButton);
        GreenButton.setDoubleBuffered(true);
        GreenButton.setPreferredSize(new java.awt.Dimension(20, 20));
        GreenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GreenButtonActionPerformed(evt);
            }
        });
        panel1.add(GreenButton);

        BlueButton.setBackground(new java.awt.Color(0, 102, 255));
        buttonGroup2.add(BlueButton);
        BlueButton.setDoubleBuffered(true);
        BlueButton.setPreferredSize(new java.awt.Dimension(20, 20));
        BlueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlueButtonActionPerformed(evt);
            }
        });
        panel1.add(BlueButton);

        PurpleButton.setBackground(new java.awt.Color(153, 0, 255));
        buttonGroup2.add(PurpleButton);
        PurpleButton.setDoubleBuffered(true);
        PurpleButton.setPreferredSize(new java.awt.Dimension(20, 20));
        PurpleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurpleButtonActionPerformed(evt);
            }
        });
        panel1.add(PurpleButton);

        jPanel2.add(panel1);

        panel2.setVisible(false);
        panel2.setLayout(new java.awt.GridLayout(2, 0));

        buttonGroup3.add(SquareButton);
        SquareButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-square-spinner-24.png"))); // NOI18N
        SquareButton.setSelected(true);
        SquareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SquareButtonActionPerformed(evt);
            }
        });
        panel2.add(SquareButton);

        buttonGroup3.add(CircleButton);
        CircleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-loading-circle-24.png"))); // NOI18N
        CircleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CircleButtonActionPerformed(evt);
            }
        });
        panel2.add(CircleButton);

        buttonGroup3.add(StarButton);
        StarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-star-24.png"))); // NOI18N
        StarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StarButtonActionPerformed(evt);
            }
        });
        panel2.add(StarButton);

        buttonGroup3.add(DiagonalButton);
        DiagonalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-line-24.png"))); // NOI18N
        panel2.add(DiagonalButton);

        buttonGroup3.add(HorizontalButton);
        HorizontalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-horizontal-line-24.png"))); // NOI18N
        panel2.add(HorizontalButton);

        buttonGroup3.add(Vertical);
        Vertical.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-vertical-line-24.png"))); // NOI18N
        panel2.add(Vertical);

        jPanel2.add(panel2);

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

    private void RedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedButtonActionPerformed
        Color color = Color.RED;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_RedButtonActionPerformed

    private void OrangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrangeButtonActionPerformed
        Color color = Color.ORANGE;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_OrangeButtonActionPerformed

    private void YellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YellowButtonActionPerformed
        Color color = Color.YELLOW;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_YellowButtonActionPerformed

    private void GreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GreenButtonActionPerformed
        Color color = Color.GREEN;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_GreenButtonActionPerformed

    private void BlueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlueButtonActionPerformed
        Color color = Color.BLUE;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_BlueButtonActionPerformed

    private void PurpleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurpleButtonActionPerformed
        Color color = Color.MAGENTA;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setColor(color);
        }
    }//GEN-LAST:event_PurpleButtonActionPerformed

    private void SquareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SquareButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SquareButtonActionPerformed

    private void CircleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CircleButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CircleButtonActionPerformed

    private void StarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StarButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StarButtonActionPerformed

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
        selectedButton = ZoomButton;
        AbstractButton aB = (AbstractButton) evt.getSource();
        boolean zSelected = aB.getModel().isSelected();

        setAppState(ToolState.ZOOM);
        panel1.setVisible(false);
        panel2.setVisible(false);

        if (zSelected) {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).emptyChart.setZoomOutlinePaint(Color.BLACK);
                charts.get(i).emptyChart.setChartState(ToolState.ZOOM);
            }
        }
    }

    private void PanButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setAppState(ToolState.PAN);
        panel1.setVisible(false);
        panel2.setVisible(false);
        
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.PAN);
        }
    }

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = SelectButton;
        RedButtonActionPerformed(evt);
        setAppState(ToolState.HIGHLIGHT);
        panel1.setVisible(true);
        panel2.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.HIGHLIGHT);
        }
    }

    private void CommentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = CommentButton;
        setAppState(ToolState.COMMENT);
        panel1.setVisible(false);
        panel2.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).emptyChart.setChartState(ToolState.COMMENT);
        }
    }

    private void MarkerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = MarkerButton;
        RedButtonActionPerformed(evt);
        setAppState(ToolState.MARK);
        panel1.setVisible(true);
        panel2.setVisible(true);

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
    private javax.swing.JToggleButton BlueButton;
    private javax.swing.JToggleButton CircleButton;
    private javax.swing.JToggleButton CommentButton;
    private javax.swing.JToggleButton DiagonalButton;
    private javax.swing.JToggleButton GreenButton;
    private javax.swing.JMenuItem HighlightColor;
    private javax.swing.JToggleButton HorizontalButton;
    private javax.swing.JToggleButton MarkerButton;
    private javax.swing.JToggleButton OrangeButton;
    private javax.swing.JToggleButton PanButton;
    private javax.swing.JToggleButton PurpleButton;
    private javax.swing.JToggleButton RedButton;
    private javax.swing.JToggleButton SelectButton;
    private javax.swing.JToggleButton SquareButton;
    private javax.swing.JToggleButton StarButton;
    private javax.swing.JToggleButton Vertical;
    private javax.swing.JToggleButton YellowButton;
    private javax.swing.JToggleButton ZoomButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JFileChooser importChooser;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenuItem;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JToggleButton selectedButton = ZoomButton;
    public javax.swing.JMenuItem getImportButton() {
        return importDataMenuItem;
    }

    public javax.swing.JFileChooser getImportChooser() {
        return importChooser;
    }
    

}