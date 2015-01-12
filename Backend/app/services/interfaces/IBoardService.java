package services.interfaces;

import models.Number;

public interface IBoardService {
	
	Number[][] getNewBoard(int rows, int columns, int maxValue);
}
