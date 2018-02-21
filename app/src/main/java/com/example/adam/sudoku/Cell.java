package com.example.adam.sudoku;

import java.util.Arrays;

/**
 * Created by Adam on 19/02/2018.
 */

public class Cell
{
    private int number;
    private int[] smallNumbers;

    public Cell(int num)
    {
        this.number = num;
        this.smallNumbers = new int[9];
    }

    public int getNumber()
    {
        return number;
    }

    public int[] getSmallNumbers()
    {
        return smallNumbers;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public void setSmallNumbers(int[] smallNumbers)
    {
        this.smallNumbers = smallNumbers;
    }
}
