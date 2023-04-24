/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.csusm.capstone.timeseriesannotator.View;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author Ramon Duran Rizo
 */
public class CommentTest {
    
    private CommentMenu commentMenu;
    private DialogFixture dialog;

    @Before
    public void setUp() {
        commentMenu = GuiActionRunner.execute(() -> new CommentMenu(new javax.swing.JFrame(), true));
        dialog = new DialogFixture(commentMenu);
        dialog.show(); // shows the frame to test
    }
    @After
    public void tearDown(){
        dialog.cleanUp();
    }

    @Test
    public void testDefaults() {
        dialog.textBox("Comment").enterText("test comment");
        dialog.button("CommentSubmit").click();
    }

}
