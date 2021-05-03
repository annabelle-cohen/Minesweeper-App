package com.example.hw1_minsweeper.Logic;


import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Game implements Serializable {

    private Board board;
    private Clock clock;
    private int minesLeft;
    private int tilesLeft;
    int moves;
    private int totalTiles;
    private String timeTotalForCheck;
    private boolean isWon;
    private boolean isLos;
    private boolean isStartCover;
    private boolean isStartPlantMines;
    private boolean isClickable;
    private String timer;
    private String type;


    public Game(String type){
        this.board = new Board(Board.LevelType.valueOf(type));
        this.minesLeft=board.getMines();
        this.tilesLeft=board.getWidth()*board.getHeight();
        this.totalTiles=board.getWidth()*board.getHeight();
        this.clock=new Clock();
        this.isWon=false;
        this.isLos=false;
        this.isStartCover=false;
        this.isStartPlantMines=false;
        this.isClickable=true;
        this.type=type;
        this.moves=0;
    }

    public void startPunish(){
        this.setClickable(false);
        final Thread tr1 = new Thread(new Runnable() {

            @Override
            public void run() {
                int countTime = 100;
                while(isStartCover){
                    if(countTime==100) {
                        if(getBoard().getTilesForPunish().size()!=0) {
                            if (getBoard().getTilesForPunish().get(getBoard().getTilesForPunish().size() - 1).isBomb() && getBoard().getTilesForPunish().get(getBoard().getTilesForPunish().size() - 1).isFlagged()) {
                                increaseMines();
                            }
                        }
                        getBoard().startCoverTiles();
                        if(getTilesLeft() != getBoard().getWidth()*getBoard().getHeight() ) {
                            int tilesLeft = getTilesLeft() + 1;
                            setTilesLeft(tilesLeft);
                        }else{
                            setStartCover(false);
                        }
                        countTime=0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                     countTime++;

                }
            }
        });
        tr1.start();

        Thread tr2 = new Thread(new Runnable() {
            @Override
            public void run() {

               while(isStartPlantMines) {
                   if(getMinesLeft() != getTotalTiles()) {
                       getBoard().startPlantPunish();
                       if (getBoard().isSucceed()) {
                           increaseMines();
                       }
                   }else{
                       Log.d("in if","lose the game!!!!!!!!!!!");
                       setLos(true);
                   }
                   try {
                       Thread.sleep(5000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

               }
            }
        });

        tr2.start();
    }

    synchronized void increaseMines() {
            setMinesLeft(getMinesLeft() + 1);
    }

    public void stopPunish(){
        this.setClickable(true);
    }


    public void playTile(int position){
        if(isClickable) {
            if (!this.getBoard().getTileByPosition(position).isClicked() && this.getBoard().getTileByPosition(position).isEmpty()) {
                this.board.getTilesForPunish().add(this.getBoard().getTileByPosition(position));
                click(this.getBoard().getX(), this.getBoard().getY());
            } else if (!this.getBoard().getTileByPosition(position).isClicked() && this.getBoard().getTileByPosition(position).isBomb()) {
                this.getBoard().getTileByPosition(position).setClicked(true);
                onGameLost();
            }
            if (this.getBoard().getTileByPosition(position).getValue() > 0 && !this.getBoard().getTileByPosition(position).isClicked()) {
                this.board.getTilesForPunish().add(this.getBoard().getTileByPosition(position));
                this.getBoard().getTileByPosition(position).setRevealed(true);
                this.getBoard().getTileByPosition(position).updateTile();
                this.getBoard().getTileByPosition(position).setClicked(true);
                checkEnd();
            }

            this.moves++;
        }
    }

    public void playTileForLong(int position){
        if(isClickable) {
            if (!this.getBoard().getTileByPosition(position).isClicked() && !this.getBoard().getTileByPosition(position).isFlagged()) {
                this.board.getTilesForPunish().add(this.getBoard().getTileByPosition(position));
                this.getBoard().getTileByPosition(position).setFlagged(true);
                this.getBoard().getTileByPosition(position).setRevealed(true);
                this.getBoard().getTileByPosition(position).updateLongTile();
                this.getBoard().getTileByPosition(position).setClicked(true);
            } else if (this.getBoard().getTileByPosition(position).isClicked() && this.getBoard().getTileByPosition(position).isFlagged()) {
                if (this.getBoard().getTileByPosition(position).isBomb()) {
                    this.minesLeft++;
                }
                if(getBoard().getTilesForPunish().contains(this.getBoard().getTileByPosition(position))){
                    getBoard().getTilesForPunish().remove(this.getBoard().getTileByPosition(position));
                }
                this.getBoard().getTileByPosition(position).setFlagged(false);
                this.getBoard().getTileByPosition(position).setRevealed(false);
                this.getBoard().getTileByPosition(position).setClicked(false);
                this.getBoard().getTileByPosition(position).setDirty(false);
                this.getBoard().getTileByPosition(position).updateTile();
                this.tilesLeft++;
            }
            this.moves++;
            checkEnd();
        }
    }

    public boolean onGameLost() {

        for (int x = 0; x < this.getBoard().getWidth(); x++) {
            for (int y = 0; y < this.getBoard().getHeight(); y++) {
                this.getBoard().getTiles()[x][y].setRevealed(true);
                this.getBoard().getTiles()[x][y].updateTile();
                if (this.getBoard().getTiles()[x][y].isFlagged() && this.getBoard().getTiles()[x][y].isBomb()) {
                    this.getBoard().getTiles()[x][y].setClicked(false);
                    this.getBoard().getTiles()[x][y].updateTile();
                }
            }
        }
        this.setLos(true);

        return this.isLos;
    }

    public void click( int x , int y ){
        if( x >= 0 && y >= 0 && x < this.getBoard().getWidth() && y < this.getBoard().getHeight() && !this.getBoard().getTiles()[x][y].isClicked() ){
            this.getBoard().getTiles()[x][y].setRevealed(true);
            this.getBoard().getTiles()[x][y].updateTile();
            this.getBoard().getTiles()[x][y].setClicked(true);
            this.board.getTilesForPunish().add(this.getBoard().getTiles()[x][y]);

            if( this.getBoard().getTiles()[x][y].getValue() == 0 ){
             //   getBoard().getTilesForPunish().add(this.getBoard().getTiles()[x][y]);
                for( int xt = -1 ; xt <= 1 ; xt++ ){
                    for( int yt = -1 ; yt <= 1 ; yt++){
                        if( xt != yt ){
                            click(x + xt , y + yt);
                        }
                    }
                }
            }

        }
        checkEnd();
    }

    public boolean checkEnd() {
        for ( int x = 0 ; x < this.getBoard().getWidth() ; x++ ){
            for( int y = 0 ; y < this.getBoard().getHeight() ; y++ ){
                if( this.getBoard().getTiles()[x][y].isRevealed()){
                    if(!this.getBoard().getTiles()[x][y].isDirty()) {
                        this.tilesLeft--;
                        if(this.getBoard().getTiles()[x][y].isFlagged()&&  this.getBoard().getTiles()[x][y].isBomb()){
                            this.minesLeft--;
                        }
                        this.getBoard().getTiles()[x][y].setDirty(true);
                    }
                }

            }
        }
        if( this.getTilesLeft() == 0 && this.getMinesLeft() == 0 ){
            this.setWon(true);
        }

        return isWon;
    }

    public boolean checkByTime(String existScore, String NewScore) throws ParseException {
        String [] max_score_array = existScore.split(" ");
        String min_Time = max_score_array[1];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d1 = sdf.parse(min_Time);
        Date d2 = sdf.parse(NewScore);
        if(d2.getTime() - d1.getTime()>0){
            Long res=d2.getTime() - d1.getTime();
            Log.d("result",res+"");
            return false;
        }else if(d2.getTime() - d1.getTime()<0){
            Long res=d2.getTime() - d1.getTime();
            Log.d("result",res+"");
            return  true;
        }else{
           return checkByMoves(max_score_array[2],this.getMoves());
        }
    }


    private boolean checkByMoves(String existMoves, int moves) {
        int existMove=Integer.valueOf(existMoves);
        if(existMove>moves){
            return  true;
        }else if(existMove<moves){
            return false;
        }else{
            return false;
        }
    }

    public String StartClock(Clock clock)  {
        this.setTimeTotalForCheck(clock.toString());
        setTimer("Time:" + clock.toString());
        return timer;

    }

    public int getTotalTiles() {
        return totalTiles;
    }

    public void setTotalTiles(int totalTiles) {
        this.totalTiles = totalTiles;
    }

    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public void setTilesLeft(int tilesLeft) {
        this.tilesLeft = tilesLeft;
    }

    public boolean isStartCover() {
        return isStartCover;
    }

    public void setStartCover(boolean startCover) {
        isStartCover = startCover;
    }

    public boolean isStartPlantMines() {
        return isStartPlantMines;
    }

    public void setStartPlantMines(boolean startPlantMines) {
        isStartPlantMines = startPlantMines;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }
    public int getMoves() {
        return moves;
    }
    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public void Run(Clock clock){
        this.clock.run();
    }

    public Clock getClock(){
        return this.clock;
    }

    public boolean isWon() {
        return isWon;
    }

    public boolean isLos() {
        return isLos;
    }
    public String getType() {
        return type;
    }

    public Board getBoard() {
        return board;
    }


    public int getMinesLeft() {
        return minesLeft;
    }


    public int getTilesLeft() {
        return tilesLeft;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public void setLos(boolean los) {
        isLos = los;
    }

    public String getTimeTotalForCheck() {
        return timeTotalForCheck;
    }

    public void setTimeTotalForCheck(String timeTotalForCheck) {
        this.timeTotalForCheck = timeTotalForCheck;
    }
}
