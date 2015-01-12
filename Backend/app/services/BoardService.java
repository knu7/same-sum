package services;

import java.util.Random;

import services.interfaces.IBoardService;
import models.Number;

public class BoardService implements IBoardService{
	
	
	public Number[][] getNewBoard(int rows, int columns, int maxValue) {
        
		Number[][] board = new Number[rows][columns];	
        Random rnd = new Random();
        for (int row = 0; row < rows; row++){
            for (int column = 0; column < columns; column++){
                board[row][column] = new Number(row, column, rnd.nextInt(maxValue) + 1);
            }
        }
        
        return board;
    }
}
