package minesweeper;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * View for the Minesweeper Game
 * @author jsaalfel
 */
public class View extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;
    private JPanel view;
    private JLabel bombs, gameState, timer;
    private ButtonView[][] fields;
    private Model model;

    /**
     * Constructor
     *
     * @param model sets the standard values for the view
     */
    public View(Model model) {
        this.model = model;
        this.setLayout(new BorderLayout());
        this.view = new JPanel();
        this.bombs = setLabel(this.bombs, "Bombs:  " + Integer.toString(model.remainingBombs()));
        this.gameState = setLabel(this.gameState, "Status:  " + model.getState());
        this.timer = setLabel(this.timer, "Time:  " + model.getTimer());

        this.add(this.bombs, BorderLayout.WEST);
        this.add(this.gameState, BorderLayout.EAST);
        this.add(this.timer, BorderLayout.CENTER);
        this.add(restartButton(), BorderLayout.NORTH);
        this.fields = new ButtonView[model.getHeight()][model.getWidth()];
        this.model.addObserver(this);

        this.view.setLayout(new GridLayout(model.getHeight(), model.getWidth()));
        buildbuttons();
        this.add(view, BorderLayout.SOUTH);

    }

    /**
     * updatet die View
     */
    @Override
    public void update(Observable obs, Object o) {
		// TODO Auto-generated method 

        // wenn o dann alle  buttons apdaten
        if (o != null) {
            if ((boolean) o) {
                updateButtons();
            }
        }
        this.bombs = setLabel(this.bombs, "Bombs:  " + Integer.toString(model.remainingBombs()));
        this.gameState = setLabel(this.gameState, "Status:  " + model.getState());
        this.timer = setLabel(this.timer, "Time:  " + this.model.getTimer());

    }

    /**
     *
     * @param label setzt das label
     * @param string der Text
     * @return gibt ein label zur�ck mit String
     */
    private JLabel setLabel(JLabel label, String string) {
        if (!(label instanceof JLabel)) {
            label = new JLabel("");
        }
        label.setText(string);
        label.setPreferredSize(new Dimension(100, 40));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;

    }

    public JPanel getview() {
        return this.view;
    }

    /**
     * erstellt reset Button
     *
     * @return
     */
    public JButton restartButton() {
        JButton button = new JButton("Restart");
        button.setPreferredSize(new Dimension(20, 40));
        Controller controller = new Controller(model);
        button.addMouseListener(controller);
        return button;

    }

    /**
     * update Buttons
     */
    public void updateButtons() {

        removeButtons();

        buildbuttons();

    }

    /**
     * buttons loeschen
     */
    private void removeButtons() {
        for (int i = 0; i < this.model.getHeight(); i++) {
            for (int j = 0; j < this.model.getWidth(); j++) {

                this.view.remove(fields[i][j].getButton());

            }

        }

    }

    /**
     * buttons erstellen
     */
    private void buildbuttons() {

        for (int i = 0; i < this.model.getHeight(); i++) {
            for (int j = 0; j < this.model.getWidth(); j++) {
                ButtonView button = new ButtonView(this.model.getField(i, j));
                fields[i][j] = button;
                this.view.add(button.getButton());

            }

        }
    }
}
