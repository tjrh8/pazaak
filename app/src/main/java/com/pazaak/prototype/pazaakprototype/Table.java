package com.pazaak.prototype.pazaakprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Table extends AppCompatActivity
{
    final int END_TURN = 0;
    final int STAND = 1;
    final int PLAY_CARD = 2;
    private GameAI aiForGame;
    private final static int ROUNDS_NEEDED_TO_WIN = 3;
    private int roundsWon[];
    //Card[] MainDeck = new Card[40];
    List<Card> MainDeck;
    final int MAX_CARDS_IN_DECK = 40;
    String p1Count, p2Count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.MainDeck = new ArrayList<Card>();
        this.aiForGame = new GameAI(setDiff());
        this.roundsWon = new int[2];
        this.roundsWon[0] = 0;
        this.roundsWon[1] = 0;
        setContentView(R.layout.activity_table);
        final int[] p1CardsPlayed = {0};
        final int[] p2CardsPlayed = {0};

        final Card player1Hand[] = assignCards();


        final Random generator = new Random();
        final Card[] board1 = new Card[9];
        final Card[] board2 = new Card[9];


        final int[] p1Value = {0};
        p1Value[0] = 0;
        final int[] p2Value = {0};
        p2Value[0] = 0;
        final boolean[] yourTurn = {true};
        final boolean[] p1Stand = {false};
        final boolean[] p2Stand = {false};

        final boolean[] p1PlayedACardThisTurn = {false};

        for (int i = 0; i < MAX_CARDS_IN_DECK; i++)
        {
            MainDeck.add(new Card(Card.MAIN, (i + 1) % 11));
        }


        final ImageView board1Slots[] = {findViewById(R.id.p1Slot0), (findViewById(R.id.p1Slot1)), findViewById(R.id.p1Slot2), findViewById(R.id.p1Slot3), findViewById(R.id.p1Slot4),
                findViewById(R.id.p1Slot5), findViewById(R.id.p1Slot6), findViewById(R.id.p1Slot7), findViewById(R.id.p1Slot8)};


        final ImageView board2Slots[] = {findViewById(R.id.p2Slot0), findViewById(R.id.p2Slot1), findViewById(R.id.p2Slot2), findViewById(R.id.p2Slot3), findViewById(R.id.p2Slot4),
                findViewById(R.id.p2Slot5), findViewById(R.id.p2Slot6), findViewById(R.id.p2Slot7), findViewById(R.id.p2Slot8)};


        final int finalP2CardsPlayed = p2CardsPlayed[0];
        final int finalP2Value = p2Value[0];
        final boolean finalP2Stand = p2Stand[0];
        final boolean finalP2Stand1 = p2Stand[0];
        final TextView p1CurrentScore = findViewById(R.id.p1ScoreCounter);
        final TextView p2CurrentScore = findViewById(R.id.p2ScoreCounter);

        //Initial Start for player 1's board
        Card firstCard = (MainDeck.remove(generator.nextInt(this.MainDeck.size())));
        board1[0] = firstCard;
        board1Slots[0].setImageResource(firstCard.getImage());
        p1Value[0] = firstCard.getValue();
        p1Count = Integer.toString(p1Value[0]);
        p1CurrentScore.setText(p1Count);
        p1CardsPlayed[0]++;

        final Button endTurn = (Button) (findViewById(R.id.bEndTurn));

        endTurn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                /* time interval between endTurn clicks edit*/
                endTurn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endTurn.findViewById(R.id.bEndTurn).setEnabled(true);
                    }
                }, 2000);
                /*
                 */

                if (p2Stand[0] == false)
                {

                    p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
                    p2Count = Integer.toString(p2Value[0]);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            p2CurrentScore.setText(p2Count);
                        }
                    }, 1000);
                    p2CardsPlayed[0]++;

                    List<Card> copyOfMainDeck = new ArrayList<Card>(MainDeck);

                    final Card p2CardToPlay = aiForGame.getCard(p2Value[0], copyOfMainDeck);
                    if (p2CardToPlay != null)
                    {
                        if (p2CardToPlay.getType() == Card.PM)
                        {
                            if (aiForGame.choosePlusOrMinus(p2Value[0], p2CardToPlay) == GameAI.PLUS)
                            {
                                p2Value[0] += p2CardToPlay.getValue();
                            }
                            else
                            {
                                p2Value[0] += p2CardToPlay.getValue() * -1;
                            }
                        }
                        else
                        {
                            p2Value[0] += p2CardToPlay.getValue();
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
                                board2Slots[p2CardsPlayed[0]].setImageResource(p2CardToPlay.getImage());
                                p2CardsPlayed[0]++;
                            }
                        }, 1000);
                    }
                    p2Stand[0] = aiForGame.shouldStand(p2Value[0], MainDeck);
                    p2Count = Integer.toString(p2Value[0]);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            p2CurrentScore.setText(p2Count);
                        }
                    }, 1000);

                    yourTurn[0] = true;
                }
                else
                {
                    yourTurn[0] = true;
                }

