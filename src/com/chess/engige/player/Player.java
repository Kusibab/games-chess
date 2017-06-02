package com.chess.engige.player;

import com.chess.engige.board.Board;
import com.chess.engige.board.Move;
import com.chess.engige.piece.King;
import com.chess.engige.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kusibab on 2017-03-24.
 */
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){

        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();

        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));

    }

    protected static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = moves.stream().filter(move -> piecePosition == move.getDestinationCoordinate()).collect(Collectors.toList());
        return ImmutableList.copyOf(attackMoves);
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return legalMoves;
    }


    protected King establishKing() {
        for (final Piece piece : getActivePieces()){
            if (piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Nie powinno tego tu być " + this.getAlliance() + " król jest w złym miejscu");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }
    //sprawdza czy jest szach i sprawdza czy jest ruch do wykonania dla króla
    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }


    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

    //tworzy WIRTUALNĄ szachownicę gdzie próbuje wynokać ruch królem. (tworzy całą grę)
    protected boolean hasEscapeMoves() {

        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }


    public MoveTransition makeMove(final Move move){

        if (!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttackOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

}
