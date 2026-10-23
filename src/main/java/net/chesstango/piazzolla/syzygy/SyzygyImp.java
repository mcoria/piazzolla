package net.chesstango.piazzolla.syzygy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static net.chesstango.piazzolla.syzygy.Chess.*;
import static net.chesstango.piazzolla.syzygy.SyzygyConstants.Piece.*;
import static net.chesstango.piazzolla.syzygy.SyzygyConstants.*;
import static net.chesstango.piazzolla.syzygy.SyzygyConstants.PieceType.*;
import static net.chesstango.piazzolla.syzygy.TableBase.TableType.*;

/**
 * Implementation of the {@link Syzygy} tablebase prober.
 * <p>
 * Syzygy Bases consist of two sets of files:
 * <ul>
 *   <li><b>WDL files (.rtbw)</b>: Storing win/draw/loss information considering the fifty-move rule for access during search.</li>
 *   <li><b>DTZ files (.rtbz)</b>: Storing distance-to-zero information for access at the root.</li>
 * </ul>
 * <p>
 * DTZ measures the shortest distance to zero the position (zeroing = pawn move, capture, or checkmate),
 * without compromising the Win/Draw/Loss status. WDL has full data for both sides, whereas DTZ50 omits data for one side to save space.
 * Syzygy WDL is double-sided and DTZ is single-sided.
 *
 * @author Mauricio Coria
 */
public class SyzygyImp implements Syzygy {

    static class HashEntry {
        long key;
        BaseEntry ptr;
    }

    static final int[] WdlToDtz = {-1, -101, 0, 101, 1};
    static final int[] WdlToMap = new int[]{1, 3, 0, 2, 0};
    static final byte[] PAFlags = new byte[]{8, 0, 0, 0, 4};

    final Path[] syzygyPath;
    final HashEntry[] tbHash = new HashEntry[1 << TB_HASHBITS];
    final PieceEntry[] pieceEntry = new PieceEntry[TB_MAX_PIECE];
    final PawnEntry[] pawnEntry = new PawnEntry[TB_MAX_PAWN];

    int tbNumPiece;
    int tbNumPawn;
    int numWdl;
    int numDtm;
    int numDtz;

    int TB_MaxCardinality;
    int TB_MaxCardinalityDTM;
    int TB_LARGEST;

    int success;
    int score;
    int dtz;

    SyzygyImp(Set<Path> syzygyDirs) {
        this.syzygyPath = syzygyDirs.stream()
                .filter(Files::isDirectory)
                .toArray(Path[]::new);
    }

