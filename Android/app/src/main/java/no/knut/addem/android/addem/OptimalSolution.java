package no.knut.addem.android.addem;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class OptimalSolution extends AsyncTaskLoader<Solution>{

    private Number[] board;
    private int maxNumber;

    public OptimalSolution(Context context, Number[] board, int maxNumber) {
        super(context);
        this.board = board;
        this.maxNumber = maxNumber;
    }

    @Override
    public Solution loadInBackground() {
        Log.d("", "optimal");
        return getSolution(board, maxNumber);
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public void deliverResult(Solution data) {
        super.deliverResult(data);
    }

    private Solution getSolution(Number[] board, int maxNumber){

        float startTime = System.currentTimeMillis();
        Set<Set<Number>> allPossibleSums = getAllPossibleSums(board, maxNumber);
        Map<Integer, Set<Set<Number>>> sumCounts = new HashMap<>();

        boolean intersectingSet;
        int sum;
        int currentScore;

        for (Set<Number> set : allPossibleSums){

            sum = 0;
            for (Number btn : set)
                sum += btn.number;

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

                if (currentScore + set.size() == board.length)
                {
                    Set<Set<Number>> wrongSet = new HashSet<>();
                    float timeSpent = System.currentTimeMillis() - startTime;
                    return new Solution(currentSetOfSets, timeSpent, board.length, currentSetOfSets, wrongSet);
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

    private Set<Set<Number>> getAllPossibleSums(Number[] board, int maxNumber){

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
                    if (Math.abs(v.row - w.row) > 1)
                        continue;
                    if (Math.abs(v.column - w.column) > 1)
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
