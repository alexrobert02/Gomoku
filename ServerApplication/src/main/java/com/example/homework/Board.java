package com.example.homework;

class Board {
    private static final int SIZE = 15;
    private char[][] cells;

    public Board() {
        cells = new char[SIZE][SIZE];
        clear();
    }

    public void clear() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = '-';
            }
        }
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && cells[row][col] == '-';
    }

    public void makeMove(int row, int col, char symbol) {
        cells[row][col] = symbol;
    }

    public boolean isWinningMove(int row, int col) {
        char symbol = cells[row][col];

        // Check horizontally
        int count = 0;
        for (int c = col - 4; c <= col + 4; c++) {
            if (c >= 0 && c < SIZE && cells[row][c] == symbol) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        // Check vertically
        count = 0;
        for (int r = row - 4; r <= row + 4; r++) {
            if (r >= 0 && r < SIZE && cells[r][col] == symbol) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        // Check diagonally (top-left to bottom-right)
        count = 0;
        for (int d = -4; d <= 4; d++) {
            int r = row + d;
            int c = col + d;
            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && cells[r][c] == symbol) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        // Check diagonally (top-right to bottom-left)
        count = 0;
        for (int d = -4; d <= 4; d++) {
            int r = row - d;
            int c = col + d;
            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && cells[r][c] == symbol) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        return false;
    }

    public boolean isFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                sb.append(cells[row][col]).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}