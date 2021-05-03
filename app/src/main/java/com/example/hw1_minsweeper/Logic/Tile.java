package com.example.hw1_minsweeper.Logic;


import java.io.Serializable;

public class Tile implements Serializable{

        private boolean isBomb;
        private boolean isEmpty;
        private boolean isClicked;
        private boolean isRevealed;
        private boolean isFlagged;
        private boolean isDirty;
        private final int ZERO=0;
        private final int ONE=1;
        private final int TWO=2;
        private final int THREE=3;
        private final int FOUR=4;
        private final int FIVE=5;
        private final int SIX=6;
        private final int SEVEN=7;
        private final int EIGHT=8;
        private final int NINE=9;
        private final int TEN=10;
        private final int ELEVEN=11;
        private final int TWELVE=12;
        private int value;
        private  int  img;



    public Tile(){
            isBomb=false;
            isEmpty=true;
            isClicked=false;
            isRevealed=false;
            isFlagged=false;
            isDirty=false;
            value=0;
            updateTile();
        }


        public int updateTile(){
            if(!this.isRevealed()&&!this.isClicked()){
                setImg(ELEVEN);
            }else if(this.isRevealed()&&this.getValue()>=0&&!this.isClicked()){
                checkWhichNumb(this.getValue());
            }else if(this.isRevealed()&&this.isBomb()&&this.isClicked()){
                setImg(NINE);
            }else if(!this.isClicked&&this.isBomb&&this.isRevealed){
                setImg(TEN);
            }
            return this.img;
        }
        public int updateLongTile(){
            setImg(TWELVE);
            return this.img;
        }

        private void checkWhichNumb(int value) {
            switch (value){
                case 0:
                    setImg(ZERO);
                    break;
                case 1:
                    setImg(ONE);
                    break;
                case 2:
                    setImg(TWO);
                    break;
                case 3:
                    setImg(THREE);
                    break;
                case 4:
                    setImg(FOUR);
                    break;
                case 5:
                    setImg(FIVE);
                    break;
                case 6:
                    setImg(SIX);
                    break;
                case 7:
                    setImg(SEVEN);
                    break;
                case 8:
                    setImg(EIGHT);
                    break;
            }
        }

        public boolean isBomb() {
            return isBomb;
        }

        public void setBomb(boolean bomb) {
            isBomb = bomb;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public boolean isRevealed() {
            return isRevealed;
        }

        public void setRevealed(boolean revealed) {
            isRevealed = revealed;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
        public boolean isFlagged() {
            return isFlagged;
        }
        public int getImg() {
        return img;
         }

         public void setImg(int pos){
                this.img=pos;
         }

        public void setFlagged(boolean flagged) {
            isFlagged = flagged;
        }
    public boolean isDirty() {
        return isDirty;
    }
    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

}
