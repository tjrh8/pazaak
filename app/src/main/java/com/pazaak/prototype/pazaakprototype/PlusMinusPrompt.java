package com.pazaak.prototype.pazaakprototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PlusMinusPrompt extends DialogFragment
{
    private final int val[] = {0};
    AlertDialog myDiag;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alterDialogBuilder = new AlertDialog.Builder(getActivity());
        alterDialogBuilder.setMessage(R.string.pmPrompt);
        alterDialogBuilder.setPositiveButton(R.string.plusPrompt, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                val[0] = Card.PLUS;
            }
        });
        alterDialogBuilder.setNegativeButton(R.string.minusPrompt, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                val[0] = Card.MINUS;
            }
        });
        myDiag = alterDialogBuilder.create();
        //dialog.show();
        //myDiag = (AlertDialog) onCreateDialog(savedInstanceState);
        //myDiag.show();
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.pmPrompt)
//                .setPositiveButton(R.string.plusPrompt, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        val = Card.PLUS;
//
//                    }
//                })
//                .setNegativeButton(R.string.minusPrompt, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                       val = Card.MINUS;
//                    }
//                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }

    public int returnType()
    {
        return val[0];
    }
    public void shower()
    {
        //myDiag = (AlertDialog) onCreateDialog(null);
        myDiag.show();
    }
}