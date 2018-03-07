package com.example.adam.sudoku;

/**
 * Created by Adam on 19/02/2018.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game extends AppCompatActivity {
    SudokuBoard sudokuBoard;
    TextView[][] cells = new TextView[9][9];
    TextView[][] smallNums = new TextView[9][9];
    View currView = null;
    String currCell = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startGame(View view)
    {
        sudokuBoard = initSudokuBoard();
        sudokuBoard.createSudoku();
        sudokuBoard.makeHoles();
        sudokuBoard.printGame();
        sudokuBoard.calculateSmall();

        setContentView(R.layout.smalltest);

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                String textID = "Cell" + i + "-" + j;
                int resID = getResources().getIdentifier(textID, "id", getPackageName());
                String smallID = "small" + i + "-" + j;
                int smallResID = getResources().getIdentifier(smallID, "id", getPackageName());

                cells[i][j] = ((TextView)findViewById(resID));
                cells[i][j].setText("" + sudokuBoard.gameBoard[i][j].getNumber());
                smallNums[i][j] = ((TextView)findViewById(smallResID));
                //if(sudokuBoard.gameBoard[i][j].getNumber() != 0) cells[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.GRAY)); // TODO for old game screen
                if(sudokuBoard.gameBoard[i][j].getNumber() != 0)
                {
                    cells[i][j].setBackgroundColor(Color.GRAY);
                    smallNums[i][j].setText("0000\n00000");
                }
                else
                {
                    smallNums[i][j].setText("" + sudokuBoard.gameBoard[i][j].smallNumToString());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openHelp(View view){
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    public void btnHelp(View view)
    {
        setContentView(R.layout.help);
    }

    private void refreshCells(int x, int y)
    {
        cells[x][y].setText("" + sudokuBoard.gameBoard[x][y].getNumber());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btnCell(View view)
    {
        String id = view.getResources().getResourceName(view.getId());
        id = id.substring(id.length() - 3, id.length());
        if(sudokuBoard.gameBoard[Integer.parseInt(id.substring(0,1))][Integer.parseInt(id.substring(2,3))].getIsEditable())
        {
            currCell = id;
            setSelected(view);
        }

        System.out.println("------------------------");
        System.out.println(sudokuBoard.gameBoard[Integer.parseInt(id.substring(0,1))][Integer.parseInt(id.substring(2,3))].smallNumToString());
        //System.out.println(Arrays.toString(sudokuBoard.gameBoard[Integer.parseInt(id.substring(0,1))][Integer.parseInt(id.substring(2,3))].getSmallNumbers()));  // Just for Test TODO remove later
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSelected(View view)
    {
        //if(currView != null) currView.setBackgroundTintList(null); todo for old game screen
        if(currView != null) currView.setBackground(null);
        if(currView == view)
        {
            //currView.setBackgroundTintList(null); todo for ild game screen
            currView.setBackground(null);
            currView = null;
            currCell = "";
        }
        else
        {
            //view.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); todo for old game screen
            view.setBackgroundColor(Color.BLUE);
            currView = view;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addNum(View view)
    {
        if(currCell != "")
        {
            String num = view.getResources().getResourceName(view.getId());
            sudokuBoard.gameBoard[Integer.parseInt(currCell.substring(0,1))][Integer.parseInt(currCell.substring(2,3))].setNumber(Integer.parseInt(num.substring(num.length() - 1)));
            refreshCells(Integer.parseInt(currCell.substring(0,1)),Integer.parseInt(currCell.substring(2,3)));
            currCell = "";
            //currView.setBackgroundTintList(null); todo for old game screen
            currView.setBackground(null);
            currView = null;

            LastChanged(currCell.substring(0,1),currCell.substring(2,3));

            sudokuBoard.calculateSmall();
            refreshSmall();
        }
    }

    private void refreshSmall()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(sudokuBoard.gameBoard[i][j].getNumber() != 0)
                {
                    smallNums[i][j].setText("0000\n00000");
                }
                else
                {
                    smallNums[i][j].setText("" + sudokuBoard.gameBoard[i][j].smallNumToString());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    protected SudokuBoard initSudokuBoard() {
        String difficulty = getDifficulty();

        switch(difficulty) {
            case "easy":
                return new EasyGame();
            case "medium":
                return new MediumGame();
            case "hard":
                return new HardGame();
            default:
                return new MediumGame();
        }
    }

    protected String getDifficulty() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("pref_difficulty", "");

    }


    // debug function to log undo/redo changes
    private void debugLogUndo(int cellX, int cellY){
        Log.d("UNDO", "cell changing from " + sudokuBoard.gameBoard[cellX][cellY].getNumber() +
        " to " + sudokuBoard.gameBoard[cellX][cellY].getPrevNumber());
    }

    private void debugLogRedo(int cellX, int cellY){
        Log.d("REDO", "cell changing from " + sudokuBoard.gameBoard[cellX][cellY].getPrevNumber() +
                " to " + sudokuBoard.gameBoard[cellX][cellY].getNumber());
    }

    // sudokuBoard.gameBoard[x][y]

    // TO DO:
    // 1. linked list containing the X/Y of the last 5 changes
        // change in sudokuboard or game?
    // 2. function that triggers the undo / redo based on button press
        // add the buttons to the overlay that appears when you tap a cell
    // 3. visible counter for number of undo/redos available
        // visible at top of board? side of board?

    // UNDO / REDO

    class Point {   // pointer for the 2d linkedlist references
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(){};
    }

    private final int undoConst = 5;

    public void LastChanged(String x, String y){
        // stores a reference to the last cell that was changed by the user
        Lx = Integer.parseInt(x);
        Ly = Integer.parseInt(y);
    }

    public int Lx, Ly;


    public boolean Undo(Cell getCell){
        if ((getCell.getPrevNumber() == 0) || (getCell.getPrevNumber() == getCell.getNumber())){
            return false;   //return 0 if no undo can be made
        }
        addToUndoFirst(Lx, Ly);
        return true;
    }

    public boolean Redo(Cell getCell){
        if (redoList.size() == 0){
            return false; // return 0 if there is nothing to redo
        }
        return true;

    }

    LinkedList<Point> undoList = new LinkedList<>();

    private void addToUndoLast(int x, int y){
        undoList.addLast(new Point(x,y));
    }
    private void addToUndoFirst(int x, int y){
        undoList.push(new Point(x,y));
        // push new element to front of list
    }

    private void addToUndo(int x, int y){
        if (undoList.size() >= undoConst) {
            // if already 5, remove oldest pointer
            undoList.removeLast();
            addToUndoFirst(x,y);
        }
    }

    LinkedList<Point> redoList = new LinkedList<>();

    private void addToRedoLast(int x, int y){
        undoList.addLast(new Point(x,y));
    }

    private void addToRedoFirst(int x, int y){
        redoList.push(new Point(x,y));
        // push new element to front of list
    }

    private void addToRedo(int x, int y){
        if (redoList.size() >= undoConst){
            // if already 5, remove oldest pointer
            redoList.removeLast();
            addToRedoFirst(x,y);
        }
    }


}
