package net.chesstango.piazzolla.polyglot;

import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static net.chesstango.gardel.fen.FENParser.INITIAL_FEN;

/**
 * @author Mauricio Coria
 */
public class PolyglotBookTest {

    @Test
    @Disabled
    public void testReadBook() throws IOException {
        //String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\komodo.bin";
        String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2023\\BIN\\Perfect2023.bin";
        //String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\chesstango\\chesstango.bin";
        try (PolyglotBook book = PolyglotBook.open(Path.of(bookPath))) {

            //book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\final-book.bin"));
            //book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2021\\BIN\\Perfect2021.bin"));

            long polyglotKey = getPolyglotKey(INITIAL_FEN);
            //long polyglotKey = getPolyglotKey("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
            //long polyglotKey = 0xDA48997503D0L;       // First entry in the book
            //long polyglotKey = 0xFFFD4170FE7E8A0AL;  // Last entry in the book

            List<PolyglotEntry> possibleMoves = book.search(polyglotKey);

            if (possibleMoves != null) {
                for (PolyglotEntry move : possibleMoves) {
                    System.out.println(move);
                }
            }
        }
    }

    private long getPolyglotKey(String fen) {
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        FEN.of(fen).export(polyglotKeyBuilder);
        return polyglotKeyBuilder.getPositionRepresentation();
    }
}
