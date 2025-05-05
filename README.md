[![Java CI with Maven](https://github.com/mcoria/piazzolla/actions/workflows/maven.yml/badge.svg)](https://github.com/mcoria/piazzolla/actions/workflows/maven.yml)
# Piazzolla

Piazzolla is a Java library designed for working with Syzygy Endgame Tablebases (EGTB) and Polyglot opening books. It provides tools for loading and searching both types of chess data.

## Features

- Load and query Syzygy endgame tablebases.
- Load Polyglot opening books.
- Search for entries in the opening book using a key.
- Lightweight and easy to integrate into Java projects.

## Requirements

- Java 21 or higher

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>net.chesstango</groupId>
    <artifactId>piazzolla</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Loading a Polyglot Book

```java
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.MappedPolyglotBook;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        PolyglotBook book = new MappedPolyglotBook();
        book.load(Path.of("path/to/polyglot/book.bin"));

        if (book.isLoaded()) {
            System.out.println("Polyglot book loaded successfully!");
        }
    }
}
```

### Searching for Entries

```java
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.polyglot.MappedPolyglotBook;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;

import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PolyglotBook book = new MappedPolyglotBook();
        book.load(Path.of("path/to/polyglot/book.bin"));

        long key = 123456789L; // Example key
        List<PolyglotEntry> entries = book.search(key);

        entries.forEach(entry -> System.out.println(entry));
    }
}
```

### Loading a Syzygy Tablebase

```java
import net.chesstango.piazzolla.syzygy.SyzygyTablebase;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        SyzygyTablebase tablebase = new SyzygyTablebase();
        tablebase.load(Path.of("path/to/syzygy/tablebase"));

        if (tablebase.isLoaded()) {
            System.out.println("Syzygy tablebase loaded successfully!");
        }
    }
}
```

## License

This project is licensed under the BSD 3-Clause License. See the [LICENSE](https://github.com/mcoria/piazzolla?tab=BSD-3-Clause-1-ov-file) file for details.


## Contributing

Contributions are welcome! Feel free to submit issues or pull requests to improve the project.


