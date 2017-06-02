package com.chess.engige.board;

import com.chess.engige.board.Board.Builder;
import com.chess.engige.piece.Pawn;
import com.chess.engige.piece.Piece;
import com.chess.engige.piece.Rook;


public abstract class Move {


    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move INCORRECT_MOVE = new IncorrectMove();

    private Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Move)){
            return false;
        }
        final Move otherMove= (Move) other;
        return  getCurrentCoordinate() == otherMove.getCurrentCoordinate()
                && getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public int getCurrentCoordinate(){
        return getMovedPiece().getPiecePosition();
    }

    public Board execute() {

        final Builder builder = new Builder();
        this.board.getCurrentPlayer().getActivePieces().stream().filter(piece -> !getMovedPiece().equals(piece)).forEachOrdered(builder::setPiece);

        this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
        builder.setPiece(getMovedPiece().movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }



    public static final class MajorMove extends Move {

        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board,movedPiece,destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class AttackMove extends Move{

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if (this == other){
                return true;
            }
            if (!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }

    public static final class PawnMove extends Move{

        public PawnMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board,movedPiece,destinationCoordinate);
        }
    }
    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board,
                               final Piece movedPiece,
                               final int destinationCoordinate,
                               final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);

        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);

        }
    }

    public static final class PawnJump extends Move{

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board,movedPiece,destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            this.board.getCurrentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);

            this.board.getCurrentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPeasantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStartPossition;
        protected final int castleRookFinalPossition;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final int castleRookStartPosition,
                          final int castleRookFinalPosition,
                          final Rook castleRook) {
            super(board,movedPiece,destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStartPossition = castleRookStartPosition;
            this.castleRookFinalPossition = castleRookFinalPosition;
        }

        public Rook getCastleRook(){
            return castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece) && !castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookFinalPossition,this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }



    public static final class KingCastleMove extends CastleMove{

        public KingCastleMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final int castleRookStartPosition,
                              final int castleRookFinalPosition,
                              final Rook castleRook) {
            super(board, movedPiece, destinationCoordinate, castleRookStartPosition, castleRookFinalPosition, castleRook);
        }
        //Tak wygląda konswencja zapisu roszady po stronie króla
        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class QueenCastleMove extends CastleMove{

        public QueenCastleMove(final Board board,
                               final Piece movedPiece,
                               final int destinationCoordinate,
                               final int castleRookStartPossition,
                               final int castleRookFinalPossition,
                               final Rook castleRook) {
            super(board,movedPiece,destinationCoordinate, castleRookStartPossition, castleRookFinalPossition, castleRook);
        }
        //Tak wygląda konswencja zapisu roszady po stronie królowej
        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    public static final class IncorrectMove extends Move{

        public IncorrectMove() {
            super(null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute null move!");
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for (final Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return INCORRECT_MOVE;
        }


    }

}

