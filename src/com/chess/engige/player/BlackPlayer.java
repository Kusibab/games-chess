package com.chess.engige.player;

import com.chess.engige.board.Board;
import com.chess.engige.board.Move;
import com.chess.engige.board.Move.KingCastleMove;
import com.chess.engige.board.Tile;
import com.chess.engige.piece.Piece;
import com.chess.engige.piece.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engige.board.Move.*;


public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> blackStandardLegalMoves, final Collection<Move> whiteStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }


    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {

        final List<Move> kingCastles = new ArrayList<>();
        //Roszada po stronie krola
        if (this.playerKing.isFirstMove() && !isInCheck()){

            //Roszada po stronie krolowej
            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttackOnTile(1, opponentLegals).isEmpty() && Player.calculateAttackOnTile(2, opponentLegals).isEmpty()
                            && Player.calculateAttackOnTile(3, opponentLegals).isEmpty())

                        kingCastles.add(new QueenCastleMove(this.board, this.playerKing, 6, rookTile.getTileCoordinate(), 2, (Rook)rookTile.getPiece()));
                }
            }
            //roszada po stronie krola
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttackOnTile(5, opponentLegals).isEmpty() && Player.calculateAttackOnTile(6, opponentLegals).isEmpty()) {

                        kingCastles.add(new KingCastleMove(this.board, this.playerKing, 6, rookTile.getTileCoordinate(), 5, (Rook) rookTile.getPiece()));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
