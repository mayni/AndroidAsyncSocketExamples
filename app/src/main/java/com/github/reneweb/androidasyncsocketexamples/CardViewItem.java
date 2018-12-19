package com.github.reneweb.androidasyncsocketexamples;

public class CardViewItem extends BaseItem{
//    private int cardViewImage;
    private String cardViewText,cardViewText1,cardViewText2,cardViewText3;

    public CardViewItem() {
        super(ViewType.TYPE_CARD_VIEW);
    }

    public String getText() {
        return cardViewText;
    }
    public String getText1() { return cardViewText1;}
    public String getText2() { return cardViewText2;}
    public String getText3() { return cardViewText3;}

    public CardViewItem setText(String cardViewText,String cardViewText1,String cardViewText2,String cardViewText3) {
            System.out.print(this.cardViewText + this.cardViewText1 + this.cardViewText2 + this.cardViewText3);
            this.cardViewText = cardViewText;
            this.cardViewText1 =cardViewText1;
            this.cardViewText2 = cardViewText2;
            this.cardViewText3 =cardViewText3;
        return this;
    }





}
