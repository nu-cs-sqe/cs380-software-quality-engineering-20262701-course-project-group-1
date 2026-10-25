package domain;

public class Card {
    private final CardType type;

    public Card(CardType type){
        if (type == null){
            throw new IllegalArgumentException("Not a valid Card Type, Card Type cannot be null");
        }
        this.type = type;;
    }

    public CardType getTypeofCard(){
        return type;
    }
}
