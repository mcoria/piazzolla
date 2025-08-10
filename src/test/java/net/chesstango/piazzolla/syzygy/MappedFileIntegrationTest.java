package net.chesstango.piazzolla.syzygy;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.chesstango.piazzolla.syzygy.MappedFile.test_tb;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class MappedFileIntegrationTest {

    @Test
    public void test_test_tb() {
        assertFalse(test_tb(Path.of("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5"), "KQvK", ".rtbm"));
    }
}
