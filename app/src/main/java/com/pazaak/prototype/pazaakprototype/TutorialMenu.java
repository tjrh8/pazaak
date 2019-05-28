package com.pazaak.prototype.pazaakprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialMenu extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_menu);

        final Button toRulebook = findViewById(R.id.bRulebook);
        final Button toWalkthrough = findViewById(R.id.bStartWalkthrough);


        toRulebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent iRulebooks = new Intent(TutorialMenu.this, Rulebook.class);
                TutorialMenu.this.startActivity(iRulebooks);
            }
        });
        toWalkthrough.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent iWalkthrough = new Intent(TutorialMenu.this, Walkthrough.class);
                TutorialMenu.this.startActivity(iWalkthrough);
            }
        });
    }
}
