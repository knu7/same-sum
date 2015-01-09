package no.knut.addem.android.addem.events;

import no.knut.addem.android.addem.core.Solution;

/**
 * Created by Knut on 08.01.2015.
 */
public class OptimalSolutionReadyEvent {
    private Solution solution;

    public Solution getSolution() {
        return solution;
    }

    public OptimalSolutionReadyEvent(Solution solution) {

        this.solution = solution;
    }
}
