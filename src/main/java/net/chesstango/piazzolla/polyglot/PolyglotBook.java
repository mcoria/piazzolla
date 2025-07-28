package net.chesstango.piazzolla.polyglot;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface PolyglotBook extends Closeable {
    /**
     * Searches for all entries in the Polyglot book that match the provided key.
     *
     * @param key the 64-bit key used to identify Polyglot entries in the book.
     * @return a list of {@code PolyglotEntry} objects that match the specified key.
     *         If no entries are found, an empty list or {@code null} may be returned.
     */
    List<PolyglotEntry> search(long key);

    /**
     * Opens a Polyglot book from the specified file syzygyDirectory.
     * The method initializes a {@code MappedPolyglotBook} instance
     * and loads its data from the given file syzygyDirectory.
     *
     * @param path the file syzygyDirectory from which to load the Polyglot book; must not be null
     * @return a {@code PolyglotBook} instance representing the loaded book
     * @throws IOException if an I/O error occurs while reading the file
     */
    static PolyglotBook open(Path path) throws IOException {
        MappedPolyglotBook book = new MappedPolyglotBook();
        book.load(path);
        return book;
    }
}
