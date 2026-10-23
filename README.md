[![Java CI with Maven](https://github.com/mcoria/piazzolla/actions/workflows/maven.yml/badge.svg)](https://github.com/mcoria/piazzolla/actions/workflows/maven.yml)
# Piazzolla

Piazzolla is a Java library designed for working with Syzygy Endgame Tablebases (EGTB) and Polyglot opening books. It provides fast, memory-mapped probing tools for both endgame tables (WDL and DTZ) and opening book files.

## Features

- **Syzygy Endgame Tablebases**:
  - Pure Java implementation, NO bridge to external libraries through JNI nor FFM.
  - Probe Win-Draw-Loss (WDL) tables (`.rtbw`) for fast evaluation during search.
  - Probe Distance-To-Zero (DTZ) tables (`.rtbz`) to find optimal moves and DTZ distance at the root.
  - Support for multi-directory tablebase configurations (3-4-5-6 pieces).
  - Helper methods to decode result values (WDL outcome, DTZ ply count, move coordinates, promotions).
- **Polyglot Opening Books**:
  - Memory-mapped reading and binary search of Polyglot `.bin` opening books.
  - Polyglot 64-bit Zobrist key generation from chess positions and FEN strings.
- **Integration**:
  - Direct integration with [Gardel](https://github.com/mcoria/gardel) for FEN handling.

## Requirements

- Java 25 or higher

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>net.chesstango</groupId>
    <artifactId>piazzolla</artifactId>
    <version>1.1.4</version>
</dependency>
```

## Usage

### Syzygy Endgame Tablebases

#### 1. Probing Win-Draw-Loss (WDL)

```java
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;

import java.nio.file.Path;
import java.util.Set;

public class SyzygyWdlExample {
    public static void main(String[] args) {
        Set<Path> tablebasePaths = Set.of(
            Path.of("/path/to/syzygy/3-4-5"),
            Path.of("/path/to/syzygy/6-WDL")
        );

        try (Syzygy syzygy = Syzygy.open(tablebasePaths)) {
            FEN fen = FEN.from("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1");
            SyzygyPosition position = SyzygyPosition.from(fen);

            int wdl = syzygy.tb_probe_wdl(position);

            if (wdl != Syzygy.TB_RESULT_FAILED) {
                switch (wdl) {
                    case Syzygy.TB_WIN -> System.out.println("Win");
                    case Syzygy.TB_CURSED_WIN -> System.out.println("Cursed Win (drawn by 50-move rule)");
                    case Syzygy.TB_DRAW -> System.out.println("Draw");
                    case Syzygy.TB_BLESSED_LOSS -> System.out.println("Blessed Loss (drawn by 50-move rule)");
                    case Syzygy.TB_LOSS -> System.out.println("Loss");
                }
            } else {
                System.out.println("Position not found in tablebase");
            }
        }
    }
}
```

#### 2. Probing Root Move and Distance-To-Zero (DTZ)

```java
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;

import java.nio.file.Path;
import java.util.Set;

public class SyzygyRootExample {
    public static void main(String[] args) {
        Set<Path> tablebasePaths = Set.of(
            Path.of("/path/to/syzygy/3-4-5"),
            Path.of("/path/to/syzygy/6-WDL"),
            Path.of("/path/to/syzygy/6-DTZ")
        );

        try (Syzygy syzygy = Syzygy.open(tablebasePaths)) {
            FEN fen = FEN.from("7k/8/7K/7Q/8/8/8/8 w - - 0 1");
            SyzygyPosition position = SyzygyPosition.from(fen);

            int[] results = new int[Syzygy.TB_MAX_MOVES];
            int bestMoveResult = syzygy.tb_probe_root(position, results);

            if (bestMoveResult != Syzygy.TB_RESULT_FAILED) {
                int wdl = Syzygy.TB_GET_WDL(bestMoveResult);
                int dtz = Syzygy.TB_GET_DTZ(bestMoveResult);
                int fromSquare = Syzygy.TB_GET_FROM(bestMoveResult);
                int toSquare = Syzygy.TB_GET_TO(bestMoveResult);
                int promotes = Syzygy.TB_GET_PROMOTES(bestMoveResult);

                System.out.printf("Best move: %d -> %d (promotes: %d), DTZ: %d plies, WDL: %d%n",
                        fromSquare, toSquare, promotes, dtz, wdl);
            }
        }
    }
}
```

### Polyglot Opening Books

#### 1. Searching Moves by Polyglot Key

```java
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;
import net.chesstango.piazzolla.polyglot.PolyglotKeyBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PolyglotExample {
    public static void main(String[] args) throws IOException {
        Path bookPath = Path.of("/path/to/polyglot/book.bin");

        try (PolyglotBook book = PolyglotBook.open(bookPath)) {
            // Generate Polyglot 64-bit Zobrist key from FEN
            PolyglotKeyBuilder keyBuilder = new PolyglotKeyBuilder();
            FEN.from("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").export(keyBuilder);
            long key = keyBuilder.getPositionRepresentation();

            List<PolyglotEntry> entries = book.search(key);

            for (PolyglotEntry entry : entries) {
                // PolyglotEntry toString formats move in coordinate notation with weight (e.g. "e2 e4 100")
                System.out.println(entry);
            }
        }
    }
}
```

## Acknowledgement

Thanks to Ronald de Man for his [Syzygy tablebases](https://github.com/syzygy1/tb).
The probing code is closely based on his implementation for Stockfish.

## License

This project is licensed under the BSD 3-Clause License. See the [LICENSE](https://github.com/mcoria/piazzolla?tab=BSD-3-Clause-1-ov-file) file for details.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests to improve the project.


