package com.chess.engige.player;

import com.chess.engige.board.Board;
import com.chess.engige.board.Move;
import com.chess.engige.board.Move.KingCastleMove;
import com.chess.engige.board.Move.QueenCastleMove;
import com.chess.engige.board.Tile;
import com.chess.engige.piece.Piece;
import com.chess.engige.piece.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kusibab on 2017-03-24.
 */
public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {

        final List<Move> kingCastles = new ArrayList<>();
            //Roszada po stronie krola
        if (this.playerKing.isFirstMove() && !isInCheck()){

            //Roszada po stronie krolowej
            if (!this.board.getTile(57).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(59).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttackOnTile(57, opponentLegals).isEmpty() && Player.calculateAttackOnTile(58, opponentLegals).isEmpty()
                            && Player.calculateAttackOnTile(59, opponentLegals).isEmpty())
                    kingCastles.add(new QueenCastleMove(this.board, this.playerKing, 58, rookTile.getTileCoordinate(), 59, (Rook) rookTile.getPiece()));
                }
            }
            //roszada po stronie krola
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttackOnTile(61, opponentLegals).isEmpty() && Player.calculateAttackOnTile(62, opponentLegals).isEmpty()) {
                        kingCastles.add(new KingCastleMove(this.board, this.playerKing, 62,
                                                                rookTile.getTileCoordinate(), 61,(Rook) rookTile.getPiece()));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }
}
