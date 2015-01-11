package no.knut.addem.android.addem.events;

import java.io.Serializable;

import no.knut.addem.android.addem.core.Solution;

public class OptimalSolutionReadyEvent implements Serializable{
    private Solution solution;
    private int sumsChecked;

    public OptimalSolutionReadyEvent(Solution solution, int sumsChecked) {

        this.solution = solution;
        this.sumsChecked = sumsChecked;
    }

    public Solution getSolution() {
        return solution;
    }

    public int getSumsChecked() {
        return sumsChecked;
    }
}
