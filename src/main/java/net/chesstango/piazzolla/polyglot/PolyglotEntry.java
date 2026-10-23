package net.chesstango.piazzolla.polyglot;

import java.io.Serializable;

/**
 * Represents a single move entry in a Polyglot opening book.
 * <p>
 * A Polyglot book consists of 16-byte binary entries ordered by position key:
 * <ul>
 *   <li><b>key</b> (64-bit unsigned integer): Zobrist hash representing the chess position.</li>
 *   <li><b>move</b> (16-bit integer): Encodes the origin square, destination square, and promotion piece.</li>
 *   <li><b>weight</b> (16-bit integer): Frequency or priority score assigned to the move.</li>
 *   <li><b>learn</b> (32-bit integer): Reserved for learning algorithms / statistics (not stored in this record).</li>
 * </ul>
 * <p>
 * Square files and ranks are 0-indexed where file 0 corresponds to 'a' through 7 to 'h',
 * and rank 0 corresponds to 1 through 7 to 8.
 *
 * @param key the 64-bit Polyglot Zobrist hash of the chess position
 * @param from_file the source square file index (0 for 'a' through 7 for 'h')
 * @param from_rank the source square rank index (0 for '1' through 7 for '8')
 * @param to_file the destination square file index (0 for 'a' through 7 for 'h')
 * @param to_rank the destination square rank index (0 for '1' through 7 for '8')
 * @param weight the weight or quality score of the move
 *
 * @see <a href="http://hgm.nubati.net/book_format.html">Polyglot Book Format Specification</a>
 * @author Mauricio Coria
 */
public record PolyglotEntry(long key, int from_file, int from_rank, int to_file, int to_rank, int weight) implements Serializable {

    /**
     * Returns a string representation of this entry in coordinate notation with weight (e.g., "e2 e4 100").
     *
     * @return a string containing the move in coordinate notation followed by the weight
     */
    @Override
    public String toString() {
        char fromFileChar = (char) ('a' + from_file);
        char toFileChar = (char) ('a' + to_file);

        return String.format("%c%d %c%d %d", fromFileChar, from_rank + 1, toFileChar, to_rank + 1, weight);
    }
}
