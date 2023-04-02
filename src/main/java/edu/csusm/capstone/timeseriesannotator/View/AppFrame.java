package edu.csusm.capstone.timeseriesannotator.View;

import com.formdev.flatlaf.FlatLightLaf;
import edu.csusm.capstone.timeseriesannotator.Controller.*;
import edu.csusm.capstone.timeseriesannotator.Model.MarkerType;
import edu.csusm.capstone.timeseriesannotator.Model.ToolState;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JColorChooser;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Cody McKinney
 */
public class AppFrame extends javax.swing.JFrame {

    public static ToolState appState;
    public static MarkerType markerType;
    ChartDisplay chartDisplay;
    public static ArrayList<ChartDisplay> charts;
    private boolean ctrlPressed = false;
    MultiSplitPane split = new MultiSplitPane();
    private static String[] textFontArray;
    public static Color color;

    /* FONT variables */
    public static String font = "Arial";
    public static String fontStyle = "PLAIN";
    public static int fontSize = 12;
    
    public static AppFrame frame;

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
        setMarkerType(MarkerType.SQUARE);
        KeyEventDispatcher toggleKeyDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED & !(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof javax.swing.JTextField)) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_1:
                            AddChartButton.doClick();
                            break;
                        case KeyEvent.VK_2:
                            ZoomButton.doClick();
                            break;
                        case KeyEvent.VK_3:
                            PanButton.doClick();
                            break;
                        case KeyEvent.VK_4:
                            EditButton.doClick();
                            break;
                        case KeyEvent.VK_5:
                            SelectButton.doClick();
                            break;
                        case KeyEvent.VK_6:
                            CommentButton.doClick();
                            break;
                        case KeyEvent.VK_7:
                            LineButton.doClick();
                            break;
                        case KeyEvent.VK_8:
                            ShapeButton.doClick();
                            break;
                        case KeyEvent.VK_CONTROL:
                            ctrlPressed = true;
                            if (!PanButton.isSelected() && (e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0) {
                                PanButton.doClick();
                            }
                            break;
                    }
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    
                    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                        selectedButton.doClick();
                    }
                    ctrlPressed = false;
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(toggleKeyDispatcher);
        ZoomButton.doClick();
        this.setLocationRelativeTo(null);
        this.frame = this;
        
        importChooser.addChoosableFileFilter(new FileNameExtensionFilter("Data Files", "csv", "hdf5"));
        importChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        importChooser.addChoosableFileFilter(new FileNameExtensionFilter("HDF5", "hdf5"));
        importChooser.setAcceptAllFileFilterUsed(false);
    }

    // Creates an empty chart on instantiation of program
    public void initialChart() {
        chartDisplay = new ChartDisplay(this);
        charts.add(chartDisplay);
        split.addComponent(chartDisplay);
        ChartPanel.removeAll();
        ChartPanel.add(split);
        validate();
        repaint();
    }

    // Adds additional charts to the panel
    public void addChart(ArrayList<ChartDisplay> c) {
        this.charts = c;
        for (int i = 0; i < charts.size(); i++) {
            System.out.println("test " + i);
            charts.get(i).aChartPanel.redrawCommentBox();
            charts.get(i).aChartPanel.repaint();
        }
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
        java.awt.GridBagConstraints gridBagConstraints;

        importChooser = new javax.swing.JFileChooser();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jDialog1 = new javax.swing.JDialog();
        ChartPanel = new javax.swing.JPanel();
        ToolBarPanel = new javax.swing.JPanel();
        ToolSelectPanel = new javax.swing.JPanel();
        AddChartButton = new javax.swing.JButton();
        ZoomButton = new javax.swing.JToggleButton();
        PanButton = new javax.swing.JToggleButton();
        EditButton = new javax.swing.JToggleButton();
        SelectButton = new javax.swing.JToggleButton();
        CommentButton = new javax.swing.JToggleButton();
        LineButton = new javax.swing.JToggleButton();
        ShapeButton = new javax.swing.JToggleButton();
        TogglePanel = new javax.swing.JPanel();
        ColorPanel = new java.awt.Panel();
        BlackButton = new javax.swing.JToggleButton();
        RedButton = new javax.swing.JToggleButton();
        OrangeButton = new javax.swing.JToggleButton();
        YellowButton = new javax.swing.JToggleButton();
        GreenButton = new javax.swing.JToggleButton();
        BlueButton = new javax.swing.JToggleButton();
        PurpleButton = new javax.swing.JToggleButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        ShapesPanel = new java.awt.Panel();
        SquareButton = new javax.swing.JToggleButton();
        EllipseButton = new javax.swing.JToggleButton();
        TriangleButton = new javax.swing.JToggleButton();
        LinesPanel = new java.awt.Panel();
        VerticalButton = new javax.swing.JToggleButton();
        HorizontalButton = new javax.swing.JToggleButton();
        DiagonalButton = new javax.swing.JToggleButton();
        RayButton = new javax.swing.JToggleButton();
        SegmentButton = new javax.swing.JToggleButton();
        FontPanel = new java.awt.Panel();
        FontSizeLabel = new javax.swing.JLabel();
        FontSizeComboBox = new javax.swing.JComboBox<>();
        FontLabel = new javax.swing.JLabel();
        FontComboBox = new javax.swing.JComboBox<>();
        FontStyleLabel = new javax.swing.JLabel();
        FontStyleComboBox = new javax.swing.JComboBox<>();
        menuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        importDataMenuItem = new javax.swing.JMenuItem();
        AddChartMenuItem = new javax.swing.JMenuItem();
        editMenuItem = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenu();
        HighlightColor = new javax.swing.JMenuItem();

        importChooser.setCurrentDirectory(new java.io.File("./dataFiles"));
        importChooser.setName("importChooser"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Multivariate Time Series Annotator");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(0, 0, 1000, 800));
        setLocation(new java.awt.Point(0, 0));
        setPreferredSize(new java.awt.Dimension(1200, 800));

        ChartPanel.setBackground(new java.awt.Color(255, 255, 255));
        ChartPanel.setPreferredSize(new java.awt.Dimension(900, 800));
        ChartPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(ChartPanel, java.awt.BorderLayout.CENTER);

        ToolBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        ToolBarPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        ToolBarPanel.setMaximumSize(new java.awt.Dimension(55, 55));
        ToolBarPanel.setMinimumSize(new java.awt.Dimension(55, 55));
        ToolBarPanel.setLayout(new java.awt.GridLayout(1, 0));

        ToolSelectPanel.setBackground(new java.awt.Color(255, 255, 255));
        ToolSelectPanel.setLayout(new java.awt.GridLayout(1, 0));

        AddChartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/addchart.png"))); // NOI18N
        AddChartButton.setText("1");
        AddChartButton.setToolTipText("Add new chart (0)");
        AddChartButton.setName("AddChartButton");
        AddChartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddChartButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        AddChartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddChartButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(AddChartButton);

        buttonGroup1.add(ZoomButton);
        ZoomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/zoomin.png"))); // NOI18N
        ZoomButton.setText("2");
        ZoomButton.setName("ZoomButton");
        ZoomButton.setToolTipText("Zoom (1)");
        ZoomButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        ZoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(ZoomButton);

        buttonGroup1.add(PanButton);
        PanButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/4dirs.png"))); // NOI18N
        PanButton.setText("3");
        PanButton.setName("PanButton");
        PanButton.setToolTipText("Move (2)");
        PanButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        PanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PanButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(PanButton);

        buttonGroup1.add(EditButton);
        EditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-3d-pointer-20.png"))); // NOI18N
        EditButton.setSelected(true);
        EditButton.setText("4");
        EditButton.setToolTipText("Select (3)");
        EditButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        EditButton.setName("EditButton");
        EditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(EditButton);

        buttonGroup1.add(SelectButton);
        SelectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-select-middle-column-20.png"))); // NOI18N
        SelectButton.setText("5");
        SelectButton.setName("SelectButton");
        SelectButton.setToolTipText("Select (4)");
        SelectButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        SelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(SelectButton);

        buttonGroup1.add(CommentButton);
        CommentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-comment-medical-32.png"))); // NOI18N
        CommentButton.setText("6");
        CommentButton.setName("CommentButton");
        CommentButton.setToolTipText("Comment (5)");
        CommentButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        CommentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(CommentButton);

        buttonGroup1.add(LineButton);
        LineButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-line-24.png"))); // NOI18N
        LineButton.setText("7");
        LineButton.setName("LineButton");
        LineButton.setToolTipText("Lines (6)");
        LineButton.setActionCommand("(5)");
        LineButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        LineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LineButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(LineButton);

        buttonGroup1.add(ShapeButton);
        ShapeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-diversity-50.png"))); // NOI18N
        ShapeButton.setText("8");
        ShapeButton.setToolTipText("Shapes(7)");
        ShapeButton.setActionCommand("(5)");
        ShapeButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        ShapeButton.setName("ShapeButton");
        ShapeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShapeButtonActionPerformed(evt);
            }
        });
        ToolSelectPanel.add(ShapeButton);

        ToolBarPanel.add(ToolSelectPanel);

        TogglePanel.setBackground(new java.awt.Color(255, 255, 255));
        TogglePanel.setLayout(new java.awt.GridLayout(1, 0));

        ColorPanel.setLayout(new java.awt.GridLayout(1, 0));

        BlackButton.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup2.add(BlackButton);
        BlackButton.setSelected(true);
        BlackButton.setDoubleBuffered(true);
        BlackButton.setName("BlackButton"); // NOI18N
        BlackButton.setPreferredSize(new java.awt.Dimension(20, 20));
        BlackButton.setName("BlackButton");
        BlackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlackButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(BlackButton);

        RedButton.setBackground(new java.awt.Color(255, 51, 51));
        buttonGroup2.add(RedButton);
        RedButton.setDoubleBuffered(true);
        RedButton.setName("RedButton"); // NOI18N
        RedButton.setPreferredSize(new java.awt.Dimension(20, 20));
        RedButton.setName("RedButton");
        RedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(RedButton);

        OrangeButton.setBackground(new java.awt.Color(255, 153, 0));
        buttonGroup2.add(OrangeButton);
        OrangeButton.setDoubleBuffered(true);
        OrangeButton.setName("OrangeButton"); // NOI18N
        OrangeButton.setPreferredSize(new java.awt.Dimension(20, 20));
        OrangeButton.setName("OrangeButton");
        OrangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrangeButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(OrangeButton);

        YellowButton.setBackground(new java.awt.Color(255, 255, 51));
        buttonGroup2.add(YellowButton);
        YellowButton.setDoubleBuffered(true);
        YellowButton.setName("YellowButton"); // NOI18N
        YellowButton.setPreferredSize(new java.awt.Dimension(20, 20));
        YellowButton.setName("YellowButton");
        YellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YellowButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(YellowButton);

        GreenButton.setBackground(new java.awt.Color(0, 255, 0));
        buttonGroup2.add(GreenButton);
        GreenButton.setDoubleBuffered(true);
        GreenButton.setName("GreenButton"); // NOI18N
        GreenButton.setPreferredSize(new java.awt.Dimension(20, 20));
        GreenButton.setName("GreenButton");
        GreenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GreenButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(GreenButton);

        BlueButton.setBackground(new java.awt.Color(0, 102, 255));
        buttonGroup2.add(BlueButton);
        BlueButton.setDoubleBuffered(true);
        BlueButton.setName("BlueButton"); // NOI18N
        BlueButton.setPreferredSize(new java.awt.Dimension(20, 20));
        BlueButton.setName("BlueButton");
        BlueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlueButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(BlueButton);

        PurpleButton.setBackground(new java.awt.Color(153, 0, 255));
        buttonGroup2.add(PurpleButton);
        PurpleButton.setDoubleBuffered(true);
        PurpleButton.setName("PurpleButton"); // NOI18N
        PurpleButton.setPreferredSize(new java.awt.Dimension(20, 20));
        PurpleButton.setName("PurpleButton");
        PurpleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurpleButtonActionPerformed(evt);
            }
        });
        ColorPanel.add(PurpleButton);

        TogglePanel.add(ColorPanel);

        jLayeredPane1.setLayout(new javax.swing.OverlayLayout(jLayeredPane1));

        ShapesPanel.setVisible(false);
        ShapesPanel.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup3.add(SquareButton);
        SquareButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-square-spinner-24.png"))); // NOI18N
        SquareButton.setSelected(true);
        SquareButton.setName("SquareButton"); // NOI18N
        SquareButton.setName("SquareButton");
        SquareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SquareButtonActionPerformed(evt);
            }
        });
        ShapesPanel.add(SquareButton);

        buttonGroup3.add(EllipseButton);
        EllipseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-loading-circle-24.png"))); // NOI18N
        EllipseButton.setName("EllipseButton"); // NOI18N
        EllipseButton.setName("EllipseButton");
        EllipseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EllipseButtonActionPerformed(evt);
            }
        });
        ShapesPanel.add(EllipseButton);

        buttonGroup3.add(TriangleButton);
        TriangleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/triangle.png"))); // NOI18N
        TriangleButton.setName("TriangleButton"); // NOI18N
        TriangleButton.setName("TriangleButton");
        TriangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TriangleButtonActionPerformed(evt);
            }
        });
        ShapesPanel.add(TriangleButton);

        jLayeredPane1.add(ShapesPanel);

        LinesPanel.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup4.add(VerticalButton);
        VerticalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-vertical-line-24.png"))); // NOI18N
        VerticalButton.setSelected(true);
        VerticalButton.setName("VerticalButton"); // NOI18N
        VerticalButton.setName("VerticalButton");
        VerticalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerticalButtonActionPerformed(evt);
            }
        });
        LinesPanel.add(VerticalButton);

        buttonGroup4.add(HorizontalButton);
        HorizontalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-horizontal-line-24.png"))); // NOI18N
        HorizontalButton.setName("HorizontalButton"); // NOI18N
        HorizontalButton.setName("HorizontalButton");
        HorizontalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorizontalButtonActionPerformed(evt);
            }
        });
        LinesPanel.add(HorizontalButton);

        buttonGroup4.add(DiagonalButton);
        DiagonalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-line-24.png"))); // NOI18N
        DiagonalButton.setName("DiagonalButton"); // NOI18N
        SegmentButton.setName("SegmentButton");
        DiagonalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiagonalButtonActionPerformed(evt);
            }
        });
        LinesPanel.add(DiagonalButton);

        buttonGroup4.add(RayButton);
        RayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-drag-up-right-arrow-20.png"))); // NOI18N
        RayButton.setName("RayButton"); // NOI18N
        RayButton.setName("RayButton");
        RayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RayButtonActionPerformed(evt);
            }
        });
        LinesPanel.add(RayButton);

        buttonGroup4.add(SegmentButton);
        SegmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-polyline-20.png"))); // NOI18N
        SegmentButton.setName("SegmentButton"); // NOI18N
        SegmentButton.setName("SegmentButton");
        SegmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SegmentButtonActionPerformed(evt);
            }
        });
        LinesPanel.add(SegmentButton);

        jLayeredPane1.setLayer(LinesPanel, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.add(LinesPanel);

        FontPanel.setVisible(false);
        FontPanel.setLayout(new java.awt.GridBagLayout());

        FontSizeLabel.setText("Font Size:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        FontPanel.add(FontSizeLabel, gridBagConstraints);

        FontSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "14", "16", "18", "20", "24", "26", "28", "30" }));
        FontSizeComboBox.setSelectedIndex(4);
        FontSizeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FontSizeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        FontPanel.add(FontSizeComboBox, gridBagConstraints);

        FontLabel.setText("Font:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        FontPanel.add(FontLabel, gridBagConstraints);

        FontComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Arial", "Calibri", "Rockwell", "SansSerif", "Times New Roman", "Comic Sans MS" }));
        FontComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FontComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        FontPanel.add(FontComboBox, gridBagConstraints);

        FontStyleLabel.setText("Font Style:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        FontPanel.add(FontStyleLabel, gridBagConstraints);

        FontStyleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plain", "Italic", "Bold" }));
        FontStyleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FontStyleComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        FontPanel.add(FontStyleComboBox, gridBagConstraints);

        jLayeredPane1.setLayer(FontPanel, javax.swing.JLayeredPane.MODAL_LAYER);
        jLayeredPane1.add(FontPanel);

        TogglePanel.add(jLayeredPane1);

        ToolBarPanel.add(TogglePanel);

        getContentPane().add(ToolBarPanel, java.awt.BorderLayout.PAGE_START);

        menuBar.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        fileMenuItem.setText("File");

        importDataMenuItem.setText("Import Data");
        importDataMenuItem.setName("ImportData");
        importDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importDataMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(importDataMenuItem);

        AddChartMenuItem.setText("Add Chart");
        AddChartMenuItem.setName("AddChartMenuItem");
        AddChartMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddChartMenuItemActionPerformed(evt);
            }
        });
        fileMenuItem.add(AddChartMenuItem);

        menuBar.add(fileMenuItem);

        editMenuItem.setText("Edit");
        menuBar.add(editMenuItem);

        optionsMenuItem.setText("Options");

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
        color = Color.RED;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_RedButtonActionPerformed

    private void OrangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrangeButtonActionPerformed
        color = Color.ORANGE;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_OrangeButtonActionPerformed

    private void YellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YellowButtonActionPerformed
        color = Color.YELLOW;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_YellowButtonActionPerformed

    private void GreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GreenButtonActionPerformed
        color = Color.GREEN;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_GreenButtonActionPerformed

    private void BlueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlueButtonActionPerformed
        color = Color.BLUE;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_BlueButtonActionPerformed

    private void PurpleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurpleButtonActionPerformed
        color = Color.MAGENTA;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_PurpleButtonActionPerformed

    private void SquareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SquareButtonActionPerformed
        setMarkerType(MarkerType.SQUARE);
    }//GEN-LAST:event_SquareButtonActionPerformed

    private void EllipseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EllipseButtonActionPerformed
        setMarkerType(MarkerType.ELLIPSE);
    }//GEN-LAST:event_EllipseButtonActionPerformed

    private void TriangleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TriangleButtonActionPerformed
        setMarkerType(MarkerType.TRIANGLE);
    }//GEN-LAST:event_TriangleButtonActionPerformed

    private void AddChartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddChartButtonActionPerformed
        AddChartAction addChart = new AddChartAction(split, charts, this);
    }//GEN-LAST:event_AddChartButtonActionPerformed

    private void VerticalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerticalButtonActionPerformed
        setMarkerType(MarkerType.VERTICAL);
    }//GEN-LAST:event_VerticalButtonActionPerformed

    private void HorizontalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorizontalButtonActionPerformed
        setMarkerType(MarkerType.HORIZONTAL);
    }//GEN-LAST:event_HorizontalButtonActionPerformed

    private void SegmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SegmentButtonActionPerformed
        setMarkerType(MarkerType.SEGMENT);
    }//GEN-LAST:event_SegmentButtonActionPerformed

    private void ShapeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShapeButtonActionPerformed
        selectedButton = ShapeButton;
        if (!ctrlPressed) {
            OrangeButton.doClick();
            SquareButton.doClick();
        }
        setAppState(ToolState.MARK);
        ColorPanel.setVisible(true);

        ShapesPanel.setVisible(true);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.MARK);
        }
    }//GEN-LAST:event_ShapeButtonActionPerformed

    private void BlackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlackButtonActionPerformed
        color = Color.BLACK;
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }
    }//GEN-LAST:event_BlackButtonActionPerformed

    private void FontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FontComboBoxActionPerformed
        font = FontComboBox.getSelectedItem().toString();
    }//GEN-LAST:event_FontComboBoxActionPerformed

    private void RayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RayButtonActionPerformed
        setMarkerType(MarkerType.RAY);
    }//GEN-LAST:event_RayButtonActionPerformed

    private void DiagonalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiagonalButtonActionPerformed
        setMarkerType(MarkerType.DIAGONAL);
    }//GEN-LAST:event_DiagonalButtonActionPerformed

    private void FontSizeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FontSizeComboBoxActionPerformed
        fontSize = Integer.parseInt(FontSizeComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_FontSizeComboBoxActionPerformed

    private void FontStyleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FontStyleComboBoxActionPerformed
        fontStyle = FontStyleComboBox.getSelectedItem().toString();
    }//GEN-LAST:event_FontStyleComboBoxActionPerformed

    private void EditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButtonActionPerformed
        selectedButton = EditButton;
        setAppState(ToolState.SELECT);
        ColorPanel.setVisible(false);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.SELECT);
        }
    }//GEN-LAST:event_EditButtonActionPerformed

    public static int getFontStyle() {
        int style = Font.PLAIN;
        if (null != fontStyle) {
            switch (fontStyle) {
                case "Plain" ->
                    style = Font.PLAIN;
                case "Italic" ->
                    style = Font.ITALIC;
                case "Bold" ->
                    style = Font.BOLD;
                default -> {
                }
            }
        }

        return style;
    }

    public static Color getAbsoluteColor() {
        Color c = Color.BLACK;
        if (color != null) {
            c = color;
        }

        return c;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static String getFontName() {
        return font;
    }

    private void HighlightColorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_HighlightColorActionPerformed
        color = JColorChooser.showDialog(this,
                "Select a color", new Color(0, 100, 255, 60));
        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setColor(color);
        }

    }// GEN-LAST:event_HighlightColorActionPerformed

    private void AddChartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RemoveChartMenuItemActionPerformed
        AddChartAction addChart = new AddChartAction(split, charts, this);
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
        ColorPanel.setVisible(false);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(false);

        if (zSelected) {
            for (int i = 0; i < charts.size(); i++) {
                charts.get(i).aChartPanel.setZoomOutlinePaint(Color.BLACK);
                charts.get(i).aChartPanel.setChartState(ToolState.ZOOM);
            }
        }
    }

    private void PanButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!ctrlPressed) {
            selectedButton = PanButton;
        }
        setAppState(ToolState.PAN);
        ColorPanel.setVisible(false);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.PAN);
        }
    }

    private void SelectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = SelectButton;
        if (!ctrlPressed) {
            RedButton.doClick();
        }
        setAppState(ToolState.HIGHLIGHT);
        ColorPanel.setVisible(true);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.HIGHLIGHT);
        }
    }

    private void CommentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = CommentButton;
        if (!ctrlPressed) {
            BlackButton.doClick();
        }
        setAppState(ToolState.COMMENT);
        ColorPanel.setVisible(true);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(false);
        FontPanel.setVisible(true);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.COMMENT);
        }
    }

    private void LineButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectedButton = LineButton;
        if (!ctrlPressed) {
            BlackButton.doClick();
            VerticalButton.doClick();
        }
        setAppState(ToolState.MARK);
        ColorPanel.setVisible(true);

        ShapesPanel.setVisible(false);
        LinesPanel.setVisible(true);
        FontPanel.setVisible(false);

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).aChartPanel.setChartState(ToolState.MARK);
        }
    }

    private void setAppState(ToolState s) {
        appState = s;
    }

    public static ToolState getAppState() {
        return appState;
    }

    private void setMarkerType(MarkerType m) {
        markerType = m;
    }

    public static MarkerType getMarkerType() {
        return markerType;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddChartButton;
    private javax.swing.JMenuItem AddChartMenuItem;
    private javax.swing.JToggleButton BlackButton;
    private javax.swing.JToggleButton BlueButton;
    private javax.swing.JPanel ChartPanel;
    private java.awt.Panel ColorPanel;
    private javax.swing.JToggleButton CommentButton;
    private javax.swing.JToggleButton DiagonalButton;
    public javax.swing.JToggleButton EditButton;
    private javax.swing.JToggleButton EllipseButton;
    private javax.swing.JComboBox<String> FontComboBox;
    private javax.swing.JLabel FontLabel;
    private java.awt.Panel FontPanel;
    private javax.swing.JComboBox<String> FontSizeComboBox;
    private javax.swing.JLabel FontSizeLabel;
    private javax.swing.JComboBox<String> FontStyleComboBox;
    private javax.swing.JLabel FontStyleLabel;
    private javax.swing.JToggleButton GreenButton;
    private javax.swing.JMenuItem HighlightColor;
    private javax.swing.JToggleButton HorizontalButton;
    private javax.swing.JToggleButton LineButton;
    private java.awt.Panel LinesPanel;
    private javax.swing.JToggleButton OrangeButton;
    private javax.swing.JToggleButton PanButton;
    private javax.swing.JToggleButton PurpleButton;
    private javax.swing.JToggleButton RayButton;
    private javax.swing.JToggleButton RedButton;
    private javax.swing.JToggleButton SegmentButton;
    private javax.swing.JToggleButton SelectButton;
    private javax.swing.JToggleButton ShapeButton;
    private java.awt.Panel ShapesPanel;
    private javax.swing.JToggleButton SquareButton;
    private javax.swing.JPanel TogglePanel;
    private javax.swing.JPanel ToolBarPanel;
    private javax.swing.JPanel ToolSelectPanel;
    private javax.swing.JToggleButton TriangleButton;
    private javax.swing.JToggleButton VerticalButton;
    private javax.swing.JToggleButton YellowButton;
    public javax.swing.JToggleButton ZoomButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JMenu editMenuItem;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JFileChooser importChooser;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenuItem;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JToggleButton selectedButton = ZoomButton;

    public javax.swing.JMenuItem getImportButton() {
        return importDataMenuItem;
    }

    public javax.swing.JFileChooser getImportChooser() {
        return importChooser;
    }

}
