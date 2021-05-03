package com.example.hw1_minsweeper.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.hw1_minsweeper.R;

public class TileAdapter extends BaseAdapter {

    private Board mBoard;
    private Context context;
    private int[] icons ={R.drawable.number_0,R.drawable.number_1,R.drawable.number_2,R.drawable.number_3,R.drawable.number_4,R.drawable.number_5
            ,R.drawable.number_6,R.drawable.number_7,R.drawable.number_8,R.drawable.bomb_exploded,R.drawable.bomb_normal,R.drawable.button,R.drawable.flag
    };
    LayoutInflater layoutInflater;

    public TileAdapter(Context context,Board mBoard){
        this.mBoard=mBoard;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mBoard.getWidth() * mBoard.getHeight();
    }

    @Override
    public Object getItem(int position) {
        return mBoard.getTileByPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView=convertView;
        if(convertView==null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = layoutInflater.inflate(R.layout.mines_layout, null);
        }
        ImageView img = (ImageView) gridView.findViewById(R.id.imageView);
        img.setImageResource(icons[mBoard.getTileByPosition(position).getImg()]);
        int sizeH = parent.getHeight() / mBoard.getWidth();
        int sizeW = parent.getHeight() / mBoard.getHeight();
        img.setFitsSystemWindows(true);
        GridView.LayoutParams params = new GridView.LayoutParams(sizeH, sizeW);
        gridView.setLayoutParams(params);
        return gridView;
    }

}
