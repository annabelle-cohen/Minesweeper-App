package com.example.hw1_minsweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FullScoreTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_score_table);

        BegginersFragment begginersFragment = new BegginersFragment();
        IntermediateFragment intermediateFragment=new IntermediateFragment();
        ExpertFragment expertFragment=new ExpertFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.TopTableFragment, begginersFragment)
                .add(R.id.MiddleTableFragment, intermediateFragment)
                .add(R.id.BottomTableFragment, expertFragment)
                .commit();
            Button buttonBack = findViewById(R.id.Back);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startBackToFirstdActivity();
                }
            });

    }

    private void startBackToFirstdActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
