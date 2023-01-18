package edu.csusm.capstone.timeseriesannotator.View;

import edu.csusm.capstone.timeseriesannotator.Controller.Chart;
import edu.csusm.capstone.timeseriesannotator.Controller.HDF5addAction;
import java.awt.event.ActionListener;

/**
 *
 * @author josef
 */
public class HDF5addSeries extends javax.swing.JDialog implements ActionListener {

    
    Chart chartStruct = Chart.getInstance();
    String Xaxis;
    /**
     * Creates new form HDF5addSeries
     */
    public HDF5addSeries(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        Xaxis = chartStruct.getXpath().split("/")[0];
        System.out.print(Xaxis);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel1.setText("Enter HDF5 File Path:");

        jLabel3.setText("Y-Axis File Path:");

        Yaxispath.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        Yaxispath.addActionListener(this);

        HDF5PathButton.setText("Select File Path");
        HDF5PathButton.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(HDF5PathButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(Yaxispath, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Yaxispath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(HDF5PathButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == HDF5PathButton) {
            HDF5addSeries.this.HDF5PathButtonActionPerformed(evt);
        }
        else if (evt.getSource() == Yaxispath) {
            HDF5addSeries.this.YaxispathActionPerformed(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void HDF5PathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HDF5PathButtonActionPerformed
        ActionListener HDF5addAction = new HDF5addAction(this, Yaxispath);
        HDF5addAction.actionPerformed(evt);
    }//GEN-LAST:event_HDF5PathButtonActionPerformed

    private void YaxispathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YaxispathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_YaxispathActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton HDF5PathButton;
    private javax.swing.JTextField Yaxispath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
