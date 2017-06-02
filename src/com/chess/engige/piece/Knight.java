package com.chess.engige.piece;

import com.chess.engige.player.Alliance;
import com.chess.engige.board.Board;
import com.chess.engige.board.BoardUtils;
import com.chess.engige.board.Move;
import com.chess.engige.board.Move.AttackMove;
import com.chess.engige.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engige.board.Move.*;

/**
 * @Knight to konik.
 */
public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) || isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

            if (!candidateDestinationTile.isTileOccupied()){
                //ruch na puste pole
                legalMoves.add(new MajorMove(board,this, candidateDestinationCoordinate));
            }
            else {
                final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                if (this.pieceAlliance != pieceAlliance) {
                    //ruch atakujacy figure przeciwnika
                    legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                }
            }
        }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.A_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 || candidateOffset ==6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.B_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.G_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.H_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17);
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
}
