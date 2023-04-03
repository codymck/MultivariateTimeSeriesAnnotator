package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.ChartStruct;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5addAction;
import edu.csusm.capstone.timeseriesannotator.Model.HDFReader;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.List;

/**
 *
 * @author josef
 */
public class HDF5addSeries extends javax.swing.JDialog implements ActionListener, ItemListener {

    boolean selected = false;
    String yPath;
    HDFReader reader;

   // ChartStruct chartStruct;// = ChartStruct.getInstance();
    //String Xaxis;
    /**
     * Creates new form HDF5addSeries
     */
    public HDF5addSeries(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(AppFrame.frame);
        //Xaxis = chartStruct.getXpath().split("/")[0];
        //System.out.print(Xaxis);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Yaxispath = new javax.swing.JTextField();
        HDF5PathButton = new javax.swing.JButton();
        yList = new java.awt.List();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setText("Enter HDF5 File Path");

        jLabel3.setText("Y-Axis File Path:");

        Yaxispath.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        Yaxispath.addActionListener(this);

        HDF5PathButton.setText("Select File Path");
        HDF5PathButton.addActionListener(this);

        yList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        yList.addItemListener(this);
        yList.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yList, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(70, 70, 70))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(Yaxispath, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(HDF5PathButton)
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Yaxispath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(yList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(HDF5PathButton)
                .addGap(38, 38, 38))
        );

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == Yaxispath) {
            HDF5addSeries.this.YaxispathActionPerformed(evt);
        }
        else if (evt.getSource() == HDF5PathButton) {
            HDF5addSeries.this.HDF5PathButtonActionPerformed(evt);
        }
        else if (evt.getSource() == yList) {
            HDF5addSeries.this.yListActionPerformed(evt);
        }
    }

    public void itemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getSource() == yList) {
            HDF5addSeries.this.yListItemStateChanged(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void HDF5PathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HDF5PathButtonActionPerformed
        ActionListener HDF5addAction = new HDF5addAction(this, Yaxispath);
        HDF5addAction.actionPerformed(evt);
        selected = true;
    }//GEN-LAST:event_HDF5PathButtonActionPerformed

    private void YaxispathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YaxispathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_YaxispathActionPerformed

    private void yListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yListItemStateChanged
        if(yPath != null){
            yPath = yPath + evt.getItemSelectable().getSelectedObjects()[0].toString();
        }else{
            yPath = evt.getItemSelectable().getSelectedObjects()[0].toString();
        }

        //update yList selection
        if(updateList(yPath, yList)){
            yPath = yPath + "/";
        }else{
            //full path set
            yList.setVisible(false);
            pack();
        }
        Yaxispath.setText(yPath);
    }//GEN-LAST:event_yListItemStateChanged

    private void yListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yListActionPerformed

    public boolean isSelected(){
        return selected;
    }
    
    public boolean updateList(String p, java.awt.List l){
      try{
          l.removeAll();
          List<String> h = reader.buildPath(p);
          for(String t : h){
                l.add(t);
            }
          return true;
      }catch (Exception e){
          return false;
      }
    }
    
    public void setModel(List<String> h, HDFReader hr) {
        reader = hr;
        for(String t : h){
            yList.add(t);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton HDF5PathButton;
    private javax.swing.JTextField Yaxispath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private java.awt.List yList;
    // End of variables declaration//GEN-END:variables
}
