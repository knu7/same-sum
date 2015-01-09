package no.knut.addem.android.addem.core;


import android.os.AsyncTask;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.greenrobot.event.EventBus;
import no.knut.addem.android.addem.events.OptimalSolutionReadyEvent;

public class OptimalSolution extends AsyncTask<Board, Void, Solution> {
    private final static String LOG_KEY = "OptimalSolution";

    @Override protected Solution doInBackground(Board... boards) {
        Board board = boards[0];
        return getSolution(board.getNumbers(), board.getMaxValue());
    }

    @Override protected void onPostExecute(Solution optimalSolution) {
        EventBus.getDefault().post(new OptimalSolutionReadyEvent(optimalSolution));
    }

    private static Solution getSolution(no.knut.addem.android.addem.core.Number[] numbers, int maxNumber){
        float startTime = System.currentTimeMillis();
        Set<Set<Number>> allPossibleSums = getAllPossibleSums(numbers, maxNumber);
        Map<Integer, Set<Set<Number>>> sumCounts = new HashMap<>();

        boolean intersectingSet;
        int sum;
        int currentScore;

        for (Set<Number> set : allPossibleSums){

            sum = 0;
            for (Number number : set)
                sum += number.getValue();

            if (sumCounts.containsKey(sum)) {
                Set<Set<Number>> currentSetOfSets = sumCounts.get(sum);

                currentScore = 0;
                intersectingSet = false;
                for (Set<Number> currentSet : currentSetOfSets){
                    if (Sets.intersection(set, currentSet).size() > 0){
                        intersectingSet = true;
                        break;
                    }
                    currentScore += currentSet.size();
                }

                if (intersectingSet)
                    continue;

                currentSetOfSets.add(set);

                if (currentScore + set.size() == numbers.length)
                {
                    Set<Set<Number>> wrongSet = new HashSet<>();
                    float timeSpent = System.currentTimeMillis() - startTime;
                    return new Solution(currentSetOfSets, timeSpent, numbers.length, currentSetOfSets, wrongSet);
                }
            }
            else{
                Set<Set<Number>> newSet = new HashSet<>();
                newSet.add(set);
                sumCounts.put(sum, newSet);
            }
        }

        int highestScore = 0;
        int combinedSize;
        Set<Set<Number>> highScoreSet = new HashSet<>(0);
        for (Set<Set<Number>> setOfSets : sumCounts.values()){

            if (setOfSets.size() < 2)
                continue;

            combinedSize = 0;
            for (Set<Number> set : setOfSets){
                combinedSize += set.size();
            }

            if (combinedSize > highestScore) {
                highestScore = combinedSize;
                highScoreSet = setOfSets;
            }
        }

        Set<Set<Number>> wrongSet = new HashSet<>();
        float timeSpent = System.currentTimeMillis() - startTime;
        return new Solution(highScoreSet, timeSpent, highestScore, highScoreSet, wrongSet);

    }

    private static Set<Set<Number>> getAllPossibleSums(Number[] board, int maxNumber){

        Set<Set<Number>> desiredSet = new HashSet<>();
        Set<Number> boardAsSet = new HashSet<>(Arrays.asList(board));
        Set<Set<Number>> powerSet = Sets.powerSet(boardAsSet);
        List<Number> removeList = new ArrayList<>();
        List<Number> list;
        // Using breadth-first graph search to eliminate illegal
        // sums from the power set
        Stack<Number> S = new Stack<>();
        for (Set<Number> set : powerSet){

            if(set.isEmpty())
                continue;

            if (set.size() == 1){
                desiredSet.add(Sets.newHashSet(set));
                continue;
            }

            if (set.size() > maxNumber)
                continue;

            list = new ArrayList<>(set);
            S.clear();
            S.push(list.remove(0));
            while (!S.isEmpty()){
                Number v = S.pop();

                for (Number w : list){
                    if (Math.abs(v.getRow() - w.getRow()) > 1)
                        continue;
                    if (Math.abs(v.getColumn() - w.getColumn()) > 1)
                        continue;

                    S.push(w);
                    removeList.add(w);
                }

                list.removeAll(removeList);
                removeList.clear();
            }

            if(list.isEmpty()){
                desiredSet.add(Sets.newHashSet(set));
            }
        }

        return desiredSet;
    }
}
