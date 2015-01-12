package no.knut.addem.android.addem.transport;

import no.knut.addem.android.addem.core.Number;

public class BoardResponse {
    private long gameId;
    private String[] opponents;
    private Number[][] board;

    public BoardResponse(long gameId, String[] opponents, Number[][] board) {
        this.gameId = gameId;
        this.opponents = opponents;
        this.board = board;
    }

    public long getGameId() {
        return gameId;
    }

    public String[] getOpponents() {
        return opponents;
    }

    public Number[][] getBoard() {
        return board;
    }
}