    /**
     * Initializes the tablebase system with the configured directory paths.
     * <p>
     * Resets counters and properties, discovers and initializes tablebases
     * (3-piece, 4-piece, 5-piece, and 6-piece tables), and updates the maximum available cardinality.
     */
    public void tb_init() {
        // Reset counters and properties
        tbNumPiece = 0;
        tbNumPawn = 0;
        numWdl = 0;
        numDtm = 0;
        numDtz = 0;
        TB_MaxCardinality = 0;
        TB_MaxCardinalityDTM = 0;
        TB_LARGEST = 0;

        PieceType[] pieces = {QUEEN, ROOK, BISHOP, KNIGHT, PAWN};

        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;

        /**
         * Initialize tablebases with 3 pieces: K_ vs K
         */
        for (i = 0; i < 5; i++) {
            String tableName = String.format("K%cvK", pieces[i].getSymbol());
            init_tb(tableName);
        }

        /**
         * Initialize tablebases with 4 pieces: K_ vs K_
         */
        for (i = 0; i < 5; i++) {
            for (j = i; j < 5; j++) {
                String tableName = String.format("K%cvK%c", pieces[i].getSymbol(), pieces[j].getSymbol());
                init_tb(tableName);
            }
        }


        /**
         * Initialize tablebases with 4 pieces: K__ vs K
         */
        for (i = 0; i < 5; i++) {
            for (j = i; j < 5; j++) {
                String tableName = String.format("K%c%cvK", pieces[i].getSymbol(), pieces[j].getSymbol());
                init_tb(tableName);
            }
        }

        /**
         * Initialize tablebases with 5 pieces: K__ vs K_
         */
        for (i = 0; i < 5; i++) {
            for (j = i; j < 5; j++) {
                for (k = 0; k < 5; k++) {
                    String tableName = String.format("K%c%cvK%c", pieces[i].getSymbol(), pieces[j].getSymbol(), pieces[k].getSymbol());
                    init_tb(tableName);
                }
            }
        }

        /**
         * Initialize tablebases with 5 pieces: K___ vs K
         */
        for (i = 0; i < 5; i++) {
            for (j = i; j < 5; j++) {
                for (k = j; k < 5; k++) {
                    String tableName = String.format("K%c%c%cvK", pieces[i].getSymbol(), pieces[j].getSymbol(), pieces[k].getSymbol());
                    init_tb(tableName);
                }
            }
        }

        /**
         * Initialize tablebases with 6 pieces: K__ vs K__
         */
        for (i = 0; i < 5; i++) {
            for (j = i; j < 5; j++) {
                for (k = i; k < 5; k++) {
                    for (l = (i == k) ? j : k; l < 5; l++) {
                        String tableName = String.format("K%c%cvK%c%c", pieces[i].getSymbol(), pieces[j].getSymbol(), pieces[k].getSymbol(), pieces[l].getSymbol());
                        init_tb(tableName);
                    }
                }
            }
        }

        /**
         * Initialize tablebases with 6 pieces: K___ vs K_
         */
        for (l = 0; l < 5; l++) {
            for (i = 0; i < 5; i++) {
                for (j = i; j < 5; j++) {
                    for (k = 0; k < 5; k++) {
                        String tableName = String.format("K%c%c%cvK%c", pieces[l].getSymbol(), pieces[i].getSymbol(), pieces[j].getSymbol(), pieces[k].getSymbol());
                        init_tb(tableName);
                    }
                }
            }
        }

        /**
         * Initialize tablebases with 6 pieces: K____ vs K
         */
        for (l = 0; l < 5; l++) {
            for (i = 0; i < 5; i++) {
                for (j = i; j < 5; j++) {
                    for (k = 0; k < 5; k++) {
                        String tableName = String.format("K%c%c%c%cvK", pieces[l].getSymbol(), pieces[i].getSymbol(), pieces[j].getSymbol(), pieces[k].getSymbol());
                        init_tb(tableName);
                    }
                }
            }
        }


        // Update the largest cardinality values
        TB_LARGEST = TB_MaxCardinality;
        if (TB_MaxCardinalityDTM > TB_LARGEST) {
            TB_LARGEST = TB_MaxCardinalityDTM;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int tb_largest() {
        return TB_LARGEST;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Probes the Distance-To-Zero (DTZ) and Win-Draw-Loss (WDL) tablebases for the given root position.
     * <p>
     * <b>Notes:</b>
     * <ul>
     *   <li>Engines can use this function to probe at the root. This function should not be used during tree search.</li>
     *   <li>DTZ tablebases can suggest unnatural moves, especially for losing positions. Engines may prefer traditional search
     *       combined with WDL move filtering using the alternative results array.</li>
     *   <li>This function is not thread-safe.</li>
     * </ul>
     *
     * @param pos the chess position representation to probe
     * @param results an optional array of size at least {@link #TB_MAX_MOVES} to receive results for all legal moves, or {@code null}
     * @return an encoded result with WDL, DTZ, and best move details, or {@link #TB_RESULT_FAILED}, {@link #TB_RESULT_CHECKMATE}, or {@link #TB_RESULT_STALEMATE}
     */
    @Override
    public int tb_probe_root(SyzygyPosition pos, int[] results) {
        if (Long.bitCount(pos.black | pos.white) > this.TB_LARGEST) {
            return TB_RESULT_FAILED;
        }

        if (pos.castling != 0)
            return TB_RESULT_FAILED;

        if (!is_valid(pos)) {
            return TB_RESULT_FAILED;
        }

        short move = probe_root(pos, results);

        if (move == 0)
            return TB_RESULT_FAILED;

        if (move == MOVE_CHECKMATE)
            return TB_RESULT_CHECKMATE;

        if (move == MOVE_STALEMATE)
            return TB_RESULT_STALEMATE;

        int res = 0;
        res = TB_SET_WDL(res, dtz_to_wdl(pos.rule50, dtz));
        res = TB_SET_DTZ(res, (dtz < 0 ? -dtz : dtz));
        res = TB_SET_FROM(res, move_from(move));
        res = TB_SET_TO(res, move_to(move));
        res = TB_SET_PROMOTES(res, move_promotes(move));
        res = TB_SET_EP(res, is_en_passant(pos, move) ? 1 : 0);

        return res;
    }


    /**
     * {@inheritDoc}
     * <p>
     * Probes the Win-Draw-Loss (WDL) tablebases for the given position during tree search.
     *
     * @param pos the chess position representation to probe
     * @return one of {@link #TB_LOSS}, {@link #TB_BLESSED_LOSS}, {@link #TB_DRAW}, {@link #TB_CURSED_WIN}, or {@link #TB_WIN}, or {@link #TB_RESULT_FAILED} if probing failed
     */
    @Override
    public int tb_probe_wdl(SyzygyPosition pos) {
        if (Long.bitCount(pos.black | pos.white) > this.TB_LARGEST) {
            return TB_RESULT_FAILED;
        }

        if (pos.castling != 0)
            return TB_RESULT_FAILED;

        if (pos.rule50 != 0)
            return TB_RESULT_FAILED;

        int v = probe_wdl(pos);

        if (success == 0)
            return TB_RESULT_FAILED;

        return v + 2;
    }

    /**
     * Closes any open resources associated with the tablebase files.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Initializes a tablebase entry for the given tableType name.
     * This method processes the tableType name to determine the pieces involved,
     * calculates unique keys for the tablebase, and sets up the corresponding
     * `BaseEntry` object with relevant attributes. It also updates global counters
     * and properties related to the tablebase.
     *
     * @param tbName the name of the tablebase to initialize
     */
    void init_tb(String tbName) {
        BaseEntry baseEntry = null;
        if (tbName.toUpperCase().contains("P")) {
            baseEntry = new PawnEntry(this);
        } else {
            baseEntry = new PieceEntry(this);
        }
        baseEntry.init_tb(tbName);
    }

    /**
     * Adds a `BaseEntry` object to the hash tableType using the provided key.
     * This method calculates the index in the hash tableType based on the key
     * and resolves collisions using linear probing.
     *
     * @param ptr the `BaseEntry` object to be added to the hash tableType
     * @param key the unique key used to identify the entry in the hash tableType
     */
    void add_to_hash(BaseEntry ptr, long key) {
        int idx = (int) (key >>> (64 - TB_HASHBITS));
        final int idxStart = idx;
        while (tbHash[idx] != null) {
            idx = (idx + 1) & ((1 << TB_HASHBITS) - 1);
            if (idx == idxStart) {
                throw new RuntimeException("Hash tableType overflow");
            }
        }
        tbHash[idx] = new HashEntry();
        tbHash[idx].key = key;
        tbHash[idx].ptr = ptr;
    }


    private short probe_root(SyzygyPosition pos, int[] results) {
        dtz = probe_dtz(pos);
        if (success == 0) return 0;

        short[] scores = new short[MAX_MOVES];
        short[] moves = new short[MAX_MOVES];
        int len = gen_moves(pos, moves);
        int num_draw = 0;
        int j = 0;
        SyzygyPosition pos1 = new SyzygyPosition();
        for (int i = 0; i < len; i++) {
            if (!do_move(pos1, pos, moves[i])) {
                scores[i] = SCORE_ILLEGAL;
                continue;
            }
            int v = 0;
            //        print_move(pos,moves[i]);
            if (dtz > 0 && is_mate(pos1))
                v = 1;
            else {
                if (pos1.rule50 != 0) {
                    v = -probe_dtz(pos1);
                    if (v > 0)
                        v++;
                    else if (v < 0)
                        v--;
                } else {
                    v = -probe_wdl(pos1);
                    v = WdlToDtz[v + 2];
                }
            }
            num_draw += v == 0 ? 1 : 0;
            if (success == 0) {
                return 0;
            }
            scores[i] = (short) v;
            if (results != null) {
                int res = 0;
                res = TB_SET_WDL(res, dtz_to_wdl(pos.rule50, v));
                res = TB_SET_FROM(res, move_from(moves[i]));
                res = TB_SET_TO(res, move_to(moves[i]));
                res = TB_SET_PROMOTES(res, move_promotes(moves[i]));
                res = TB_SET_EP(res, is_en_passant(pos, moves[i]) ? 1 : 0);
                res = TB_SET_DTZ(res, (v < 0 ? -v : v));
                results[j++] = res;
            }
        }
        if (results != null) {
            results[j++] = TB_RESULT_FAILED;
        }

        score = dtz;

        // Now be a bit smart about filtering out moves.
        if (dtz > 0) {        // winning (or 50-move rule draw)
            int best = BEST_NONE;
            short best_move = 0;
            for (int i = 0; i < len; i++) {
                int v = scores[i];
                if (v == SCORE_ILLEGAL) {
                    continue;
                }
                if (v > 0 && v < best) {
                    best = v;
                    best_move = moves[i];
                }
            }
            return (best == BEST_NONE ? 0 : best_move);
        } else if (dtz < 0) {   // losing (or 50-move rule draw)
            int best = 0;
            short best_move = 0;
            for (int i = 0; i < len; i++) {
                int v = scores[i];
                if (v == SCORE_ILLEGAL)
                    continue;
                if (v < best) {
                    best = v;
                    best_move = moves[i];
                }
            }
            return (best == 0 ? MOVE_CHECKMATE : best_move);
        } else {                // drawing
            // Check for stalemate:
            if (num_draw == 0)
                return MOVE_STALEMATE;

            // Select a "random" move that preserves the draw.
            // Uses calc_key as the PRNG.
            long count = Math.abs(calcKey(pos, !pos.turn)) % num_draw;
            for (int i = 0; i < len; i++) {
                int v = scores[i];
                if (v == SCORE_ILLEGAL)
                    continue;
                if (v == 0) {
                    if (count == 0)
                        return moves[i];
                    count--;
                }
            }
            return 0;
        }
    }

    // Probe the DTZ table for a particular position.
    // If *success != 0, the probe was successful.
    // The return value is from the point of view of the side to move:
    //         n < -100 : loss, but draw under 50-move rule
    // -100 <= n < -1   : loss in n ply (assuming 50-move counter == 0)
    //         0        : draw
    //     1 < n <= 100 : win in n ply (assuming 50-move counter == 0)
    //   100 < n        : win, but draw under 50-move rule
    //
    // If the position mate, -1 is returned instead of 0.
    //
    // The return value n can be off by 1: a return value -n can mean a loss
    // in n+1 ply and a return value +n can mean a win in n+1 ply. This
    // cannot happen for tables with positions exactly on the "edge" of
    // the 50-move rule.
    //
    // This means that if dtz > 0 is returned, the position is certainly
    // a win if dtz + 50-move-counter <= 99. Care must be taken that the engine
    // picks moves that preserve dtz + 50-move-counter <= 99.
    //
    // If n = 100 immediately after a capture or pawn move, then the position
    // is also certainly a win, and during the whole phase until the next
    // capture or pawn move, the inequality to be preserved is
    // dtz + 50-movecounter <= 100.
    //
    // In short, if a move is available resulting in dtz + 50-move-counter <= 99,
    // then do not accept moves leading to dtz + 50-move-counter == 100.
    //
    private int probe_dtz(SyzygyPosition pos) {
        int wdl = probe_wdl(pos);
        if (success == 0) return 0;

        // If draw, then dtz = 0.
        if (wdl == 0) return 0;

        // Check for winning capture or en passant capture as only best move.
        if (success == 2)
            return WdlToDtz[wdl + 2];

        short[] moves = new short[TB_MAX_MOVES];
        short m = 0;

        SyzygyPosition pos1 = new SyzygyPosition();
        int totalMoves = 0;

        // If winning, check for a winning pawn move.
        if (wdl > 0) {
            // Generate at least all legal non-capturing pawn moves
            // including non-capturing promotions.
            // (The following call in fact generates all moves.)
            totalMoves = gen_legal(pos, moves);

            for (m = 0; m < totalMoves; m++) {
                short move = moves[m];
                if (type_of_piece_moved(pos, move) != PAWN || is_capture(pos, move))
                    continue;
                if (!do_move(pos1, pos, move))
                    continue; // not legal
                int v = -probe_wdl(pos1);
                if (success == 0) return 0;
                if (v == wdl) {
                    assert (wdl < 3);
                    return WdlToDtz[wdl + 2];
                }
            }
        }

        // If we are here, we know that the best move is not an ep capture.
        // In other words, the value of wdl corresponds to the WDL value of
        // the position without ep rights. It is therefore safe to probe the
        // DTZ table with the current value of wdl.

        int dtz = probe_dtz_table(pos, wdl);
        if (success >= 0)
            return WdlToDtz[wdl + 2] + ((wdl > 0) ? dtz : -dtz);


        // *success < 0 means we need to probe DTZ for the other side to move.
        int best;
        if (wdl > 0) {
            best = Integer.MAX_VALUE;
        } else {
            // If (cursed) loss, the worst case is a losing capture or pawn move
            // as the "best" move, leading to dtz of -1 or -101.
            // In case of mate, this will cause -1 to be returned.
            best = WdlToDtz[wdl + 2];
            // If wdl < 0, we still have to generate all moves.
            totalMoves = gen_moves(pos, moves);
        }
        assert (totalMoves != 0);

        for (int i = 0; i < totalMoves; i++) {
            short move = moves[i];
            // We can skip pawn moves and captures.
            // If wdl > 0, we already caught them. If wdl < 0, the initial value
            // of best already takes account of them.
            if (is_capture(pos, move) || type_of_piece_moved(pos, move) == PAWN)
                continue;
            if (!do_move(pos1, pos, move)) {
                // move was not legal
                continue;
            }
            int v = -probe_dtz(pos1);
            // Check for the case of mate in 1
            if (v == 1 && is_mate(pos1))
                best = 1;
            else if (wdl > 0) {
                if (v > 0 && v + 1 < best)
                    best = v + 1;
            } else {
                if (v - 1 < best)
                    best = v - 1;
            }
            if (success == 0) return 0;
        }
        return best;
    }

    // Probe the WDL table for a particular position.
    //
    // If *success != 0, the probe was successful.
    //
    // If *success == 2, the position has a winning capture, or the position
    // is a cursed win and has a cursed winning capture, or the position
    // has an ep capture as only best move.
    // This is used in probe_dtz().
    //
    // The return value is from the point of view of the side to move:
    // -2 : loss
    // -1 : loss, but draw under 50-move rule
    //  0 : draw
    //  1 : win, but draw under 50-move rule
    //  2 : win
    private int probe_wdl(SyzygyPosition pos) {
        success = 1;

        // Generate (at least) all legal captures including (under)promotions.
        short[] moves0 = new short[TB_MAX_CAPTURES];
        int moveCount = gen_captures(pos, moves0);
        int bestCap = -3, bestEp = -3;

        // We do capture resolution, letting bestCap keep track of the best
        // capture without ep rights and letting bestEp keep track of still
        // better ep captures if they exist.

        SyzygyPosition pos1 = new SyzygyPosition();
        for (int i = 0; i < moveCount; i++) {
            short move = moves0[i];
            if (!is_capture(pos, move))
                continue;
            if (!do_move(pos1, pos, move))
                continue; // illegal move
            int v = -probe_ab(pos1, -2, -bestCap);
            if (success == 0)
                return 0;
            if (v > bestCap) {
                if (v == 2) {
                    success = 2;
                    return 2;
                }
                if (!is_en_passant(pos, move))
                    bestCap = v;
                else if (v > bestEp)
                    bestEp = v;
            }
        }

        int v = probe_wdl_table(pos);
        if (success == 0) return 0;

        // Now max(v, bestCap) is the WDL value of the position without ep rights.
        // If the position without ep rights is not stalemate or no ep captures
        // exist, then the value of the position is max(v, bestCap, bestEp).
        // If the position without ep rights is stalemate and bestEp > -3,
        // then the value of the position is bestEp (and we will have v == 0).

        if (bestEp > bestCap) {
            if (bestEp > v) { // ep capture (possibly cursed losing) is best.
                success = 2;
                return bestEp;
            }
            bestCap = bestEp;
        }

        // Now max(v, bestCap) is the WDL value of the position unless
        // the position without ep rights is stalemate and bestEp > -3.

        if (bestCap >= v) {
            // No need to test for the stalemate case here: either there are
            // non-ep captures, or bestCap == bestEp >= v anyway.
            success = 1 + (bestCap > 0 ? 1 : 0);
            return bestCap;
        }

        // Now handle the stalemate case.
        if (bestEp > -3 && v == 0) {
            short[] moves = new short[TB_MAX_MOVES];
            int totalMoves = gen_moves(pos, moves);
            // Check for stalemate in the position with ep captures.
            int m = 0;
            for (; m < totalMoves; m++) {
                if (!is_en_passant(pos, moves[m]) && legal_move(pos, moves[m])) break;
            }
            if (m == totalMoves && !is_check(pos)) {
                // stalemate score from tb (w/o e.p.), but an en-passant capture
                // is possible.
                success = 2;
                return bestEp;
            }
        }
        // Stalemate / en passant not an issue, so v is the correct value.

        return v;
    }

    // probe_ab() is not called for positions with en passant captures.
    private int probe_ab(SyzygyPosition pos, int alpha, int beta) {
        assert (pos.ep == 0);

        short[] moves0 = new short[TB_MAX_CAPTURES];
        // Generate (at least) all legal captures including (under)promotions.
        // It is OK to generate more, as long as they are filtered out below.
        int totalMoves = gen_captures(pos, moves0);

        SyzygyPosition pos1 = new SyzygyPosition();
        for (int m = 0; m < totalMoves; m++) {
            short move = moves0[m];
            if (!is_capture(pos, move))
                continue;
            if (!do_move(pos1, pos, move))
                continue; // illegal move
            int v = -probe_ab(pos1, -beta, -alpha);
            if (success == 0) return 0;
            if (v > alpha) {
                if (v >= beta)
                    return v;
                alpha = v;
            }
        }

        int v = probe_wdl_table(pos);

        return Math.max(alpha, v);
    }

    private int probe_wdl_table(SyzygyPosition pos) {
        return probe_table(pos, 0, WDL);
    }

    private int probe_dtm_table(SyzygyPosition pos, int won) {
        return probe_table(pos, won, DTM);
    }

    private int probe_dtz_table(SyzygyPosition pos, int wdl) {
        return probe_table(pos, wdl, DTZ);
    }

    private int probe_table(SyzygyPosition syzygyPosition, int score, TableBase.TableType type) {
        long key = calcKey(syzygyPosition);

        int hashIdx = (int) (key >>> (64 - TB_HASHBITS));
        final int hashIdxStart = hashIdx;

        while (tbHash[hashIdx] == null || tbHash[hashIdx].key != key) {
            hashIdx = (hashIdx + 1) & ((1 << TB_HASHBITS) - 1);
            if (hashIdx == hashIdxStart) {
                return 0;
            }
        }

        BaseEntry be = tbHash[hashIdx].ptr;

        return switch (type) {
            case WDL -> be.probe_wdl(syzygyPosition, key, score);
            case DTZ -> be.probe_dtz(syzygyPosition, key, score);
            default -> throw new IllegalArgumentException("Unknown table type: " + type);
        };
    }

    /*
     * Given a position, produce a 64-bit material signature key.
     */
    static long calcKey(SyzygyPosition pos, boolean mirror) {
        long white = pos.white, black = pos.black;
        if (mirror) {
            long tmp = white;
            white = black;
            black = tmp;
        }
        return calcKey(white, black,
                pos.queens, pos.rooks,
                pos.bishops, pos.knights,
                pos.pawns);
    }

    static long calcKey(SyzygyPosition pos) {
        return calcKey(pos.white, pos.black,
                pos.queens, pos.rooks,
                pos.bishops, pos.knights,
                pos.pawns);
    }

    static long calcKey(long white, long black, long queens, long rooks, long bishops, long knights, long pawns) {
        return popcount(white & queens) * W_QUEEN.getPrime() +
                popcount(white & rooks) * W_ROOK.getPrime() +
                popcount(white & bishops) * W_BISHOP.getPrime() +
                popcount(white & knights) * W_KNIGHT.getPrime() +
                popcount(white & pawns) * W_PAWN.getPrime() +
                popcount(black & queens) * B_QUEEN.getPrime() +
                popcount(black & rooks) * B_ROOK.getPrime() +
                popcount(black & bishops) * B_BISHOP.getPrime() +
                popcount(black & knights) * B_KNIGHT.getPrime() +
                popcount(black & pawns) * B_PAWN.getPrime();
    }
}
