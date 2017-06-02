package com.chess.engige.piece;

import com.chess.engige.board.Move.AttackMove;
import com.chess.engige.board.Move.PawnJump;
import com.chess.engige.board.Move.PawnMove;
import com.chess.engige.player.Alliance;
import com.chess.engige.board.Board;
import com.chess.engige.board.BoardUtils;
import com.chess.engige.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kusibab on 2017-03-10.
 */
public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE){

            int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue; }


            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isWhite()))){


                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);

                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.H_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.A_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //TODO przygotowac atak dla piona i promocje!!!!!!!!!
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }

            }else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.A_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.H_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //TODO przygotowac atak dla piona i promocje!!!!!!!!!
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
