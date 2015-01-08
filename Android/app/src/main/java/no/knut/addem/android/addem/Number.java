package no.knut.addem.android.addem;

import java.io.Serializable;

public class Number implements Serializable{

    public int row, column, number;

    public Number(int row, int column, int number){
        this.row = row;
        this.column = column;
        this.number = number;

    }

}
