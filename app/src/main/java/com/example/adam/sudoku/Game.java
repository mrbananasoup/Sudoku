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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

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
                if(sudokuBoard.gameBoard[i][j].getIsEditable() == false)
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

            //LastChanged(currCell.substring(0,1),currCell.substring(1,2));

            sudokuBoard.calculateSmall();
            refreshSmall();

            if(sudokuBoard.checkSolution() == true)
            {
                youHaveWon();
            }
        }
    }

    private void refreshSmall()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(sudokuBoard.gameBoard[i][j].getIsEditable() == false)
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

    private void youHaveWon()
    {
        System.out.println("YOU HAVE WON ------------------");
        setContentView(R.layout.win);
    }

    public void undo(View view)
    {
        System.out.println("UNDO CLICKED ___________________");
        //Undo(sudokuBoard.gameBoard[Lx][Ly]);
        setGameBoardUndo(pos);
    }

    public void redo(View view)
    {
        System.out.println("REDO CLICKED ___________________");
        setGameBoardRedo(pos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    protected SudokuBoard initSudokuBoard() {
        String difficulty = getDifficulty();

        System.out.println(difficulty + "------------------------");

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

    private void debugLogRedo(int cellX, int cellY) {
        Log.d("REDO", "cell changing from " + sudokuBoard.gameBoard[cellX][cellY].getPrevNumber() +
                " to " + sudokuBoard.gameBoard[cellX][cellY].getNumber());
    }

    // TO DO:
    // 1. linked list containing the X/Y of the last 5 changes
        // change in sudokuboard or game?
    // 2. function that triggers the undo / redo based on button press
        // add the buttons to the overlay that appears when you tap a cell
    // 3. visible counter for number of undo/redos available
        // visible at top of board? side of board?

    // UNDO / REDO created by James

    class Point {   // pointer for the 2d linkedlist references
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(){};
    }

    private final int undoConst = 5; // constant for max number of last undo/redo attempts stored
    int gsFirst, gsLast = 0;    //initialise as 0 : empty
    int pos = 0;       //initialise as 0 : default position

    public UndoRedoStack<Cell[][]> gameStates = new UndoRedoStack<>();

    public void copyToGameStates(Cell[][] board){
        if (gameStates.undoStack.size() >= undoConst){
            gameStates.pop();
            gameStates.push(board);
        } else {
            gameStates.push(board);
        }
    }

    public Cell[][] copyFromGameStates(){
        // not working yet
        Cell[][] board = null;

        return board;
    }

    public void setGameBoardUndo(int pos){
        // replaces the current sudoku board with a stored copy
        gameStates.undo();
        sudokuBoard.gameBoard = (Cell[][]) gameStates.undoStack.pop();
        // cast to cell[][] as you need a specific type
    }

    public void setGameBoardRedo(int pos){
        // replaces the current sudoku board with a stored copy
        gameStates.redo();
        sudokuBoard.gameBoard = (Cell[][]) gameStates.redoStack.pop();
    }

    public class UndoRedoStack<E> extends Stack<E>{
        private Stack undoStack;
        private Stack redoStack;

        //construct an empty undo redo stack
        public UndoRedoStack(){
            undoStack = new Stack();
            redoStack = new Stack();
        }

        //pushes and returns value at top of stack
        public E push(E value){
            super.push(value);
            undoStack.push("push");
            redoStack.clear();
            return value;
        }

        public E pop(){
            E value = super.pop();
            undoStack.push(value);
            undoStack.push("pop");
            redoStack.clear();
            return value;
        }

        public boolean canWeUndo(){
            return !undoStack.isEmpty();
        }

        // undoes the last move
        // can only undo if moves have been made
        public void undo(){
            if (!canWeUndo()){
                //throw new IllegalStateException();
            }
            Object action = undoStack.pop();
            if (action.equals("push")){
                E value = super.pop();
                redoStack.push(value);
                redoStack.push("push");
            } else {
                E value = (E) undoStack.pop();
                super.push(value);
                redoStack.push("pop");
            }

        }

        public boolean canWeRedo(){
            return !redoStack.isEmpty();
        }

        // redoes the last move
        // can only redo if moves have been undone
        public void redo(){
            if (!canWeRedo()){
                //throw new IllegalStateException();
            }
            Object action = redoStack.pop();
            if (action.equals("push")){
                E value = (E) redoStack.pop();
                super.push(value);
                undoStack.push("push");
            } else {
                E value = super.pop();
                undoStack.push(value);
                undoStack.push("pop");
            }
        }
    }



    /*
    ArrayList<Cell[][]> gameStates = new ArrayList<>();
    int gameState;  // empty int to refer to current position in the game list

    public int setGameState(int X) { this.gameState = X; return this.gameState;} ;

    public Cell[][] copyFromGameStates(Cell[][] boardCopy){
        // returns the boardCopy from the gameStates array list
        return boardCopy; //return multi-dimension array of cell

    }

    public void copyToGameStates(int pos, Cell[][] boardCopy){
        // stores a copy of the board to gameStates
        if (gsLast == 0){ gsLast = 1; }

        // if the last is at 5
        if (gsLast == 5) {
            // if list is full then shift list down
            for (int n = 0; n < 5; n++) {
                copyToGameStates(n++, gameStates.get(n));
            }
        }
        gameStates.add(boardCopy);
    }

    public void setGameBoard(int pos){
        // replaces the current sudoku board with a stored copy
        // call copyFromGameStates
        sudokuBoard.gameBoard = copyFromGameStates(gameStates.get(pos));
        // this line right here gets the gameState at position (pos)

    }

    public void UndoGame(int pos){
        // change game board to previous position
        // if not at lower limit (5)
        // call setGameBoard with position as parameter Pos
            // put check here

        // if the 'last' (oldest) entry is not the same count as the constant
        // AND position is not at Last

        //if gsLast is at 5 (the max) nothing will happen
        if(gsLast != undoConst && pos != gsLast){
            pos++;
            setGameBoard(pos);
        }

    }

    public void RedoGame(int pos){
        // change game board to next position
        // if not at upper limit (0)
        // call setGameBoard with position as parameter Pos
            // put check here

        // if gsFirst
        if(pos != 0){
            pos--;
            setGameBoard(pos);
        }

    }






     */



}
