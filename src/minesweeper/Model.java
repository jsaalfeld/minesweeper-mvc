package minesweeper;

import java.util.Observable;

/**
 * Model for Minesweeper
 *
 * @author jsaalfeld
 *
 */
public class Model extends Observable {

    //No one frome the outside should manipulate this
    private Field[][] field;
    private int height;
    private int width;
    private int bombs;
    private int bombs_left;
    private String state;
    private int revealed;
    private Thread thread;	//Thread for Timer
    private int timer;		//Time for Timer as int
    private boolean running;	//boolean running for terminating the thread

    /**
     * Constructor
     *
     * @param width	width of the game area
     * @param height	heigth of the game area
     * @param bombs	number of Bombs
     */
    public Model(int width, int height, int bombs) {

        //Creates the Game field as an Array
        this.field = new Field[height][width];
        this.width = width;
        this.height = height;
        this.bombs = bombs;
        this.bombs_left = bombs;
        this.revealed = 0;
        this.timer = 0;
        this.state = "running";
        this.running = false;
        this.thread = new Thread();

        buildGameBoard();

    }

    /**
     * Initialize everything
     */
    public void Init() {
        //initialization as "lost"
        this.state = "lost";
        this.setChanged();
        this.notifyObservers();
        //reset all the fields
        resetAllFields();
        this.state = "running";
        this.bombs_left = this.bombs;
        this.revealed = 0;

        resetThread();
        buildGameBoard();
        this.setChanged();
        this.notifyObservers(true);

    }

    /**
     * Creates the Thread for the Timer
     */
    private void setThread() {

        this.thread = new Thread() {
            @SuppressWarnings("static-access")
            //if running = true timer increments every second by 1
            @Override
            public void run() {
                while (running) {

                    try {

                        addTimer();
                        setChanged();
                        notifyObservers();
                        this.sleep(1000);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        this.thread.start();

    }

    /**
     * Sets the timer
     */
    public void addTimer() {
        this.timer++;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Creates the Gameboads
     */
    public void buildGameBoard() {
        //set all the fields
        setAllFields();

        //"random" selection of Bombs
        for (int i = 0; i < bombs; i++) {
            int width, height;

            do {

                width = (int) (Math.random() * (this.width));
                height = (int) (Math.random() * (this.height));

            } while (this.field[height][width].getField_id() == 9);

            this.field[height][width].setBomb();
            bombcounter(getField(height, width));
        }
    }

    /**
     *
     * @param y height
     * @param x width
     * @return count of bombs around
     */
    public int nearbombs(int y, int x) {

        int result = 0;
        int ax;
        int ay;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                ay = y + i;
                ax = x + j;
                if (ay >= 0 && ay < this.height && ax >= 0 && ax < this.width) {
                    if (this.field[ay][ax].getField_id() == 9) {
                        result++;
                    }
                }

            }
        }
        return result;

    }

    /**
     * Add 1 to all fields around
     *
     * @param field
     */
    public void bombcounter(Field field) {
        int ax;
        int ay;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                ay = field.getPosy() + i;
                ax = field.getPosx() + j;

                if (ay >= 0 && ay < this.height && ax >= 0 && ax < this.width) {
                    if (this.field[ay][ax].getField_id() != 9) {
                        this.field[ay][ax].add1();
                    }
                }

            }
        }
    }

    /**
     * Opens all zeros and reveals them
     *
     * @param field
     */
    public void revealZeros(Field field) {
        int x = field.getPosx();
        int y = field.getPosy();

        int ax;
        int ay;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                ay = y + i;
                ax = x + j;

                if (ay >= 0 && ay < this.height && ax >= 0 && ax < this.width) {
                    if (!this.field[ay][ax].isFlag()) {
                        this.field[ay][ax].reveal();
                    }
                }

            }
        }

    }

    /**
     * Prints the field to console (for Debugging Stuff)
     */
    public void printArray() {

        System.out.println();
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                System.out.print(field[i][j].getField_id() + " ");
            }

            System.out.println();

        }
    }

    /**
     * Sets the State
     *
     * @param state Status
     */
    public void setState(String state) {
        this.state = state;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Sets all the feilds
     */
    private void setAllFields() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.field[i][j] = new Field(this, j, i, 0);
            }
        }
    }

    /**
     * Resets all fields to 0
     */
    private void resetAllFields() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.field[i][j].Init(this, j, i, 0);
            }
        }
    }

    /**
     * Adds one to the number of revelead bombs and changes the state to won
     * if it was won.
     */
    public void addToRevealed() {
        this.revealed++;
        if (this.revealed >= ((this.width * this.height) - bombs)) {
            setState("won");

        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Adds 1 to the remaining bombs
     */
    public void addRemainingBombs() {
        this.bombs_left++;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Removes 1 of the remaining bombs
     */
    public void subRemainingBombs() {
        this.bombs_left--;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Starts Thread
     */
    public void startThread() {
        this.running = true;
        this.setThread();
    }

    /**
     * Resetzts the Threads timer
     */
    public void resetThread() {
        this.running = false;
        this.timer = 0;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Stops the Thread
     */
    public void stopThread() {
        this.running = false;

    }

    /**
     *
     * @return Timer Thread
     */
    public Thread getThread() {
        return this.thread;
    }

    /**
     * 
     * @return Heigth of the Game
     */
    public int getHeight() {
        return this.height;
    }

    /**
     *
     * @return Width of The Game
     */
    public int getWidth() {
        return this.width;
    }

    /**
     *
     * @return State of the Game
     */
    public String getState() {
        return this.state;
    }

    /**
     *
     * @param height
     * @param width
     * @return Field at this position
     */
    public Field getField(int height, int width) {
        return this.field[height][width];
    }

    /**
     * 
     * @return the field as an Array
     */
    public Field[][] getFields() {
        return this.field;
    }

    /**
     *
     * @return number of Bombs
     */
    public int getBombs() {
        return this.bombs;
    }

    /**
     *
     * @return number of remaining Bombs
     */
    public int remainingBombs() {
        return this.bombs_left;
    }

    /**
     * 
     * @return the Timer
     */
    public int getTimer() {
        return this.timer;
    }

}
