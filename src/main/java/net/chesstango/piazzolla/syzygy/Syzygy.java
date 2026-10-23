package net.chesstango.piazzolla.syzygy;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.Set;

/**
 * Public interface for probing Syzygy endgame tablebases (WDL and DTZ).
 * <p>
 * Syzygy tablebases provide game-theoretic endgame values and optimal move guidance:
 * <ul>
 *   <li><b>WDL (.rtbw)</b>: Win-Draw-Loss tables considering the fifty-move rule, optimized for fast probing during search.</li>
 *   <li><b>DTZ (.rtbz)</b>: Distance-To-Zero tables measuring the shortest distance (in half-moves / plies) to zero the 50-move counter
 *       (pawn move or capture) or achieve checkmate without forfeiting the theoretical outcome, designed for root probing.</li>
 * </ul>
 *
 * @author Mauricio Coria
 */
public interface Syzygy extends Closeable {

    /**
     * Maximum number of legal moves that can be generated and returned in the alternative results array during root probing.
     */
    int TB_MAX_MOVES = (192 + 1);

    /**
     * WDL outcome value indicating a win for the side to move within the 50-move rule.
     */
    int TB_WIN = 4;                /* WIN  */

    /**
     * Encoded probe result representing immediate checkmate on the board.
     */
    int TB_RESULT_CHECKMATE = SyzygyConstants.TB_SET_WDL(0, TB_WIN);

    /**
     * WDL outcome value indicating a theoretical draw.
     */
    int TB_DRAW = 2;               /* DRAW */

    /**
     * Encoded probe result representing immediate stalemate on the board.
     */
    int TB_RESULT_STALEMATE = SyzygyConstants.TB_SET_WDL(0, TB_DRAW);

    /**
     * Encoded probe result indicating that the tablebase probe failed (e.g., position not in tablebase, illegal position, or castling rights present).
     */
    int TB_RESULT_FAILED = 0xFFFFFFFF;

    /**
     * WDL outcome value indicating a loss for the side to move within the 50-move rule.
     */
    int TB_LOSS = 0;               /* LOSS */

    /**
     * WDL outcome value indicating a blessed loss (a theoretical loss that can be drawn due to the 50-move rule).
     */
    int TB_BLESSED_LOSS = 1;       /* LOSS but 50-move draw */

    /**
     * WDL outcome value indicating a cursed win (a theoretical win that is drawn due to the 50-move rule).
     */
    int TB_CURSED_WIN = 3;         /* WIN but 50-move draw  */

    /**
     * Promotion flag indicating no piece promotion.
     */
    int TB_PROMOTES_NONE = 0;

    /**
     * Promotion flag indicating promotion to a Queen.
     */
    int TB_PROMOTES_QUEEN = 1;

    /**
     * Promotion flag indicating promotion to a Rook.
     */
    int TB_PROMOTES_ROOK = 2;

    /**
     * Promotion flag indicating promotion to a Bishop.
     */
    int TB_PROMOTES_BISHOP = 3;

    /**
     * Promotion flag indicating promotion to a Knight.
     */
    int TB_PROMOTES_KNIGHT = 4;

    /**
     * Extracts the Win-Draw-Loss (WDL) value from an encoded probe result.
     *
     * @param _res the encoded probe result
     * @return one of {@link #TB_LOSS}, {@link #TB_BLESSED_LOSS}, {@link #TB_DRAW}, {@link #TB_CURSED_WIN}, or {@link #TB_WIN}
     */
    static int TB_GET_WDL(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_WDL_MASK) >>> SyzygyConstants.TB_RESULT_WDL_SHIFT;
    }

    /**
     * Extracts the Distance-To-Zero (DTZ) value (distance in plies) from an encoded probe result.
     *
     * @param _res the encoded probe result
     * @return the DTZ value in half-moves / plies
     */
    static int TB_GET_DTZ(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_DTZ_MASK) >> SyzygyConstants.TB_RESULT_DTZ_SHIFT;
    }

    /**
     * Extracts the originating square index (0 to 63, where 0 is A1 and 63 is H8) from an encoded probe result.
     *
     * @param _res the encoded probe result
     * @return the origin square index (0-63)
     */
    static int TB_GET_FROM(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_FROM_MASK) >> SyzygyConstants.TB_RESULT_FROM_SHIFT;
    }

    /**
     * Extracts the destination square index (0 to 63, where 0 is A1 and 63 is H8) from an encoded probe result.
     *
     * @param _res the encoded probe result
     * @return the destination square index (0-63)
     */
    static int TB_GET_TO(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_TO_MASK) >> SyzygyConstants.TB_RESULT_TO_SHIFT;
    }

    /**
     * Extracts the piece promotion type from an encoded probe result.
     *
     * @param _res the encoded probe result
     * @return one of {@link #TB_PROMOTES_NONE}, {@link #TB_PROMOTES_QUEEN}, {@link #TB_PROMOTES_ROOK}, {@link #TB_PROMOTES_BISHOP}, or {@link #TB_PROMOTES_KNIGHT}
     */
    static int TB_GET_PROMOTES(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_PROMOTES_MASK) >> SyzygyConstants.TB_RESULT_PROMOTES_SHIFT;
    }

    /**
     * Probes Distance-To-Zero (DTZ) and Win-Draw-Loss (WDL) tablebases for a position at the root of search.
     * <p>
     * Returns an encoded result containing the best move, DTZ, and WDL values.
     * If the optional {@code results} array is provided, it must be sized to at least {@link #TB_MAX_MOVES},
     * and will be populated with encoded results for each legal move, terminated with {@link #TB_RESULT_FAILED}.
     *
     * @param pos the chess position representation to probe
     * @param results an optional array of size at least {@link #TB_MAX_MOVES} to receive results for all legal moves, or {@code null}
     * @return an encoded result with WDL, DTZ, and best move details, or {@link #TB_RESULT_FAILED}, {@link #TB_RESULT_CHECKMATE}, or {@link #TB_RESULT_STALEMATE}
     */
    int tb_probe_root(SyzygyPosition pos, int[] results);

    /**
     * Probes Win-Draw-Loss (WDL) tablebases for a given position during tree search.
     *
     * @param pos the chess position representation to probe
     * @return one of {@link #TB_LOSS}, {@link #TB_BLESSED_LOSS}, {@link #TB_DRAW}, {@link #TB_CURSED_WIN}, or {@link #TB_WIN}, or {@link #TB_RESULT_FAILED} if probing failed
     */
    int tb_probe_wdl(SyzygyPosition pos);

    /**
     * Returns the maximum piece count (cardinality) among all successfully initialized tablebases.
     *
     * @return the largest number of pieces supported by the loaded tablebases (e.g. 5 or 6), or 0 if none loaded
     */
    int tb_largest();

    /**
     * Opens and initializes Syzygy tablebases from the specified directories.
     *
     * @param syzygyDirs a set of directory paths containing Syzygy {@code .rtbw} and {@code .rtbz} files
     * @return an initialized {@link Syzygy} instance
     */
    static Syzygy open(Set<Path> syzygyDirs) {
        SyzygyImp syzygyImp = new SyzygyImp(syzygyDirs);
        syzygyImp.tb_init();
        return syzygyImp;
    }
}
