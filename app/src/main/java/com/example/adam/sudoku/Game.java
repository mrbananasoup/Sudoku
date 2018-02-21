package com.example.adam.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        final SudokuBoard mediumGame = new MediumGame();
        mediumGame.createSudoku();
        mediumGame.makeHoles();
        mediumGame.printGame();
        //new
        mediumGame.calculateSmall();

        System.out.println(Arrays.toString(mediumGame.gameBoard[0][0].getSmallNumbers()));

        System.out.print("From con");
       
        System.out.println("This is from the admins");

        final Button button = findViewById(R.id.btnStart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.game);

                TextView[][] cells = new TextView[9][9];

                for(int i = 0; i < 9; i++)
                {
                    for(int j = 0; j < 9; j++)
                    {
                        String textID = "Cell" + i + "-" + j;
                        int resID = getResources().getIdentifier(textID, "id", getPackageName());
                        cells[i][j] = ((TextView)findViewById(resID));
                        System.out.println();
                        cells[i][j].setText("" + mediumGame.solutionBoard[i][j].getNumber());
                    }
                }
            }
        });

    }
}
