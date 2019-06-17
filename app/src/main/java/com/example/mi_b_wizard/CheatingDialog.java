package com.example.mi_b_wizard;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.Data.Hand;
import com.example.mi_b_wizard.Data.ViewCards;

import java.util.List;

public class CheatingDialog extends Dialog implements android.view.View.OnClickListener  {
    private Activity c;
    private Dialog d;
    private Button btnOk;
    private LinearLayout linearLayoutCardView;
    private Hand handToShow = null;
    public boolean isActive;

    public CheatingDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        isActive = false;
        //linearLayoutCardView = findViewById(R.id.layoutEnemyCards);
        //handToShow = hand;
    }

    public Hand getHandToShow() {
        return handToShow;
    }

    public void setHandToShow(Hand handToShow) {
        this.handToShow = handToShow;
        //addCardsToView();
    }

    public void addCardsToView() {
        if(handToShow != null) {
            ViewCards cardview;
            List<Card> cards = handToShow.getHand();
            Card oneCard;
            linearLayoutCardView.removeAllViews();

            for (int i = 0; i<handToShow.getHandSize(); i++){
                oneCard = cards.get(i);
                cardview = new ViewCards(c, oneCard);
                linearLayoutCardView.addView(cardview.view);
            }
        }


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheating_dialog);
        this.setTitle("Title");
        btnOk = findViewById(R.id.btn_ok);
        linearLayoutCardView = findViewById(R.id.layoutEnemyCards);
        btnOk.setOnClickListener(this);
        Log.d("DEBUG", "in on create mf");
        addCardsToView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                //c.finish();
                isActive = false;

                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
