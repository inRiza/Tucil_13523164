package GUI;

import utils.FileHandler;

import javax.swing.*;

import main.Input;
import main.Solver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainGUI extends JFrame {
    private JTextField fileInputField;
    private JButton solveButton;
    private JPanel boardPanel;
    private JLabel infoLabel;
    private JButton saveTextButton;
    private JButton saveImageButton;

    private Solver solver;
    private Input input;

    public MainGUI() {
        setTitle("IQ Puzzler Pro Solver");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        fileInputField = new JTextField(20);
        solveButton = new JButton("Solve");
        inputPanel.add(new JLabel("Enter filename: "));
        inputPanel.add(fileInputField);
        inputPanel.add(solveButton);
        add(inputPanel, BorderLayout.NORTH);

        // Board panel
        boardPanel = new JPanel();
        boardPanel.setBackground(Color.WHITE);
        add(boardPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoLabel = new JLabel("Execution time: 0 ms | Iterations: 0");
        infoPanel.add(infoLabel);
        add(infoPanel, BorderLayout.SOUTH);

        // Save buttons panel
        JPanel savePanel = new JPanel();
        saveTextButton = new JButton("Save as Text");
        saveImageButton = new JButton("Save as Image");
        saveTextButton.setEnabled(false);
        saveImageButton.setEnabled(false);
        savePanel.add(saveTextButton);
        savePanel.add(saveImageButton);
        add(savePanel, BorderLayout.EAST);

        // Solve button action
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFilename = fileInputField.getText();
                try {
                    input = FileHandler.readInputFile(inputFilename);
                    solver = new Solver(input.rows, input.cols, input.pieces, input.grid);

                    // Start solving in a separate thread to avoid freezing the GUI
                    new Thread(() -> {
                        solveButton.setEnabled(false);
                        boolean solved = solver.solve();

                        long executionTime = solver.getExecutionTime();
                        long iterations = solver.getCases();

                        SwingUtilities.invokeLater(() -> {
                            if (solved) {
                                infoLabel.setText(
                                        "Execution time: " + executionTime + " ms | Iterations: " + iterations);
                                drawBoard(solver.getBoard());
                                saveTextButton.setEnabled(true);
                                saveImageButton.setEnabled(true);
                            } else {
                                infoLabel.setText("No solution exists!");
                            }
                            solveButton.setEnabled(true);
                        });
                    }).start();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Save as Text button action
        saveTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String outputFilename = "solution_text_" + fileInputField.getText();
                try {
                    FileHandler.saveText(outputFilename, solver.getBoard(), solver.getExecutionTime(),
                            solver.getCases());
                    JOptionPane.showMessageDialog(MainGUI.this, "Solution saved to: test/output/" + outputFilename,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Save as Image button action
        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String outputFilename = "solution_image_" + fileInputField.getText().replace(".txt", ".png");
                try {
                    FileHandler.saveImage(outputFilename, solver.getBoard(), input.rows, input.cols,
                            solver.getExecutionTime(), solver.getCases());
                    JOptionPane.showMessageDialog(MainGUI.this, "Image saved to: test/output/" + outputFilename,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void drawBoard(String boardState) {
        boardPanel.removeAll();
        String[] lines = boardState.split("\n");
        int rows = lines.length;
        int cols = lines[0].length();

        boardPanel.setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = lines[i].charAt(j);
                JLabel cell = new JLabel(String.valueOf(c), SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setBackground(getColorForPiece(c));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private Color getColorForPiece(char pieceId) {
        if (pieceId == '.' || pieceId == ' ') {
            return Color.WHITE;
        }
        int index = pieceId - 'A';
        if (index >= 0 && index < FileHandler.COLORS.length) {
            return FileHandler.COLORS[index];
        }
        return Color.LIGHT_GRAY;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}