package com.example.adam.sudoku;

import java.util.Arrays;

/**
 * Created by Adam on 19/02/2018.
 */

public class Cell
{
    private int number;
    private int prevNumber = 0;
    private int[] smallNumbers;
    private boolean isEditable;

    public Cell(int num)
    {
        this.number = num;
        this.smallNumbers = new int[9];
        this.isEditable = false;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean getIsEditable()
    {
        return isEditable;
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
        this.prevNumber = this.number; // prev number set incorporated into set new number
        this.number = number;
    }

    public void setSmallNumbers(int[] smallNumbers)
    {
        this.smallNumbers = smallNumbers;
    }

    public int getPrevNumber() { return prevNumber; }

    public String smallNumToString()
    {
        String s = "";
        int count = 0;
        for (int i : smallNumbers)
        {
            if(i == 0) break;
            if(count == 4) s += "\n";
            s += i + ",";
            count++;
        }
        return s;
    }
}
