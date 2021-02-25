package com.example.MemoryGame;

import java.util.Random;

public class GameModel {

    public static final int EASY_SIZE = 3;
    public static final int HARD_SIZE = 4;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;
    public static final int GAME_END = 1;

    private int lives;
    private int score;
    private int[][] game;
    private int currentSize;
    private int currentGoal;

    private Random random;


    public GameModel(){
        game = new int[EASY_SIZE][EASY_SIZE];
        lives = 3;
        score = 0;
        currentSize = EASY_SIZE;
        random = new Random();
    }



    public void makeTable() {

        game = new int[currentSize][currentSize];


        for(int row = 0; row < currentSize; row++){
            for(int col = 0; col < currentSize; col++){
                int tempNumber = random.nextInt(10);
                if(tempNumber < 5)
                    game[row][col] = 0;
                else
                    game[row][col] = 1;
            }
        }

        currentGoal = scanGridForGoal();
    }

    public int play(int row, int col){
        int response = 0;


        if(game[row][col] == 1){
            score++;
            game[row][col] = SUCCESS;
            response = SUCCESS;
        }
        else{
            lives--;
            game[row][col] = ERROR;
            response = ERROR;
        }

        if(score == currentGoal || lives == 0){
            response = GAME_END;

        }

        return response;

    }

    private int scanGridForGoal(){
        int goal = 0;

        for(int row = 0; row < currentSize; row++){
            for(int col = 0; col < currentSize; col++){
                if(game[row][col] == 1)
                    goal++;
            }
        }

        return goal;
    }

    public int getCurrentSize(){
        return currentSize;
    }

    public void setCurrentSize(int size){

        this.currentSize = size;
    }

    public int[][] getGrid(){
        return game;
    }

    public int getNumberOfLives(){
        return lives;
    }

    public int getScore(){
        return score;
    }



    public void resetGame() {
        lives = 3;
        score = 0;
        this.currentSize = currentSize;
        makeTable();
    }


}
