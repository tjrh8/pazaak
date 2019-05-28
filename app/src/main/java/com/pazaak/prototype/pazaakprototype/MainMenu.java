package com.pazaak.prototype.pazaakprototype;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ProgressBar;

public class MainMenu extends AppCompatActivity
{
    public boolean cards[];// = new boolean[18]; //KEVIN Testing
    private static final int DECK_BUILDER = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        if(cards == null) {
            cards = new boolean[18];
            for (int i = 0; i < 18; i++) {
                cards[i] = false;
            }
        }//KEVIN testing

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Button toGameMenu = findViewById(R.id.bStartGame);
        final Button toDeckBuilder = findViewById(R.id.bDeckBuilder);
        final Button toTutorial = findViewById(R.id.bStartTutorial);

        toGameMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /* Goes straight to AI difficulty
                ** No multiplayer options
                */
                Intent iGameMenu = new Intent(MainMenu.this, SetAIDifficulty.class);
                iGameMenu.putExtra("cards", cards);
                MainMenu.this.startActivity(iGameMenu);
            }
        });
        toTutorial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent iTutorial = new Intent(MainMenu.this, Walkthrough.class);
                MainMenu.this.startActivity(iTutorial);
            }
        });
        toDeckBuilder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                findViewById(R.id.loadingMain).setVisibility(View.VISIBLE);
                Intent iDeckBuilder = new Intent(MainMenu.this, DeckBuilderV2.class);

                iDeckBuilder.putExtra("cards", cards); //KEVIN testing
                /*MainMenu.this.startActivity(iDeckBuilder);
                cards = iDeckBuilder.getBooleanArrayExtra("cards");*/
                MainMenu.this.startActivityForResult(iDeckBuilder, DECK_BUILDER);
                findViewById(R.id.loadingMain).setVisibility(View.GONE);
                //startActivityForResult(new Intent(Intent));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DECK_BUILDER && resultCode == Activity.RESULT_OK)
        {
            boolean temp[] = data.getBooleanArrayExtra("cards");
            /*for(int i = 0; i < 18; i++)
                cards[i] = temp[i];*/
            System.arraycopy(temp, 0, cards, 0, 18);
        }
    }
}
