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
        String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\komodo.bin";
        //String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2023\\BIN\\Perfect2023.bin";
        //String bookPath = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\chesstango\\chesstango.bin";
        try (PolyglotBook book = PolyglotBook.open(Path.of(bookPath))) {

            //book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\final-book.bin"));
            //book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\Perfect_2021\\BIN\\Perfect2021.bin"));

            //long polyglotKey = getPolyglotKey(INITIAL_FEN);
            long polyglotKey = getPolyglotKey("r1bqkb1r/1p1n1ppp/p2ppn2/6B1/3NPP2/2N5/PPP1Q1PP/R3KB1R b KQkq - 1 8");
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
