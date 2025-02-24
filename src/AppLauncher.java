import GUI.MainGUI;
import main.Input;
import main.Solver;
import utils.FileHandler;

import java.util.Scanner;
import java.io.IOException;

public class AppLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your mode:");
        System.out.println("1. GUI Mode");
        System.out.println("2. CLI Mode");
        System.out.print("Enter choice (1/2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            // Jalankan GUI
            MainGUI.main(args);
        } else if (choice == 2) {
            // Jalankan CLI
            runCLI();
        } else {
            System.out.println("Choice is invalid.");
        }

        scanner.close();
    }

    private static void runCLI() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input filename: ");
        String inputFilename = scanner.nextLine();

        try {
            Input input = FileHandler.readInputFile(inputFilename);
            Solver solver = new Solver(input.rows, input.cols, input.pieces, input.grid);

            System.out.println("\nSolving... Please wait.");
            boolean solved = solver.solve();

            long executionTime = solver.getExecutionTime();
            long cases = solver.getCases();

            if (solved) {
                System.out.println("\nSolution found:");
                System.out.println(solver.getColoredBoard());
            } else {
                System.out.println("\nNo solution found.");
            }

            System.out.println("Time searching: " + executionTime + " ms");
            System.out.println("Total cases: " + cases);

            if (solved) {
                System.out.print("\nSave solution? (y/n): ");
                String saveChoice = scanner.nextLine().trim().toLowerCase();

                if (saveChoice.equals("y")) {
                    System.out.println("\nChoose output format:");
                    System.out.println("1. Image (.png)");
                    System.out.println("2. Text (.txt)");
                    System.out.print("Enter choice (1/2): ");

                    String formatChoice = scanner.nextLine().trim();

                    if (formatChoice.equals("1")) {
                        String outputFilename = "output_image_" + inputFilename.replace(".txt", ".png");
                        FileHandler.saveImage(outputFilename,
                                solver.getBoard(),
                                input.rows, input.cols, executionTime, cases);
                        System.out.println("Image saved to: test/saved/" + outputFilename);
                    } else if (formatChoice.equals("2")) {
                        String outputFilename = "output_text_" + inputFilename;
                        FileHandler.saveText(outputFilename,
                                solver.getBoard(),
                                executionTime,
                                cases);
                        System.out.println("Solution saved to: test/saved/" + outputFilename);
                    } else {
                        System.out.println("Invalid choice. Solution not saved.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}