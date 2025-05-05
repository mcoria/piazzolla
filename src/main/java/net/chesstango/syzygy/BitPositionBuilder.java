package net.chesstango.syzygy;

import net.chesstango.gardel.AbstractPositionBuilder;

/**
 * @author Mauricio Coria
 */
public class BitPositionBuilder extends AbstractPositionBuilder<BitPosition> {

    @Override
    public BitPosition getPositionRepresentation() {
        BitPosition bitPosition = new BitPosition();

        bitPosition.white = this.whitePositions;
        bitPosition.black = this.blackPositions;
        bitPosition.kings = this.kingPositions;
        bitPosition.queens = this.queenPositions;
        bitPosition.rooks = this.rookPositions;
        bitPosition.bishops = this.bishopPositions;
        bitPosition.knights = this.knightPositions;
        bitPosition.pawns = this.pawnPositions;
        bitPosition.rule50 = this.halfMoveClock;

        bitPosition.castling = (castlingBlackKingAllowed ||
                castlingBlackQueenAllowed ||
                castlingWhiteKingAllowed ||
                castlingWhiteQueenAllowed) ? 1 : 0;

        bitPosition.ep = enPassantSquare == 0 ? 0 : 1;
        bitPosition.turn = this.whiteTurn;

        return bitPosition;
    }
}
