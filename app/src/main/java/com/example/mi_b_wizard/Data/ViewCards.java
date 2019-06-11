package com.example.mi_b_wizard.Data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.mi_b_wizard.R;

public class ViewCards {
    public Card card;
    public ImageView view;
    public boolean isActive = false;


    public ViewCards(Context context, Card card) {
        this.card = card;
        this.view = new ImageView(context);
        this.updatePictureOfCard();


        //Calculates ImageView size from px to dp
        // ->needed for cards, to display them in same size on every screen (Smartphone, Tablet,...)
        final float scale = view.getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (65 * scale);
        int dpHeightInPx = (int) (90 * scale);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx,dpHeightInPx);
        view.setLayoutParams(layoutParams);
    }



    /**
     * To connect jpg Cards with Card Class
     */
    public void updatePictureOfCard() {
        if (this.card == null) {
            this.view.setBackground(this.view.getResources().getDrawable(R.drawable.back,null));
            return;

        }
        Drawable draw = this.view.getResources().getDrawable(R.drawable.back,null);

        if (this.card.getColour() == Colour.GREEN) {

            switch (this.card.getRank()) {
                case JESTER:
                    draw = this.view.getResources().getDrawable(R.drawable.green_jester,null);
                    break;
                case ONE:
                    draw = this.view.getResources().getDrawable(R.drawable.green1,null);
                    break;
                case TWO:
                    draw = this.view.getResources().getDrawable(R.drawable.green2,null);
                    break;
                case THREE:
                    draw = this.view.getResources().getDrawable(R.drawable.green3,null);
                    break;
                case FOUR:
                    draw = this.view.getResources().getDrawable(R.drawable.green4,null);
                    break;
                case FIVE:
                    draw = this.view.getResources().getDrawable(R.drawable.green5,null);
                    break;
                case SIX:
                    draw = this.view.getResources().getDrawable(R.drawable.green6,null);
                    break;
                case SEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.green7,null);
                    break;
                case EIGHT:
                    draw = this.view.getResources().getDrawable(R.drawable.green8,null);
                    break;
                case NINE:
                    draw = this.view.getResources().getDrawable(R.drawable.green9,null);
                    break;
                case TEN:
                    draw = this.view.getResources().getDrawable(R.drawable.green10,null);
                    break;
                case ELEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.green11,null);
                    break;
                case TWELVE:
                    draw = this.view.getResources().getDrawable(R.drawable.green12,null);
                    break;
                case THIRTEEN:
                    draw = this.view.getResources().getDrawable(R.drawable.green13,null);
                    break;
                case MAGICIAN:
                    draw = this.view.getResources().getDrawable(R.drawable.green_magician,null);
                    break;
                default:
                    draw = this.view.getResources().getDrawable(R.drawable.back,null);
                    break;
            }
        } else if (this.card.getColour() == Colour.BLUE) {
            switch (this.card.getRank()) {
                case JESTER:
                    draw = this.view.getResources().getDrawable(R.drawable.blue_jester,null);
                    break;
                case ONE:
                    draw = this.view.getResources().getDrawable(R.drawable.blue1,null);
                    break;
                case TWO:
                    draw = this.view.getResources().getDrawable(R.drawable.blue2,null);
                    break;
                case THREE:
                    draw = this.view.getResources().getDrawable(R.drawable.blue3,null);
                    break;
                case FOUR:
                    draw = this.view.getResources().getDrawable(R.drawable.blue4,null);
                    break;
                case FIVE:
                    draw = this.view.getResources().getDrawable(R.drawable.blue5,null);
                    break;
                case SIX:
                    draw = this.view.getResources().getDrawable(R.drawable.blue6,null);
                    break;
                case SEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.blue7,null);
                    break;
                case EIGHT:
                    draw = this.view.getResources().getDrawable(R.drawable.blue8,null);
                    break;
                case NINE:
                    draw = this.view.getResources().getDrawable(R.drawable.blue9,null);
                    break;
                case TEN:
                    draw = this.view.getResources().getDrawable(R.drawable.blue10,null);
                    break;
                case ELEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.blue11,null);
                    break;
                case TWELVE:
                    draw = this.view.getResources().getDrawable(R.drawable.blue12,null);
                    break;
                case THIRTEEN:
                    draw = this.view.getResources().getDrawable(R.drawable.blue13,null);
                    break;
                case MAGICIAN:
                    draw = this.view.getResources().getDrawable(R.drawable.blue_magician,null);
                    break;
                default:
                    draw = this.view.getResources().getDrawable(R.drawable.back,null);
                    break;
            }

        } else if (this.card.getColour() == Colour.YELLOW) {
            switch (this.card.getRank()) {
                case JESTER:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow_jester,null);
                    break;
                case ONE:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow1,null);
                    break;
                case TWO:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow2,null);
                    break;
                case THREE:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow3,null);
                    break;
                case FOUR:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow4,null);
                    break;
                case FIVE:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow5,null);
                    break;
                case SIX:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow6,null);
                    break;
                case SEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow7,null);
                    break;
                case EIGHT:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow8,null);
                    break;
                case NINE:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow9,null);
                    break;
                case TEN:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow10,null);
                    break;
                case ELEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow11,null);
                    break;
                case TWELVE:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow12,null);
                    break;
                case THIRTEEN:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow13,null);
                    break;
                case MAGICIAN:
                    draw = this.view.getResources().getDrawable(R.drawable.yellow_magician,null);
                    break;
                default:
                    draw = this.view.getResources().getDrawable(R.drawable.back,null);
                    break;
            }
        } else if (this.card.getColour() == Colour.RED) {
            switch (this.card.getRank()) {
                case JESTER:
                    draw = this.view.getResources().getDrawable(R.drawable.red_jester,null);
                    break;
                case ONE:
                    draw = this.view.getResources().getDrawable(R.drawable.red1,null);
                    break;
                case TWO:
                    draw = this.view.getResources().getDrawable(R.drawable.red2,null);
                    break;
                case THREE:
                    draw = this.view.getResources().getDrawable(R.drawable.red3,null);
                    break;
                case FOUR:
                    draw = this.view.getResources().getDrawable(R.drawable.red4,null);
                    break;
                case FIVE:
                    draw = this.view.getResources().getDrawable(R.drawable.red5,null);
                    break;
                case SIX:
                    draw = this.view.getResources().getDrawable(R.drawable.red6,null);
                    break;
                case SEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.red7,null);
                    break;
                case EIGHT:
                    draw = this.view.getResources().getDrawable(R.drawable.red8,null);
                    break;
                case NINE:
                    draw = this.view.getResources().getDrawable(R.drawable.red9,null);
                    break;
                case TEN:
                    draw = this.view.getResources().getDrawable(R.drawable.red10,null);
                    break;
                case ELEVEN:
                    draw = this.view.getResources().getDrawable(R.drawable.red11,null);
                    break;
                case TWELVE:
                    draw = this.view.getResources().getDrawable(R.drawable.red12,null);
                    break;
                case THIRTEEN:
                    draw = this.view.getResources().getDrawable(R.drawable.red13,null);
                    break;
                case MAGICIAN:
                    draw = this.view.getResources().getDrawable(R.drawable.red_magician,null);
                    break;
                default:
                    draw = this.view.getResources().getDrawable(R.drawable.back,null);
                    break;
            }
        }
        this.view.setImageDrawable(draw);
    }
}