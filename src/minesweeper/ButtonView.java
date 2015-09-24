package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

/**
 * 
 * @author jsaalfeld
 */
public class ButtonView extends JButton implements Observer {

    private static final long serialVersionUID = 1L;
    private JButton button;
    private Field field;
    private Controller controller;

    /**
     * Creates the neccassary Buttons
     *
     * @param field 
     */
    public ButtonView(Field field) {
        this.field = field;
        this.button = new JButton("");
        this.button.setPreferredSize(new Dimension(50, 50));
        this.controller = new Controller(field);
        this.button.addMouseListener(controller);
        this.field.addObserver(this);
    }

    public JButton getButton() {
        return this.button;
    }

    /**
     * Makes a Button Invisible
     */
    public void setUnvisible() {
        this.button.setVisible(false);
        this.field.reveal();
    }

    /**
     * Update button view
     * 
     * @param obs 
     * @param o
     */
    @Override
    public void update(Observable obs, Object o) {
        // TODO Auto-generated method stub
        if (field.getRevealed() == true) {

            if (this.field.getField_id() == 9) {
                this.button.setBackground(Color.red);
                this.button.setText("B");
            } else {
                this.button.setBackground(Color.LIGHT_GRAY);
                if (this.field.getField_id() == 0) {
                    this.button.setText("");
                } else {
                    this.button.setText(Integer.toString(this.field.getField_id()));
                }
            }
        }
        if (field.isFlag()) {
            this.button.setText("!");
            this.button.setBackground(Color.orange);
        }
        if (!field.isFlag() && !field.getRevealed()) {
            this.button.setText("");
            this.button.setBackground(new JButton().getBackground());
        }
    }

}
