package com.example.adam.sudoku;

/**
 * Created by Adam on 19/02/2018.
 */

import java.util.*;

public abstract class SudokuBoard
{
    protected static final int BOARD_WIDTH = 9;
    protected static final int BOARD_HEIGHT = 9;

    protected Cell[][] solutionBoard;
    protected Cell[][] gameBoard;

    private int operations;



    public SudokuBoard()
    {
        solutionBoard = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
        gameBoard = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                solutionBoard[i][j] = new Cell(0);
            }
        }
    }

    public void createSudoku()
    {
        fillCells(0,0); // fill in all cells of solution board
        for(int i = 0; i < 9; i++) // copy solution board to game board
        {
            for(int j = 0; j < 9; j++)
            {
                gameBoard[i][j] = new Cell(solutionBoard[i][j].getNumber());
            }
        }
    }

    protected boolean fillCells(int x, int y)
    {
        int nextX = x;
        int nextY = y;
        int[] numRange = {1,2,3,4,5,6,7,8,9};
        Random r = new Random();
        int temp = 0;
        int curr = 0;
        int top = numRange.length;

        for(int i=top-1;i>0;i--)
        {
            curr = r.nextInt(i);
            temp = numRange[curr];
            numRange[curr] = numRange[i];
            numRange[i] = temp;
        }

        for(int i = 0; i < numRange.length;i++)
        {
            if(checkConflict(x, y, numRange[i]))
            {
                solutionBoard[x][y] =  new Cell(numRange[i]);
                if(x == 8)
                {
                    if(y == 8) return true;
                    else
                    {
                        nextX = 0;
                        nextY = y + 1;
                    }
                }
                else
                {
                    nextX = x + 1;
                }
                if(fillCells(nextX, nextY)) return true;
            }
        }
        solutionBoard[x][y] = new Cell(0);
        return false;
    }

    protected boolean checkConflict(int x, int y, int num)
    {
        for(int i = 0; i <9; i++)
        {
            if(num == solutionBoard[x][i].getNumber()) return false;
        }
        for(int i = 0; i < 9; i++)
        {
            if(num == solutionBoard[i][y].getNumber()) return false;
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
                if(num == solutionBoard[i][j].getNumber()) return false;
        return true;
    }

    protected abstract void makeHoles();

    protected abstract void calculateSmall();

    public boolean checkSolution()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(gameBoard[i][j].getNumber() != solutionBoard[i][j].getNumber()) return false;
            }
        }
        return true;
    }

    public void printSolution()
    {
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++) System.out.print(solutionBoard[i][j].getNumber() + "  ");
            System.out.println();
        }
        System.out.println();
    }

    public void printGame()
    {
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++) System.out.print(gameBoard[i][j].getNumber() + "  ");
            System.out.println();
        }
        System.out.println();
    }

}