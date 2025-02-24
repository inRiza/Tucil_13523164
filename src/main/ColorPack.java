package main;

public class ColorPack {
    private static final String[] COLOR_PACK = {
            "\u001B[31m", // Red
            "\u001B[32m", // Green
            "\u001B[33m", // Yellow
            "\u001B[34m", // Blue
            "\u001B[35m", // Magenta
            "\u001B[36m", // Cyan
            "\u001B[91m", // Bright Red
            "\u001B[92m", // Bright Green
            "\u001B[93m", // Bright Yellow
            "\u001B[94m", // Bright Blue
            "\u001B[95m", // Bright Magenta
            "\u001B[96m", // Bright Cyan
            "\u001B[31;1m", // Bold Red
            "\u001B[32;1m", // Bold Green
            "\u001B[33;1m", // Bold Yellow
            "\u001B[34;1m", // Bold Blue
            "\u001B[35;1m", // Bold Magenta
            "\u001B[36;1m", // Bold Cyan
            "\u001B[31;2m", // Dim Red
            "\u001B[32;2m", // Dim Green
            "\u001B[33;2m", // Dim Yellow
            "\u001B[34;2m", // Dim Blue
            "\u001B[35;2m", // Dim Magenta
            "\u001B[36;2m", // Dim Cyan
            "\u001B[37m", // White
            "\u001B[90m" // Gray
    };
    private static final String RESET = "\u001B[0m";

    public String colorize(char pieceId) {
        if (pieceId == '.')
            return String.valueOf(pieceId);
        if (pieceId == ' ')
            return " ";
        return COLOR_PACK[pieceId - 'A'] + pieceId + RESET;
    }
}