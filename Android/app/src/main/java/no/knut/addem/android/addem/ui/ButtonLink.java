package no.knut.addem.android.addem.ui;

/**
 * Created by Knut on 18.01.2017.
 */

public class ButtonLink {
    private NumberButton source;
    private NumberButton destination;

    public ButtonLink(NumberButton source, NumberButton button2){
        this.source = source;
        this.destination = button2;
    }

    public NumberButton getSource(){
        return source;
    }

    public NumberButton getDestination(){
        return destination;
    }
    
    public void setSource(NumberButton source){
        this.source = source;
    }

    public void setDestination(NumberButton destination){
        this.destination = destination;
    }

    public boolean isLinking(NumberButton button){
        return source == button || button == destination;
    }

    public boolean isLinking(NumberButton button, NumberButton button2){
        return  source == button && button2 == destination ||
                source == button2 && button == destination;
    }
}
