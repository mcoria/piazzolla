package net.chesstango.piazzolla.polyglot;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Public interface for querying and reading Polyglot opening books.
 * <p>
 * A Polyglot opening book is a binary file composed of 16-byte entries ordered by 64-bit Zobrist key.
 * This interface provides capabilities to query book entries matching a position's Polyglot key and to open
 * opening book files via memory mapping.
 *
 * @author Mauricio Coria
 */
public interface PolyglotBook extends Closeable {
    /**
     * Searches for all entries in the Polyglot book that match the provided key.
     *
     * @param key the 64-bit Zobrist key used to identify Polyglot entries in the book
     * @return a list of {@link PolyglotEntry} objects that match the specified key, or an empty list if no entries are found
     */
    List<PolyglotEntry> search(long key);

    /**
     * Opens a Polyglot book from the specified file path.
     * <p>
     * The method initializes a {@code MappedPolyglotBook} instance
     * and loads its data from the given file path into memory.
     *
     * @param filePath the file path from which to load the Polyglot book; must not be null
     * @return a {@link PolyglotBook} instance representing the loaded book
     * @throws IOException if an I/O error occurs while reading or mapping the file
     */
    static PolyglotBook open(Path filePath) throws IOException {
        MappedPolyglotBook book = new MappedPolyglotBook();
        book.load(filePath);
        return book;
    }
}
