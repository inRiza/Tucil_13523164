package utils;

import main.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class FileHandler {
    private static final int CELL_SIZE = 50;
    public static final Color[] COLORS = {
            new Color(255, 0, 0),
            new Color(0, 0, 255),
            new Color(0, 255, 0),
            new Color(255, 165, 0),
            new Color(255, 0, 255),
            new Color(0, 255, 255),
            new Color(255, 192, 203),
            new Color(255, 255, 0),
            new Color(128, 0, 0),
            new Color(0, 128, 0),
            new Color(0, 0, 128),
            new Color(128, 0, 128),
            new Color(128, 128, 0),
            new Color(0, 128, 128),
            new Color(240, 128, 128),
            new Color(255, 140, 0),
            new Color(218, 112, 214),
            new Color(176, 224, 230),
            new Color(255, 235, 205),
            new Color(173, 216, 230),
            new Color(221, 160, 221),
            new Color(144, 238, 144),
            new Color(255, 160, 122),
            new Color(102, 205, 170),
            new Color(135, 206, 235),
            new Color(219, 112, 147)

    };

    public static Input readInputFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("../test/input/" + filename))) {
            String firstLine = reader.readLine();
            if (firstLine == null || firstLine.trim().isEmpty()) {
                throw new IOException("Format error: Please input rows, cols, and total pieces");
            }

            String[] dims = firstLine.trim().split("\\s+");
            if (dims.length != 3) {
                throw new IOException("Format error: rows, cols, and total pieces line must contain exactly 3 numbers");
            }

            try {
                int N = Integer.parseInt(dims[0]);
                int M = Integer.parseInt(dims[1]);
                int P = Integer.parseInt(dims[2]);

                if (N <= 0 || M <= 0 || P <= 0) {
                    throw new IOException("Invalid input: rows, cols, and total pieces cannot be a negative number");
                }

                String mode = reader.readLine().trim();

                char[][] grid = new char[N][M];

                if (mode.equals("DEFAULT")) {
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < M; j++) {
                            grid[i][j] = '.';

                        }
                    }
                } else if (mode.equals("CUSTOM")) {
                    for (int i = 0; i < N; i++) {
                        String line = reader.readLine();
                        if (line == null || line.length() != M) {
                            throw new IOException("Invalid custom configuration format");
                        }
                        for (int j = 0; j < M; j++) {
                            grid[i][j] = line.charAt(j) == 'X' ? '.' : ' ';
                        }
                    }
                } else {
                    throw new IOException("Invalid mode");
                }

                List<Piece> pieces = new ArrayList<>();
                List<String> currentLines = new ArrayList<>();
                String line;
                char currentId = '\0';
                int maxWidth = 0;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        char pieceId = line.charAt(0);

                        int currentLineWidth = 0;
                        for (char c : line.toCharArray()) {
                            if (c == pieceId) {
                                currentLineWidth++;
                            }
                        }
                        maxWidth = Math.max(maxWidth, currentLineWidth);

                        if (currentId == '\0') {
                            currentId = pieceId;
                        } else if (pieceId != currentId) {
                            if (currentLines.size() > N || maxWidth > M) {
                                throw new IOException("Piece " + pieceId + " size exceeds board dimensions");
                            }

                            pieces.add(Piece.linesToPiece(currentLines, currentId));
                            currentLines = new ArrayList<>();
                            currentId = pieceId;
                            maxWidth = currentLineWidth;
                        }

                        for (char c : line.toCharArray()) {
                            if (c != ' ' && c != currentId) {
                                throw new IOException("Invalid piece format: mixed Letters in same piece");
                            }
                        }

                        currentLines.add(line);
                    }
                }

                if (!currentLines.isEmpty()) {
                    int height = currentLines.size();
                    int maxDimension = Math.max(height, maxWidth);
                    if (maxDimension > Math.max(N, M)) {
                        throw new IOException("Piece " + currentId + " size exceeds board dimensions");
                    }

                    pieces.add(Piece.linesToPiece(currentLines, currentId));
                }
                if (pieces.size() != P) {
                    throw new IOException("Number of pieces (" + pieces.size() + ") does not match piece (" + P + ")");
                }

                Set<Character> uniqueIds = new HashSet<>();
                for (Piece piece : pieces) {
                    if (!uniqueIds.add(piece.getId())) {
                        throw new IOException("Duplicate piece ID found: " + piece.getId());
                    }
                }
                return new Input(N, M, P, mode, pieces, grid);

            } catch (NumberFormatException e) {
                throw new IOException("Invalid number format in rows, cols, and total pieces");
            }
        }
    }

    public static void saveText(String filename, String boardState, long executionTime, long iterations)
            throws IOException {
        File testFolder = new File("../test/output");
        if (!testFolder.exists()) {
            testFolder.mkdir();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("../test/output/" + filename))) {
            writer.println("Solution:");
            writer.println(boardState);
            writer.println("Execution time: " + executionTime + " ms");
            writer.println("Iterations: " + iterations);
        }
    }

    public static void saveImage(String outputFilename, String boardState, int N, int M, long executionTime,
            long cases) throws IOException {
        File testFolder = new File("../test/saved");
        if (!testFolder.exists()) {
            testFolder.mkdir();
        }
        int padding = 20; 
        int infoWidth = 200; 

        int imageWidth = M * CELL_SIZE + infoWidth + 3 * padding; 
        int imageHeight = N * CELL_SIZE + 2 * padding; 

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);

        String[] lines = boardState.split("\n");
        int boardX = padding;
        int boardY = padding;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                char c = lines[i].charAt(j);
                int cellX = boardX + j * CELL_SIZE;
                int cellY = boardY + i * CELL_SIZE;

                if (c != ' ' && c != '.') {
                    g2d.setColor(COLORS[c - 'A']);
                    g2d.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
                }

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2)); 
                g2d.drawRect(cellX, cellY, CELL_SIZE, CELL_SIZE);

                if (c != ' ' && c != '.') {
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    int textX = cellX + CELL_SIZE / 3;
                    int textY = cellY + 2 * CELL_SIZE / 3;
                    g2d.drawString(String.valueOf(c), textX, textY);
                }
            }
        }

        int infoX = boardX + M * CELL_SIZE + padding; 
        int infoY = boardY; 

        g2d.setColor(new Color(240, 240, 240)); 
        g2d.fillRect(infoX, infoY, infoWidth, N * CELL_SIZE);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        String executionInfo = "Execution time: " + executionTime + " ms";
        String casesInfo = "Total cases: " + cases;

        int textOffsetY = 20; 
        g2d.drawString(executionInfo, infoX + 10, infoY + textOffsetY);
        g2d.drawString(casesInfo, infoX + 10, infoY + textOffsetY + 20);
        g2d.dispose();
        ImageIO.write(image, "png", new File("../test/saved/" + outputFilename));
    }
}