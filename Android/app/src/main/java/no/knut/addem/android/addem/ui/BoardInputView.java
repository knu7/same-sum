package no.knut.addem.android.addem.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import no.knut.addem.android.addem.core.Board;
import no.knut.addem.android.addem.core.Number;
import no.knut.addem.android.addem.core.Solution;

public class BoardInputView extends BoardView implements View.OnTouchListener {

    private final static String LOG_KEY = "BoardView";
    private final int[] Colors = new int[]{Color.BLUE, Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW};

    private Stack<NumberButtonSum> sums;
    private NumberButtonSum currentSumSet;
    private NumberButton firstPreviouslySelectedButtonTouched;
    private NumberButton previouslySelectedButton;
    private boolean allowInput;
    private TextView sumGoalTextView;

    public BoardInputView(Context context, Board board, TextView sumGoalTextView) {
        super(context, board);
        sums = new Stack<>();
        firstPreviouslySelectedButtonTouched = null;
        allowInput = true;
        setOnTouchListener(this);
        this.sumGoalTextView = sumGoalTextView;
        updateCurrentSumGoal(0);

    }

    public Solution getSolution(){
        Set<Set<Number>> sumsAsSet = new HashSet<>(sums.size());
        Set<Number> sumAsSet;

        for (Set<NumberButton> sum : sums){

            sumAsSet = new HashSet<>(sum.size());
            for( NumberButton button : sum ){
                sumAsSet.add(button.number);
            }

            sumsAsSet.add(sumAsSet);
        }

        return new Solution(sumsAsSet, 30);
    }

    public void deactivateInput(){
        allowInput = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!allowInput)
            return false;

        NumberButton buttonTouched = null;
        // Check if any button is touched
        for (NumberButton button : numberButtons){
            if(button.contains(event.getX(), event.getY()))
                buttonTouched = button;
        }

        // CREATION
        if (buttonTouched == null) {
            // no button in scope
            if (event.getAction() == MotionEvent.ACTION_UP){
                if (currentSumSet != null && !currentSumSet.isEmpty()) {
                    // finalize sum on touch release
                    finalizeSum(currentSumSet);
                }

                // reset state objects
                currentSumSet = null;
                firstPreviouslySelectedButtonTouched = null;
                previouslySelectedButton = null;
            }
            return true;
        }

        // A BUTTON IS IN SCOPE

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            // check if the button is in a previously defined set
            currentSumSet = getSetOf(buttonTouched);