//                if (p2Value[0] < 16)
//                {
//                    p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
//                    p2Count = Integer.toString(p2Value[0]);
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            p2CurrentScore.setText(p2Count);
//                        }
//                    } , 1000);
//                    p2CardsPlayed[0]++;
//                    yourTurn[0] = true;
//                }
//                else
//                {
//                    p2Stand[0] = true;
//                    yourTurn[0] = true;
//                }

                if (yourTurn[0] == true && p1Stand[0] == false)
                {
                    p1Value[0] = p1Turn(p1Value[0], p1CardsPlayed[0], board1Slots);
                    p1Count = Integer.toString(p1Value[0]);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            p1CurrentScore.setText(p1Count);
                        }
                    } , 2000);



                    p1CardsPlayed[0]++;
                    yourTurn[0] = false;


                }
                p1PlayedACardThisTurn[0] = false;
            }
        });

        final Button bStand1 = (Button) (findViewById(R.id.bStand));

        bStand1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                p1Stand[0] = true;
                while (p1Stand[0] == true && p2Stand[0] == false)
                {
                    p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
                    p2Count = Integer.toString(p2Value[0]);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            p2CurrentScore.setText(p2Count);

                        }
                    }, 1000);

                    p2CardsPlayed[0]++;

                    List<Card> copyOfMainDeck = new ArrayList<Card>(MainDeck);

                    final Card p2CardToPlay = aiForGame.getCard(p2Value[0], copyOfMainDeck);
                    if (p2CardToPlay != null)
                    {
                        if (p2CardToPlay.getType() == Card.PM)
                        {
                            if (aiForGame.choosePlusOrMinus(p2Value[0], p2CardToPlay) == GameAI.PLUS)
                            {
                                p2Value[0] += p2CardToPlay.getValue();
                            }
                            else
                            {
                                p2Value[0] += p2CardToPlay.getValue() * -1;
                            }
                        }
                        else
                        {
                            p2Value[0] += p2CardToPlay.getValue();
                        }


                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                board2Slots[p2CardsPlayed[0]].setImageResource(p2CardToPlay.getImage());
                                p2CardsPlayed[0]++;
                            }
                        }, 1000);



                    }
                    p2Stand[0] = aiForGame.shouldStand(p2Value[0], MainDeck);
                    p2Count = Integer.toString(p2Value[0]);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            p2CurrentScore.setText(p2Count);
                        }
                    }, 1000);


                }
                boolean shouldReset = checkifEnd(p1Stand[0], p2Stand[0], p1Value[0], p2Value[0]);
                if (shouldReset == true)
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < board1Slots.length; i++)
                            {
                                board1Slots[i].setImageResource(R.drawable.empty);
                                board2Slots[i].setImageResource(R.drawable.empty);

                            }
                            MainDeck.clear();
                            for (int i = 0; i < MAX_CARDS_IN_DECK; i++)
                            {
                                MainDeck.add(new Card(Card.MAIN, (i + 1) % 11));
                            }
                            p1Value[0] = 0;
                            p2Value[0] = 0;
                            p1Stand[0] = false;
                            p2Stand[0] = false;
                            p1CardsPlayed[0] = 0;
                            p2CardsPlayed[0] = 0;
                            for (int i = 0; i < board1.length; i++)
                            {
                                board1[i] = null;
                                board2[i] = null;
                            }

                            board1[0] = MainDeck.remove(generator.nextInt(MainDeck.size()));
                            board1Slots[0].setImageResource(board1[0].getImage());
                            p1CardsPlayed[0]++;
                            p1Value[0] = board1[0].getValue();
                            p1CurrentScore.setText(Integer.toString(p1Value[0]));
                            p2CurrentScore.setText(Integer.toString(p2Value[0]));
                        }
                    },4000);



                }
            }


        });



        /*
         * Decided to help out here a bit :) wanted to get familiar with the java file
         * Created/Displayed a temporary hand for player 1
         * Feel free to modify/expand/delete
         */

        /* generates random values for card retrieval in board1 slots from 0 - (board1.length - 1)*/
        //int randomCardValue = generator.nextInt(board1.length);

        final ImageButton board1hand1 = findViewById(R.id.p1Hand1);
        final ImageButton board1hand2 = findViewById(R.id.p1Hand2);
        final ImageButton board1hand3 = findViewById(R.id.p1Hand3);
        final ImageButton board1hand4 = findViewById(R.id.p1Hand4);

        /* Sets images of random card obtained into empty hand slots */

        board1hand1.setImageResource(player1Hand[0].getImage());
        //randomCardValue = generator.nextInt(board1.length);
        board1hand2.setImageResource(player1Hand[1].getImage());
        //randomCardValue = generator.nextInt(board1.length);
        board1hand3.setImageResource(player1Hand[2].getImage());
        //randomCardValue = generator.nextInt(board1.length);
        board1hand4.setImageResource(player1Hand[3].getImage());





        //Added as of Nov. 4th

        //TODO fix hand card 1
        board1hand1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (p1PlayedACardThisTurn[0] == false)
                {


                    p1Value[0] = p1PlayCard(p1Value[0], p1CardsPlayed[0], player1Hand[0], board1Slots);
                    p1Count = Integer.toString(p1Value[0]);

                    p1CurrentScore.setText(p1Count);
                    board1hand1.setImageResource(R.drawable.empty_dark);
                    p1CardsPlayed[0]++;

//                    if (p2Value[0] < 16)
//                    {
//                        p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
//                        p2CardsPlayed[0]++;
//                    }
//                    else
//                    {
//                        p2Stand[0] = true;
//                    }
//                    yourTurn[0] = true;

                    p1PlayedACardThisTurn[0] = true;
                    board1hand1.setEnabled(false);
                }


            }
        });

        //TODO fix hand card 2
        board1hand2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (p1PlayedACardThisTurn[0] == false)
                {
                    p1Value[0] = p1PlayCard(p1Value[0], p1CardsPlayed[0], player1Hand[1], board1Slots);
                    p1Count = Integer.toString(p1Value[0]);
                    p1CurrentScore.setText(p1Count);
                    board1hand2.setImageResource(R.drawable.empty_dark);
                    p1CardsPlayed[0]++;

//                    if (p2Value[0] < 16)
//                    {
//                        p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
//                        p2CardsPlayed[0]++;
//                    }
//                    else
//                    {
//                        p2Stand[0] = true;
//                    }
//                    yourTurn[0] = true;

                    p1PlayedACardThisTurn[0] = true;
                    board1hand2.setEnabled(false);
                }

            }
        });

        //TODO fix hand card 3
        board1hand3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (p1PlayedACardThisTurn[0] == false)
                {
                    p1Value[0] = p1PlayCard(p1Value[0], p1CardsPlayed[0], player1Hand[2], board1Slots);
                    p1Count = Integer.toString(p1Value[0]);
                    p1CurrentScore.setText(p1Count);
                    board1hand3.setImageResource(R.drawable.empty_dark);
                    p1CardsPlayed[0]++;

//                    if (p2Value[0] < 16)
//                    {
//                        p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
//                        p2CardsPlayed[0]++;
//                    }
//                    else
//                    {
//                        p2Stand[0] = true;
//                    }
//                    yourTurn[0] = true;

                    p1PlayedACardThisTurn[0] = true;

                    board1hand3.setEnabled(false);
                }

            }
        });

        //TODO fix hand card 4
        board1hand4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (p1PlayedACardThisTurn[0] == false)
                {
                    p1Value[0] = p1PlayCard(p1Value[0], p1CardsPlayed[0], player1Hand[3], board1Slots);
                    p1Count = Integer.toString(p1Value[0]);
                    p1CurrentScore.setText(p1Count);
                    board1hand4.setImageResource(R.drawable.empty_dark);
                    p1CardsPlayed[0]++;

//                    if (p2Value[0] < 16)
//                    {
//                        p2Value[0] = p2EndTurn(p2Value[0], p2CardsPlayed[0], board2Slots);
//                        p2CardsPlayed[0]++;
//                    }
//                    else
//                    {
//                        p2Stand[0] = true;
//                    }
//                    yourTurn[0] = true;

                    p1PlayedACardThisTurn[0] = true;

                    board1hand4.setEnabled(false);
                }

            }
        });

    }

    protected int p1PlayCard(int currentValue, int cardsPlayed, Card cardToPlay, ImageView board[])
    {
        board[cardsPlayed].setImageResource(cardToPlay.getImage());
        int val = 0;

        if (cardToPlay.getType() == Card.PM)
        {
            //PlusMinusPrompt myPrompt = new PlusMinusPrompt();
            //myPrompt.shower();
            int type;
            type = PMType(cardToPlay.getValue());
            //type = myPrompt.returnType();
            System.out.println("Card Value Type" + type);
            if (type == Card.PLUS)
            {
                val = cardToPlay.getValue();
            }
            else
            {
                val = cardToPlay.getValue() * -1;
            }
        }
        else
        {
            val = cardToPlay.getValue();
        }

        return currentValue + val;
    }

    protected void handleMessage(Message mesg)
    {
        throw new RuntimeException();
    }
    protected int PMType(int cardToPlayValue)
    {
        final int val[] = {0};
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(getLayoutInflater().inflate(R.layout.activity_pmpopup, null))
                .create();
        dialog.show();
        ImageButton plus = dialog.findViewById(R.id.plusSign);
        ImageButton minus = dialog.findViewById(R.id.minusSign);

        plus.setImageResource(new Card(Card.PLUS, cardToPlayValue).getImage());
        minus.setImageResource(new Card(Card.MINUS, cardToPlayValue).getImage());


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val[0] = Card.PLUS;
                dialog.dismiss();
                handleMessage(null);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val[0] = Card.MINUS;
                dialog.dismiss();
                handleMessage(null);
            }
        });

        try
        {
            Looper.loop();
        }
        catch (RuntimeException e)
        {
            //Empty here
        }
        return val[0];
    }

    //Returns the new value of the board
    protected int p1Turn(int currentValue, int cardsPlayed, ImageView board[])
    {
        int returnedValue = 0;
        returnedValue = p1EndTurn(currentValue, cardsPlayed, board);

        return returnedValue;
    }

       /*
    * Original Code
    *
    protected int p1EndTurn(int currentValue, int cardsPlayed, ImageView board[])
    {
        Random getMainDeckCard = new Random();
        //Card cardToDraw = MainDeck[(getMainDeckCard.nextInt(40) + 1) % 11];
        Card cardToDraw = MainDeck.remove(getMainDeckCard.nextInt(MainDeck.size()));
        board[cardsPlayed].setImageResource(cardToDraw.getImage());
        return currentValue + cardToDraw.getValue();


    } */

    protected int p1EndTurn(int currentValue, final int cardsPlayed, final ImageView board[])
    {
        Random getMainDeckCard = new Random();
        //Card cardToDraw = MainDeck[(getMainDeckCard.nextInt(40) + 1) % 11];
        final Card cardToDraw = MainDeck.remove(getMainDeckCard.nextInt(MainDeck.size()));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                board[cardsPlayed].setImageResource(cardToDraw.getImage());
            }
        }, 2000);

        return currentValue + cardToDraw.getValue();


    }

    /*
    * Original Code
    *
    protected int p2EndTurn(int curVal, int cardsPlayed, ImageView board[])
    {


        Random getMainDeckCard = new Random();
        //Card cardToDraw = MainDeck[(getMainDeckCard.nextInt(40) + 1) % 11];
        Card cardToDraw = MainDeck.remove(getMainDeckCard.nextInt(MainDeck.size()));
        board[cardsPlayed].setImageResource(cardToDraw.getImage());
        return cardToDraw.getValue() + curVal;
    } */

    protected int p2EndTurn(int curVal, final int cardsPlayed, final ImageView board[])
    {

        /*
        Random getMainDeckCard = new Random();
        //Card cardToDraw = MainDeck[(getMainDeckCard.nextInt(40) + 1) % 11];
        Card cardToDraw = MainDeck.remove(getMainDeckCard.nextInt(MainDeck.size()));
        board[cardsPlayed].setImageResource(cardToDraw.getImage());
        return cardToDraw.getValue() + curVal; */

        final Handler handler = new Handler();
        final Random getMainDeckCard = new Random();
        final Card cardToDraw = MainDeck.remove(getMainDeckCard.nextInt(MainDeck.size()));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Card cardToDraw = MainDeck[(getMainDeckCard.nextInt(40) + 1) % 11];
                board[cardsPlayed].setImageResource(cardToDraw.getImage());
            }
        }, 1000);

        return cardToDraw.getValue() + curVal;
    }
    public boolean checkifEnd(boolean stand1, boolean stand2, int val1, int val2)
    {
        boolean shouldReset = false;
        if (stand1 && stand2)
        {
            if ((val1 <= 20 && val1 > val2) || (val1 <= 20 && val2 > 20))
            {
                Context context = getApplicationContext();
                CharSequence text = "YouWin";
                int duration = Toast.LENGTH_SHORT;

                final Toast toast = Toast.makeText(context, text, duration);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                },1000);
                this.roundsWon[0]++;


            }
            else if ((val2 > val1 && val2 <= 20) || (val1 > 20 & val2 <= 20))
            {
                Context context = getApplicationContext();
                CharSequence text = "YouLose";
                int duration = Toast.LENGTH_SHORT;

                final Toast toast = Toast.makeText(context, text, duration);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                }, 1000);
                this.roundsWon[1]++;

            }
            else
            {
                Context context = getApplicationContext();
                CharSequence text = "Tie";
                int duration = Toast.LENGTH_SHORT;

                final Toast toast = Toast.makeText(context, text, duration);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                }, 1000);

            }
            if (roundsWon[0] >= ROUNDS_NEEDED_TO_WIN)
            {
                Context endContext = getApplicationContext();
                CharSequence winMatchText = "You've Won the match!";
                int duration = Toast.LENGTH_LONG;

                final Toast toast = Toast.makeText(endContext, winMatchText, duration);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                }, 1000);
            }
            else if (roundsWon[1] >= ROUNDS_NEEDED_TO_WIN)
            {
                Context endContext = getApplicationContext();
                CharSequence loseMatchText = "You've Lost the match.";
                int duration = Toast.LENGTH_LONG;

                final Toast toast = Toast.makeText(endContext, loseMatchText, duration);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.show();
                    }
                }, 1000);
            }
            else
            {
                shouldReset = true;
            }
        }
        return shouldReset;
    }

    public Card[] assignCards()
    {
        Bundle extra = getIntent().getExtras();
        List<Card> playerDeck = new ArrayList<Card>();
        if (extra.containsKey("cards") && (extra.getBooleanArray("cards")) != null)
        {
            playerDeck.addAll(Arrays.asList(Card.getSideDeck(extra.getBooleanArray("cards"))));
        }

        Random getRandomHandGen = new Random();
        Card handToReturn[] = new Card[4];
        int cardsInHand = 0;
        while (cardsInHand < 4)
        {
            handToReturn[cardsInHand] = playerDeck.remove(getRandomHandGen.nextInt(playerDeck.size()));
            cardsInHand++;
        }

        return handToReturn;
    }

    protected int setDiff()
    {
        Bundle extra = getIntent().getExtras();
        int diff = extra.getInt("diff");
        return diff;
    }
}
