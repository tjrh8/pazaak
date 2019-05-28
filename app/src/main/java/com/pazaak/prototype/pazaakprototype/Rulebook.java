package com.pazaak.prototype.pazaakprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rulebook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulebook);

        final Button toWalkthrough = findViewById(R.id.bToWalkthrough);
        final Button toMainMenu = findViewById(R.id.bBackToMain);

        toMainMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent iMainMenu = new Intent(Rulebook.this, MainMenu.class);
                Rulebook.this.startActivity(iMainMenu);
            }
        });
        toWalkthrough.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent iWalkthrough = new Intent(Rulebook.this, Walkthrough.class);
                Rulebook.this.startActivity(iWalkthrough);
            }
        });
    }
}
