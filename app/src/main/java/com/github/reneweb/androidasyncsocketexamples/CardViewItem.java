package com.github.reneweb.androidasyncsocketexamples;

public class CardViewItem extends BaseItem{
//    private int cardViewImage;
    private String cardViewText;

    public CardViewItem() {
        super(ViewType.TYPE_CARD_VIEW);
    }

    public String getText() {
        return cardViewText;
    }

    public CardViewItem setText(String cardViewText) {
        this.cardViewText = cardViewText;
        return this;
    }


}
