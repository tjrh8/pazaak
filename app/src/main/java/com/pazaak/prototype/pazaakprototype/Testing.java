package com.pazaak.prototype.pazaakprototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Testing extends AppCompatActivity
{
    boolean testToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        testToggle = true;
        //toggle(R.id.testingButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        setButton();
    }
    protected void setButton()
    {
        final ImageButton testButton = findViewById(R.id.testingButton);
        toggle(testButton.getId());
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toggle(testButton.getId());
            }

        });
    }
    protected void toggle(int buttonID)
    {
        ImageButton toggle = findViewById(buttonID);
        testToggle = !testToggle;
        if(testToggle)
            toggle.setBackgroundColor(getResources().getColor(R.color.brightGreen, null));
        else
            toggle.setBackgroundColor(getResources().getColor(R.color.brightRed, null));
    }
}
