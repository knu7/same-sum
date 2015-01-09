package no.knut.addem.android.addem.core;

import java.io.Serializable;

public class Number implements Serializable{

    private int row, column, value;

    public Number(int row, int column, int value){
        this.row = row;
        this.column = column;
        this.value = value;

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getValue() {
        return value;
    }

}
