package com.example.adam.sudoku;

/**
 * Created by Adam on 19/02/2018.
 */

public class MediumGame extends SudokuBoard
{
    @Override
    protected void makeHoles()
    {
        double remainingSquares = 81;
        double remainingHoles = 35;

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                double holeChance = remainingHoles/remainingSquares;
                if(Math.random() <= holeChance)
                {
                    gameBoard[i][j].setNumber(0);
                    remainingHoles--;
                }
                remainingSquares--;
            }
        }
    }

    public void calculateSmall()
    {
        int[] smallNums = new int[9];
        int count = 0;

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                for(int k = 1; k < 10; k++)
                {
                    if(checkConflictSmall(i,j,k))
                    {
                        smallNums[count] = k;
                        count++;
                    }
                }
                gameBoard[i][j].setSmallNumbers(smallNums);
                smallNums = new int[9];
                count = 0;
            }
        }
    }

    private boolean checkConflictSmall(int x, int y, int num)
    {
        for(int i = 0; i <9; i++)
        {
            if(num == gameBoard[x][i].getNumber()) return false;
        }
        for(int i = 0; i < 9; i++)
        {
            if(num == gameBoard[i][y].getNumber()) return false;
        }
        int cornerX = 0;
        int cornerY = 0;

        if(x > 2)
            if(x > 5)
                cornerX = 6;
            else
                cornerX = 3;
        if(y > 2)
            if(y > 5)
                cornerY = 6;
            else
                cornerY = 3;

        for(int i = cornerX; i < 10 && i < cornerX+3; i++)
            for(int j = cornerY; j<10 && j < cornerY+3; j++)
                if(num == gameBoard[i][j].getNumber()) return false;
        return true;
    }
}
