package no.knut.addem.android.addem.core;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution implements Serializable{

    public Set<Set<no.knut.addem.android.addem.core.Number>> sums;
    private Set<Set<Number>> correctSums = null;
    private Set<Set<Number>> wrongSums = null;
    private Integer score = null;
    private int positiveScore = 0;
    private int negativeScore = 0;
    private float millisSpent;

    public Solution(Set<Set<Number>> sums, float millisSpent){

        this.sums = sums;
        this.millisSpent = millisSpent;
    }

    public Solution(Set<Set<Number>> sums,
                    float millisSpent,
                    Integer score,
                    Set<Set<Number>> correctSums,
                    Set<Set<Number>> wrongSums){
        this.sums = sums;
        this.millisSpent = millisSpent;
        this.score = score;
        this.correctSums = correctSums;
        this.wrongSums = wrongSums;
    }

    public float getSecondsSpent(){
        return millisSpent / 1000;
    }

    public float getMillisSpent(){
        return millisSpent;
    }

    public int getScore(){

        if (score != null)
            return score;

        separateWrongAndCorrectSums();
        return score;
    }

    public Set<Set<Number>> getCorrectSums(){
        if (correctSums == null)
            separateWrongAndCorrectSums();

        return correctSums;
    }

    public Set<Set<Number>> getWrongSums(){
        if (wrongSums == null)
            separateWrongAndCorrectSums();

        return wrongSums;
    }

    private void separateWrongAndCorrectSums(){

        if (sums.isEmpty()){
            score = 0;
            correctSums = sums;
            wrongSums = sums;
            return;
        }

        Map<Integer, Set<Set<Number>>> equalSumsMap = new HashMap<>();
        for (Set<Number> sumSet : sums){
            int sum = 0;
            for (Number btn : sumSet)
                sum += btn.getValue();

            if (equalSumsMap.containsKey(sum)) {
                Set<Set<Number>> currentSetOfSets = equalSumsMap.get(sum);
                currentSetOfSets.add(sumSet);
            }
            else{
                Set<Set<Number>> newSet = new HashSet<>();
                newSet.add(sumSet);
                equalSumsMap.put(sum, newSet);
            }
        }

        positiveScore = 0;
        for (Set<Set<Number>> equalSumsSet : equalSumsMap.values() ){

            int currentScore = 0;
            int numSums = 0;
            for (Set<Number> set : equalSumsSet){
                currentScore += set.size();
                numSums++;
            }

            if(numSums < 2)
                continue;

            if (currentScore >= positiveScore){
                positiveScore = currentScore;
                correctSums = equalSumsSet;
            }
        }
        if (positiveScore == 0)
            correctSums = new HashSet<>(0);

        negativeScore = 0;
        wrongSums = new HashSet<>();
        for (Set<Set<Number>> equalSumsSet : equalSumsMap.values() ){

            if (equalSumsSet != correctSums){
                for (Set<Number> wrongSet : equalSumsSet){
                    wrongSums.add(wrongSet);
                    negativeScore += wrongSet.size();
                }
            }
        }

        score = positiveScore - negativeScore;
    }
}
