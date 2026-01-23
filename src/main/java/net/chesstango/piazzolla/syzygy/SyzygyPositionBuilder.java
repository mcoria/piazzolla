package net.chesstango.piazzolla.syzygy;

import net.chesstango.gardel.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class SyzygyPositionBuilder extends AbstractPositionBuilder<SyzygyPosition> {

    @Override
    public SyzygyPosition getPositionRepresentation() {
        SyzygyPosition syzygyPosition = new SyzygyPosition();

        syzygyPosition.white = this.whitePositions;
        syzygyPosition.black = this.blackPositions;
        syzygyPosition.kings = this.kingPositions;
        syzygyPosition.queens = this.queenPositions;
        syzygyPosition.rooks = this.rookPositions;
        syzygyPosition.bishops = this.bishopPositions;
        syzygyPosition.knights = this.knightPositions;
        syzygyPosition.pawns = this.pawnPositions;
        syzygyPosition.rule50 = (byte) this.halfMoveClock;

        syzygyPosition.castling = (byte) ((castlingBlackKingAllowed ||
                castlingBlackQueenAllowed ||
                castlingWhiteKingAllowed ||
                castlingWhiteQueenAllowed) ? 1 : 0);

        int enPassantSquareTrail = Long.numberOfTrailingZeros(enPassantSquare);

        syzygyPosition.ep = (byte) (enPassantSquareTrail < 64 ? enPassantSquareTrail : 0);
        syzygyPosition.turn = this.whiteTurn;

        return syzygyPosition;
    }
}
