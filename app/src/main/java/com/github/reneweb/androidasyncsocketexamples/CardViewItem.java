package com.github.reneweb.androidasyncsocketexamples;

public class CardViewItem extends BaseItem{
//    private int cardViewImage;
    private String cardViewText,cardViewText1;

    public CardViewItem() {
        super(ViewType.TYPE_CARD_VIEW);
    }

    public String getText() {
        return cardViewText;
    }
    public String getText1() {
        return cardViewText1;
    }

    public CardViewItem setText(String cardViewText,String cardViewText1) {
        this.cardViewText = cardViewText;
        this.cardViewText1 =cardViewText1;
        return this;
    }



}
