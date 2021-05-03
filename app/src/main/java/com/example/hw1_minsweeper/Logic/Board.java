package com.example.hw1_minsweeper.Logic;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board implements Serializable{

    static enum LevelType {
        Beginner, Intermediate, Expert;
    }

    ;


    Tile[][] tiles;
    private final int MINES_BEGINNER =7;
    private final int MINES_INTERMEDIATE = 20;
    private final int MINES_EXPERT = 40;
    private final int WIDTH_BEGINNER = 7;
    private final int WIDTH_INTERMEDIATE = 10;
    private final int WIDTH_EXPERT = 12;
    private final int LENGTH_EXPERT = 14;
    private ArrayList<Tile> tilesForPunish;
    private  int width;
    private int height;
    private int mines;
    private int minesAddition;
    private int position;
    private int updateMinesLeft;
    private boolean isSucceed=false;


    private int x,y;

    public Board(LevelType levelType){
        switch (levelType) {
            case Beginner:
                width=WIDTH_BEGINNER;
                height=WIDTH_BEGINNER;
                mines=MINES_BEGINNER;
                break;
            case Intermediate:
                width=WIDTH_INTERMEDIATE;
                height=WIDTH_INTERMEDIATE;
                mines=MINES_INTERMEDIATE;
                break;
            case Expert:
                width=WIDTH_EXPERT;
                height=LENGTH_EXPERT;
                mines=MINES_EXPERT;
                break;
            default:
                break;

        }
        tilesForPunish=new ArrayList<>();
        tiles=new Tile[width][height];
        initBoard(tiles,width,height,mines);
    }


    private void initBoard(Tile[][] tiles, int width, int height, int mines) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Tile();
            }
        }
        plantMines(tiles,mines,width,height);
        calculateNeiCell(tiles,width,height);
    }


    public Tile[][] calculateNeiCell(Tile[][] tiles, int width, int length) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                tiles[x][y].setValue(getNeighbourNumber(tiles, x, y, width, length));
                if (tiles[x][y].getValue() != 0) {
                    tiles[x][y].setEmpty(false);
                }
            }
        }

        return tiles;
    }

    public static int getNeighbourNumber(Tile[][] tiles, int x, int y, int width, int height) {
        if (tiles[x][y].isBomb()) {
            //-1 for mine
            return -1;
        }

        int count = 0;

        if (isMineAt(tiles, x - 1, y + 1, width, height)) count++; // top-left
        if (isMineAt(tiles, x, y + 1, width, height)) count++; // top
        if (isMineAt(tiles, x + 1, y + 1, width, height)) count++; // top-right
        if (isMineAt(tiles, x - 1, y, width, height)) count++; // left
        if (isMineAt(tiles, x + 1, y, width, height)) count++; // right
        if (isMineAt(tiles, x - 1, y - 1, width, height)) count++; // bottom-left
        if (isMineAt(tiles, x, y - 1, width, height)) count++; // bottom
        if (isMineAt(tiles, x + 1, y - 1, width, height)) count++; // bottom-right

        return count;
    }

    private static boolean isMineAt(Tile[][] tiles, final int x, final int y, final int width, final int height) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            if (tiles[x][y].isBomb()) {
                return true;
            }
        }
        return false;
    }

    public void plantMines(Tile[][] tiles, int numberOfMinesNeeded, int width, int length) {
        Random rn = new Random();
        while (numberOfMinesNeeded > 0) {
            int x = rn.nextInt(width);
            int y = rn.nextInt(length);
            if (tiles[x][y].isEmpty() == true) {
                tiles[x][y].setBomb(true);
                tiles[x][y].setEmpty(false);
                numberOfMinesNeeded -= 1;
            }
        }
    }


    public Tile getTileByPosition(int position) {
        this.position=position;
        this.x = position % this.width;
        setX(x);
        this.y = position / this.width;
        setY(y);
        return tiles[x][y];
    }

    public void startCoverTiles(){

        if(tilesForPunish.size()!=0){
            if(getTilesForPunish().get(tilesForPunish.size()-1).isFlagged()){
                if(getTilesForPunish().get(tilesForPunish.size()-1).isBomb()){
                }
                getTilesForPunish().get(tilesForPunish.size()-1).setFlagged(false);
                getTilesForPunish().get(tilesForPunish.size()-1).setRevealed(false);
                getTilesForPunish().get(tilesForPunish.size()-1).setDirty(false);
                getTilesForPunish().get(tilesForPunish.size()-1).setClicked(false);
                getTilesForPunish().get(tilesForPunish.size()-1).updateTile();
            }else{
                getTilesForPunish().get(tilesForPunish.size()-1).setClicked(false);
                getTilesForPunish().get(tilesForPunish.size()-1).setRevealed(false);
                getTilesForPunish().get(tilesForPunish.size()-1).setDirty(false);
                getTilesForPunish().get(tilesForPunish.size()-1).updateTile();
            }
            tilesForPunish.remove(tilesForPunish.size()-1);
        }
    }

    public void startPlantPunish(){
        setSucceed(false);
        Random rn = new Random();
        int x = rn.nextInt(width);
        int y = rn.nextInt(height);
        if(!tiles[x][y].isRevealed()&&!tiles[x][y].isBomb()&&!tiles[x][y].isClicked()){
            tiles[x][y].setBomb(true);
            tiles[x][y].setEmpty(false);
           setSucceed(true);

        }
        calculateNeiCell(tiles,width,height);
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public void setSucceed(boolean succeed) {
        isSucceed = succeed;
    }


    public ArrayList<Tile> getTilesForPunish() {
        return tilesForPunish;
    }

    public void setTilesForPunish(ArrayList<Tile> tilesForPunish) {
        this.tilesForPunish = tilesForPunish;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    private void setY(int y) {
        this.y=y;
    }

    private void setX(int x) {
        this.x=x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMines() {
        return mines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
