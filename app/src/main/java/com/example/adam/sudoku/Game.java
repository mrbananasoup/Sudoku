package com.example.adam.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    final SudokuBoard mediumGame = new MediumGame();
    TextView[][] cells = new TextView[9][9];

    public void startGame(View view)
    {
        mediumGame.createSudoku();
        mediumGame.makeHoles();
        mediumGame.printGame();
        mediumGame.calculateSmall();

        // Just for Test TODO remove later
        System.out.println(Arrays.toString(mediumGame.gameBoard[0][0].getSmallNumbers()));

        setContentView(R.layout.game);

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                String textID = "Cell" + i + "-" + j;
                int resID = getResources().getIdentifier(textID, "id", getPackageName());
                cells[i][j] = ((TextView)findViewById(resID));
                cells[i][j].setText("" + mediumGame.solutionBoard[i][j].getNumber());
            }
        }
    }

    private void refreshCells()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                String textID = "Cell" + i + "-" + j;
                int resID = getResources().getIdentifier(textID, "id", getPackageName());
                cells[i][j] = ((TextView)findViewById(resID));
                cells[i][j].setText("" + mediumGame.solutionBoard[i][j].getNumber());
            }
        }
    }

    public void btnCell(View view)
    {
        String id = view.getResources().getResourceName(view.getId());
        id = id.substring(id.length() - 3, id.length());
        mediumGame.solutionBoard[Integer.parseInt(id.substring(0,1))][Integer.parseInt(id.substring(2,3))].setNumber(0); // TODO test for cell on click remove later
        refreshCells();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }
}
