package com.example.MemoryGame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener{

    private LinearLayout mainLayout;
    private RelativeLayout relativeLayout;
    private RelativeLayout bottomLayout;
    private RelativeLayout topLayout;
    private GridLayout gridLayout;
    private Button[][] buttons;
    private Button startButton;
    private Button resetButton;
    private Button gameButton;
    private CheckBox checkbox;
    private TextView headerView;
    private TextView livesView;
    private TextView countDownView;
    private TextView characterView;
    private EditText editText;
    private ImageView imgView;
    private GameModel gameModel;
    private CountDownTimer countDownTimer;
    private View.OnClickListener buttonClickHandler;
    private RelativeLayout.LayoutParams relativeParams;

    private int SCREEN_WIDTH = 1080;
    private int SCREEN_HEIGHT = 1920;
    private int EASY_SIZE = gameModel.EASY_SIZE;
    private int HARD_SIZE = gameModel.HARD_SIZE;
    private int SUCCESS = gameModel.SUCCESS;
    private int ERROR =gameModel.ERROR;
    private int GAME_END = gameModel.GAME_END;
    private static int HEADER_ID = 10;
    private static int CHECKBOX_ID = 9;
    private static int IMG_ID = 8;
    private static int LIVES_ID = 7;
    private static int GRID_ID = 6;
    private static int START_ID = 5;
    private static int COUNTDOWN_ID = 4;
    private static int RESET_ID = 3;
    private String CHARACTER = "O";



    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameModel = new GameModel();

        addLayout();

        //SETTING LISTENER
        checkbox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    gameModel.setCurrentSize(EASY_SIZE);
                    resetGame();
                }
                else{
                    gameModel.setCurrentSize(HARD_SIZE);
                    resetGame();
                }
            }
        });


    }

    @Override
    public void onClick(View v){
        if(v.getId() == START_ID)
            startGame();
        else if(v.getId() == RESET_ID)
            resetGame();
        else {
            int[] pos = getPosition(v);
            int row = pos[0];
            int col = pos[1];
            play(v, row, col);
        }
    }

    private int[] getPosition(View v) {
        int[] pos = new int[2];

        for (int row = 0; row < gameModel.getCurrentSize(); row++) {
            for (int col = 0; col < gameModel.getCurrentSize(); col++) {
                if(buttons[row][col].getId() == v.getId()){
                    pos[0] = row;
                    pos[1] = col;
                    return pos;
                }
                else
                    continue;
            }
        }


        return pos;
    }

    private void addLayout() {


        // LAYOUT **HEADER**
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // PARAMS
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearParams.setMargins(30, 30, 30, 30);
        mainLayout.setLayoutParams(linearParams);

        // CREATING AND ADDING HEADER
        headerView = new TextView(this);
        headerView.setId(HEADER_ID);
        headerView.setText("Memory Game");
        headerView.setTextAppearance(this, R.style.HeaderStyle);
        headerView.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.addView(headerView);
        //#####################################################################//

        // LAYOUT **CHECKBOX, HEART IMAGE VIEW AND LIVES TEXTVIEW**
        topLayout = new RelativeLayout(this);

        // CREATING AND ADDING CHECKBOX
        checkbox = new CheckBox(this);
        checkbox.setText("Hard mode");
        checkbox.setEnabled(true);
        checkbox.setId(CHECKBOX_ID);
        topLayout.addView(checkbox);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        relativeParams.setMargins(0, 0, 30, 0);

        // CREATING AND ADDING LIVES TEXTVIEW
        livesView = new TextView(this);
        livesView.setId(LIVES_ID);
        livesView.setTextAppearance(this, R.style.TextStyle);
        livesView.setText(Integer.toString(gameModel.getNumberOfLives()));
        topLayout.addView(livesView, relativeParams);



        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.LEFT_OF, LIVES_ID);


        // CREATING AND ADDING IMAGE VIEW
        imgView = new ImageView(this);
        imgView.setImageResource(R.drawable.heart);
        imgView.setLayoutParams(relativeParams);
        imgView.getLayoutParams().width = 100;
        imgView.getLayoutParams().height = 100;
        topLayout.addView(imgView, relativeParams);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        //CREATING AND ADDING TEXTVIEW
        characterView = new TextView(this);
        characterView.setText("Remember this: " + CHARACTER);
        topLayout.addView(characterView, relativeParams);

        mainLayout.addView(topLayout);


        //#####################################################################//

        // LAYOUT **BUTTON GRID**
        relativeLayout = new RelativeLayout(this);

        gridLayout = new GridLayout(this);
        gridLayout.setId(GRID_ID);


        // PARAMS

        // CREATING AND ADDING BUTTON ARRAY
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);


        gameModel.makeTable();

        int width = point.x - 60;
        int buttonWidth = width / EASY_SIZE;

        gridLayout.setRowCount(EASY_SIZE);
        gridLayout.setColumnCount(EASY_SIZE);

        buttons = new Button[EASY_SIZE][EASY_SIZE];
        for (int row = 0; row < EASY_SIZE; row++) {
            for (int col = 0; col < EASY_SIZE; col++) {
                buttons[row][col] = new Button(this);
                String strId = "1" + row + "" + col;
                int id = Integer.parseInt(strId);
                buttons[row][col].setId(id);
                buttons[row][col].setEnabled(false);
                buttons[row][col].setOnClickListener(this);
                gridLayout.addView(buttons[row][col], buttonWidth, buttonWidth);
            }
        }

        mainLayout.addView(gridLayout);
        //#####################################################################//

        // LAYOUT **START BUTTON, COUNTDOWN AND RESET BUTTON**
        bottomLayout = new RelativeLayout(this);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        // CREATING AND ADDING START BUTTON
        startButton = new Button(this);
        startButton.setId(START_ID);
        startButton.setText("Start Game");
        startButton.setOnClickListener(this);

        bottomLayout.addView(startButton, relativeParams);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // CREATING AND ADDING COUNTDOWN TEXT
        countDownView = new TextView(this);
        countDownView.setId(COUNTDOWN_ID);
        countDownView.setText("COUNTDOWN");
        countDownView.setTextSize(18);
        bottomLayout.addView(countDownView, relativeParams);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        //CREATING AND ADDING RESET BUTTON
        resetButton = new Button(this);
        resetButton.setId(RESET_ID);
        resetButton.setText("Reset Game");
        resetButton.setOnClickListener(this);
        bottomLayout.addView(resetButton, relativeParams);

        mainLayout.addView(bottomLayout);
        //#####################################################################//

        setContentView(mainLayout);


    }

    public void showToast(String str){
        Toast toast;
        toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
        toast.show();
    }

    public void startGame() {
        // disable checkbox
        checkbox.setEnabled(false);


        startButton.setEnabled(false);

        // SETTING THE X AND O IN THE GRID
        int[][] game = gameModel.getGrid();
        for (int row = 0; row < gameModel.getCurrentSize(); row++) {
            for (int col = 0; col < gameModel.getCurrentSize(); col++) {
                if (game[row][col] == 0)
                    buttons[row][col].setText("X");
                else
                    buttons[row][col].setText("O");
            }
        }

        if (!checkbox.isChecked()) {

            // COUNTDOWN TIMER
            countDownTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) millisUntilFinished / 1000;
                    countDownView.setText(Integer.toString(seconds));

                }

                @Override
                public void onFinish() {
                    countDownView.setText("Go!");
                    enableAllButtons(true);
                    for (int row = 0; row < gameModel.getCurrentSize(); row++) {
                        for (int col = 0; col < gameModel.getCurrentSize(); col++) {
                            buttons[row][col].setTextSize(0);
                        }
                    }
                }
            }.start();

        }
        else {

            // COUNTDOWN TIMER
            countDownTimer  = new CountDownTimer(7000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) millisUntilFinished / 1000;
                    countDownView.setText(Integer.toString(seconds));
                }

                @Override
                public void onFinish() {
                    countDownView.setText("Go!");
                    enableAllButtons(true);
                    for (int row = 0; row < HARD_SIZE; row++) {
                        for (int col = 0; col < HARD_SIZE; col++) {
                            buttons[row][col].setTextSize(0);
                        }
                    }
                }
            }.start();
        }


    }

    public void replaceLayout(){
        mainLayout.removeView(bottomLayout);
        mainLayout.removeView(gridLayout);

        livesView.setText(Integer.toString(gameModel.getNumberOfLives()));

        // LAYOUT **BUTTON GRID*
        gridLayout = new GridLayout(this);
        gridLayout.setId(GRID_ID);


        // CREATING AND ADDING BUTTON ARRAY
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);


        int currentSize = gameModel.getCurrentSize();



        int width = point.x - 60;
        int buttonWidth = width / currentSize;

        gridLayout.setRowCount(currentSize);
        gridLayout.setColumnCount(currentSize);

        buttons = new Button[currentSize][currentSize];
        for (int row = 0; row < currentSize; row++) {
            for (int col = 0; col < currentSize; col++) {
                buttons[row][col] = new Button(this);
                String strId = "1" + row + "" + col;
                int id = Integer.parseInt(strId);
                buttons[row][col].setId(id);
                buttons[row][col].setEnabled(false);
                buttons[row][col].setOnClickListener(this);
                gridLayout.addView(buttons[row][col], buttonWidth, buttonWidth);
            }
        }


        mainLayout.addView(gridLayout);
        //#####################################################################//

        // LAYOUT **START BUTTON, COUNTDOWN AND RESET BUTTON**
        bottomLayout = new RelativeLayout(this);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        // CREATING AND ADDING START BUTTON
        startButton = new Button(this);
        startButton.setId(START_ID);
        startButton.setText("Start Game");
        startButton.setOnClickListener(this);

        bottomLayout.addView(startButton, relativeParams);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // CREATING AND ADDING COUNTDOWN TEXT
        countDownView = new TextView(this);
        countDownView.setId(COUNTDOWN_ID);
        countDownView.setText("COUNTDOWN");
        countDownView.setTextSize(18);
        bottomLayout.addView(countDownView, relativeParams);

        // PARAMS
        relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        //CREATING AND ADDING RESET BUTTON
        resetButton = new Button(this);
        resetButton.setId(RESET_ID);
        resetButton.setText("Reset Game");
        resetButton.setOnClickListener(this);
        bottomLayout.addView(resetButton, relativeParams);

        mainLayout.addView(bottomLayout);


    }

    private void enableAllButtons(boolean temp){
        if(temp){
            for (int row = 0; row < gameModel.getCurrentSize(); row++) {
                for (int col = 0; col < gameModel.getCurrentSize(); col++) {
                    buttons[row][col].setEnabled(true);
                }
            }
        }
        else{
            for (int row = 0; row < gameModel.getCurrentSize(); row++) {
                for (int col = 0; col < gameModel.getCurrentSize(); col++) {
                    buttons[row][col].setEnabled(false);
                }
            }
        }
    }


    private void resetGame(){

        if(!(countDownTimer == null)){
            countDownTimer.cancel();
        }

        gameModel.resetGame();
        replaceLayout();
        // call a new method that removes grid and bottom conditionally build them back up

        //addLayout();
        //enable the checkbox again
        checkbox.setEnabled(true);

    }

    private void play (View v, int row, int col){

        int response = gameModel.play(row, col);
        int error = ERROR;
        int success = SUCCESS;

        gameButton = (Button) v;
        int[][] game = gameModel.getGrid();

        if(response == success){
            gameButton.setTextSize(15);
            gameButton.setTextColor(Color.GREEN);
        }
        else if(response == error){
            livesView.setText(Integer.toString(gameModel.getNumberOfLives()));
            gameButton.setTextSize(15);
            gameButton.setTextColor(Color.RED);
        }
        else{

            int errorCount = 0;
            livesView.setText(Integer.toString(gameModel.getNumberOfLives()));
            for (int x = 0; x < gameModel.getCurrentSize(); x++) {
                for (int y = 0; y < gameModel.getCurrentSize(); y++) {
                    if(game[x][y] == 0 || game[x][y] == 1){
                        buttons[x][y].setTextColor(Color.BLACK);
                        buttons[x][y].setTextSize(15);
                    }
                    else if(game[x][y] == SUCCESS){
                        buttons[x][y].setTextSize(15);
                        buttons[x][y].setTextColor(Color.GREEN);
                    }
                    else if(game[x][y] == ERROR){
                        buttons[x][y].setTextSize(15);
                        buttons[x][y].setTextColor(Color.RED);
                    }
                    if(game[x][y] == ERROR){
                        errorCount++;
                    }
                }
            }

            if(errorCount > 2){
                showToast("YOU LOST :((");
            }
            else{
                showToast("YOU WIN!! :)");
            }

            // COUNTDOWN TIMER
            countDownTimer = new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) millisUntilFinished / 1000;
                    countDownView.setText("Game will reset in " + seconds + " seconds");
                }

                @Override
                public void onFinish() {
                    countDownView.setText("COUNTDOWN");
                    resetGame();
                }
            }.start();
        }

    }



}