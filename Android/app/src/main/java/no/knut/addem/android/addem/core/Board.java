package no.knut.addem.android.addem.core;


import java.util.Random;

public class Board {

    private int rows;
    private int columns;
    private Number[] numbers;
    private int maxValue;

    public Board(int rows, int columns, int maxValue) {
        this.rows = rows;
        this.columns = columns;
        this.maxValue = maxValue;
        this.numbers = new Number[rows * columns];

        Random rnd = new Random();
        int index = 0;
        for (int row = 0; row < rows; row++){
            for (int column = 0; column < columns; column++){
                numbers[index++] = new Number(row, column, rnd.nextInt(maxValue) + 1);
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public Number[] getNumbers() {
        return numbers;
    }
}
