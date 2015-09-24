package minesweeper;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class MineSweeper {

    /**
     * Starts your Minesweeper Game
     * @param argv You can either pass your Level as parameter of 3 parameters for
     *             width, heigth and bombs
     * @throws IOException
     */
    public static void main(String[] argv) throws IOException {
        String input = "";
        int width = -1;
        int height = -1;
        int bombs = -1;

        //Set the Modus
        if (argv.length == 1) {
            input = argv[0];
        }
        //Set the width, height and the number of Bombs
        if (argv.length == 3) {
            width = Integer.parseInt(argv[0]);
            height = Integer.parseInt(argv[1]);
            bombs = Integer.parseInt(argv[2]);
            input = "Custom";
        }

        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);

        Model model;
        
        System.out.println("Modes: Beginner/Intermediate/Expert/Custom");

        //Annoy the user until he chooses something
        while (!input.equals("Beginner")
                && !input.equals("beginner")
                && !input.equals("Intermediate")
                && !input.equals("intermediate")
                && !input.equals("Expert")
                && !input.equals("expert")
                && !input.equals("Custom")
                && !input.equals("custom")) {

            System.out.print("Enter your desired mode: ");
            input = scan.nextLine();
        }

        //Sets the specific Variables for the modes
        if (input.equals("Beginner") || input.equals("beginner")) {
            width = 9;
            height = 9;
            bombs = 10;
            model = new Model(width, height, bombs);
        } else if (input.equals("Intermediate") || input.equals("intermediate")) {
            width = 16;
            height = 16;
            bombs = 40;
            model = new Model(width, height, bombs);
        } else if (input.equals("Expert") || input.equals("expert")) {
            width = 30;
            height = 16;
            bombs = 99;
            model = new Model(width, height, bombs);
        } else {

            System.out.println();
            while (width < 6 || width > 35) {

                System.out.print("Enter your desired width (min:6/max:35): ");
                width = scan.nextInt();

            }

            System.out.println();
            while (height < 6 || height > 20) {

                System.out.print("Enter your desired heigth (min:6/max:20): ");
                height = scan.nextInt();

            }

            int maxbombs = height * width - 1;

            System.out.println();
            while (bombs < 1 || bombs > maxbombs) {
                System.out.print("Plese enter the number of Bombs (min:1/max:" + maxbombs + "): ");
                bombs = scan.nextInt();
            }
            //Creating the Model with the desired values
            model = new Model(width, height, bombs);
        }
        
        //Information about the game
        System.out.println("\n\nCreating Minesweeper:");
        System.out.println("Heigth: " + height);
        System.out.println("Width: " + width);
        System.out.println("Bombs: " + bombs);

        //Creating the View
        View view = new View(model);

        JFrame frame = new JFrame("Minesweeper");

        frame.setContentPane(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

    }
}
