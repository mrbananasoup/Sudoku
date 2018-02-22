package com.example.adam.sudoku;

/**
 * Created by Adam on 19/02/2018.
 */

public class HardGame extends SudokuBoard
{
    @Override
    protected void makeHoles()
    {
        double remainingSquares = 81;
        double remainingHoles = 50;

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                double holeChance = remainingHoles/remainingSquares;
                if(Math.random() <= holeChance)
                {
                    gameBoard[i][j].setNumber(0);
                    gameBoard[i][j].setEditable(true);
                    remainingHoles--;
                }
                remainingSquares--;
            }
        }
    }

    @Override
    protected void calculateSmall() {
        throw new UnsupportedOperationException();
    }
}
