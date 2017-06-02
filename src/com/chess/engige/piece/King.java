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
 * Created by Kusibab on 2017-03-11.
 */
public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};


    public King(final int piecePosition,final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
    }

    public King(final int piecePosition,final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE){

            final int candidateDestinationCoordinate = this.piecePosition + candidateCoordinateOffset;

            if(isFirstColumnExclusion(this.piecePosition, candidateCoordinateOffset) || isEighthColumnExclusion(this.piecePosition, candidateCoordinateOffset)){
                continue;
            }


            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
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
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.A_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == 7 || candidateOffset == -9);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.H_COLUMN[currentPosition] && (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
