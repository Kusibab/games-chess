package com.chess.engige.player;

import com.chess.engige.board.Board;
import com.chess.engige.board.Move;

/**
 * Created by Kusibab on 2017-03-25.
 */
public class MoveTransition {

    private final Board toBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board toBoard, final Move move, final MoveStatus moveStatus) {
        this.toBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getToBoard() {
        return this.toBoard;
    }
}