            if (currentSumSet == null) {
                currentSumSet = createNewSum();
                buttonTouched.paint = buttonTouched.unknownPaint;
                currentSumSet.add(buttonTouched);
                this.invalidate();
            }
            else {
                // used to validate deletion
                firstPreviouslySelectedButtonTouched = buttonTouched;
            }
        }
        else  if (event.getAction() == MotionEvent.ACTION_UP) {

            // DELETION
            if (firstPreviouslySelectedButtonTouched != null && firstPreviouslySelectedButtonTouched == buttonTouched) {
                // can only delete numbers whose button does not provide the sole
                // link between two of the other buttons in the set
                boolean allowDeletion = false;
                if(currentSumSet.size() <= 2)
                    // a set of size 2 must be adjacent to each other
                    // therefore, both can be deleted without complications
                    allowDeletion = true;
                else {
                    List<NumberButton> afterDeletionList = new ArrayList<>(currentSumSet);
                    afterDeletionList.remove(buttonTouched);
                    if (legalSum(afterDeletionList)){
                        allowDeletion = true;
                    }
                }

                if (allowDeletion){
                    buttonTouched.paint = basicPaint;
                    currentSumSet.remove(buttonTouched);

                    if (currentSumSet.isEmpty()){
                        sums.remove(currentSumSet);
                    }
                    else{
                        relink(buttonTouched, currentSumSet);
                    }

                    evaluateSums();
                }
            }

            // CREATION
            if (currentSumSet != null && !currentSumSet.isEmpty()) {

                finalizeSum(currentSumSet);
            }

            // reset state objects
            currentSumSet = null;
            firstPreviouslySelectedButtonTouched = null;
            previouslySelectedButton = null;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (currentSumSet == null){
                currentSumSet = createNewSum();
            }

            if (currentSumSet.contains(buttonTouched))
                return true;

            if(isSelectedInAnotherSet(buttonTouched, currentSumSet))
                return true;

            // ADDITION
            if (!legalAdditionToSet(buttonTouched, currentSumSet))
                return true;
            buttonTouched.paint = buttonTouched.unknownPaint;
            currentSumSet.add(buttonTouched);
            if (previouslySelectedButton != null){
                super.buttonLinks.add(new ButtonLink(previouslySelectedButton, buttonTouched));
            }
            this.invalidate();
        }

        previouslySelectedButton = buttonTouched;

        return true;
    }

    private boolean legalAdditionToSet(NumberButton button, Set<NumberButton> set){

        if (set.isEmpty())
            return true;

        boolean hasConnectedButton = false;

        for (NumberButton buttonInSet : set){
            if (button == buttonInSet)
                return false;

            if (areNeighbours(button, buttonInSet))
                hasConnectedButton = true;
        }

        return hasConnectedButton;
    }

    private boolean areNeighbours(NumberButton button1, NumberButton button2){
        int rowDifference = Math.abs(button1.number.getRow() - button2.number.getRow());
        int columnDifference = Math.abs(button1.number.getColumn() - button2.number.getColumn());
        if (rowDifference > 1)
            return false;

        if (columnDifference > 1)
            return false;

        if (rowDifference == 0 && columnDifference == 0)
            return false;

        return true;
    }

    private void relink(NumberButton removed, NumberButtonSum sum){

        // remove links connecting the removed button
        List<ButtonLink> linksToReplace = new ArrayList<>();
        for (Iterator<ButtonLink> linkIterator = super.buttonLinks.iterator(); linkIterator.hasNext();) {
            ButtonLink link = linkIterator.next();
            if (link.getSource() == removed || removed == link.getDestination()){
                linksToReplace.add(link);
                linkIterator.remove();
            }
        }

        // replace links sourced from removed button
        for (ButtonLink linkToReplace : linksToReplace){

            if (linkToReplace.getSource() == removed){

                for (NumberButton button : sum){

                    if (!areNeighbours(linkToReplace.getDestination(), button)){
                        continue;
                    }

                    boolean hasLink = false;

                    for (ButtonLink link : super.buttonLinks){
                        if (link.isLinking(button, linkToReplace.getDestination())){
                            hasLink = true;
                            break;
                        }
                    }

                    if (!hasLink){
                        super.buttonLinks.add(new ButtonLink(button, linkToReplace.getDestination()));
                        break;
                    }
                }

            }
        }

        for (NumberButton button : sum){

            boolean hasLink = false;
            for (ButtonLink link : super.buttonLinks){
                if (link.isLinking(button)){
                    hasLink = true;
                    break;
                }
            }

        }

    }

    private boolean legalSum(List<NumberButton> sum){
        List<NumberButton> removeList = new ArrayList<>();
        Stack<NumberButton> S = new Stack<>();
        S.push(sum.remove(0));
        while (!S.isEmpty()){
            NumberButton v = S.pop();

            for (NumberButton w : sum){
                if (!areNeighbours(v, w))
                    continue;

                S.push(w);
                removeList.add(w);
            }

            sum.removeAll(removeList);
            removeList.clear();
        }

        if (sum.isEmpty())
            return true;
        else return false;
    }

    private boolean isSelectedInAnotherSet(NumberButton button, Set<NumberButton> currentSet){

        for (Set<NumberButton> anotherSet : sums)
        {
            if (currentSet != anotherSet && anotherSet.contains(button))
            {
                return true;
            }
        }

        return false;
    }

    private void finalizeSum(NumberButtonSum buttonSet) {

        if (!sums.contains(buttonSet))
            sums.push(buttonSet);
        
        currentSumSet = null;
        evaluateSums();
    }

    private void evaluateSums(){

        // key: sum, val: (num sums, num buttons integrated)
        HashMap<Integer, Pair<Integer, Integer>> sumCounts = new HashMap<>();

        for (NumberButtonSum sum : sums){
            int summed = 0;
            for (NumberButton button : sum){
                summed += button.number.getValue();
            }

            if (sumCounts.containsKey(summed)){
                Pair<Integer, Integer> counts = sumCounts.get(summed);
                sumCounts.put(summed, new Pair<Integer, Integer>(counts.first + 1, counts.second + sum.size()));
            }
            else{
                sumCounts.put(summed, new Pair<Integer, Integer>(1, sum.size()));
            }
        }

        int maxButtonCount = 0;
        int maxSum = 0;
        boolean validMax = false;

        Iterator it = sumCounts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Pair<Integer, Integer>> entry = (Map.Entry) it.next();

            // skip if less than 2 equal sums
            if (entry.getValue().first < 2)
                continue;

            if (entry.getValue().second > maxButtonCount) {
                maxButtonCount = entry.getValue().second;
                maxSum = entry.getKey();
                validMax = true;
            }
        }

        if (validMax){
            updateCurrentSumGoal(maxSum);
        }
        else{
            updateCurrentSumGoal(0);
        }

        for (NumberButtonSum sum : sums) {

            boolean equalsCurrentSum;
            int buttonSetSum = 0;

            for (NumberButton button : sum) {
                buttonSetSum += button.number.getValue();
            }

            equalsCurrentSum = buttonSetSum == maxSum;

            for (NumberButton button : sum) {

                if (sum == currentSumSet || !validMax){
                    button.paint = button.unknownPaint;
                }
                else{
                    button.paint = equalsCurrentSum ? button.successPaint : button.failurePaint;
                }
            }
        }

        this.invalidate();
    }

    private NumberButtonSum createNewSum(){

        return new NumberButtonSum();
    }

    private NumberButtonSum getSetOf(NumberButton button)
    {
        for (NumberButtonSum sum : sums)
        {
            if(sum.contains(button))
            {
                return sum;
            }
        }

        return null;
    }

    private void updateCurrentSumGoal(int sumGoal){
        if (sumGoal > 0){
            sumGoalTextView.setText("Sum goal: " + sumGoal);
        }
        else{
            sumGoalTextView.setText("Sum goal: -");
        }
    }
}
