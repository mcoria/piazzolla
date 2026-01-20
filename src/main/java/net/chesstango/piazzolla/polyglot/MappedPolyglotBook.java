package net.chesstango.piazzolla.polyglot;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/**
 * @author Mauricio Coria
 */
class MappedPolyglotBook implements PolyglotBook {
    private final static long ENTRY_SIZE = 16L;

    private RandomAccessFile file;
    private MemorySegment memorySegment;

    private long maxUpperBoundIdx = 0;

    void load(Path pathToRead) throws IOException {
        Arena globalArena = Arena.global();

        file = new RandomAccessFile(pathToRead.toFile(), "r");

        FileChannel channel = file.getChannel();

        memorySegment = channel.map(READ_ONLY, 0, channel.size(), globalArena);

        maxUpperBoundIdx = (channel.size() / ENTRY_SIZE) - 1L;
    }

    @Override
    public void close() throws IOException {
        if (file != null) {
            file.close();
        }
    }

    @Override
    public List<PolyglotEntry> search(long key) {
        List<PolyglotEntry> polyglotEntryList = Collections.emptyList();

        long idx = findIndex(key, 0, maxUpperBoundIdx + 1);

        if (getKey(idx) == key) {
            polyglotEntryList = new LinkedList<>();

            // Corregir idx para encontrar la base
            while (idx > 0 && getKey(idx - 1) == key) {
                idx--;
            }

            while (idx <= maxUpperBoundIdx && getKey(idx) == key) {

                int moveAndWeight = memorySegment.get(ValueLayout.JAVA_INT.withOrder(ByteOrder.BIG_ENDIAN), idx * ENTRY_SIZE + 8);

                PolyglotEntry entry = createPolyglotEntry(key, moveAndWeight);

                polyglotEntryList.add(entry);

                idx++;
            }
        }

        return polyglotEntryList;
    }

    private long findIndex(final long key, final long lowerBoundIdx, final long upperBoundIdx) {
        if (lowerBoundIdx + 1 >= upperBoundIdx) {
            return lowerBoundIdx;
        }

        final long middleIdx = (lowerBoundIdx + upperBoundIdx) / 2;

        final long middleKey = getKey(middleIdx);

        final int unsignedCompare = Long.compareUnsigned(key, middleKey);

        if (unsignedCompare == 0) {
            return middleIdx;
        } else if (unsignedCompare < 0) {
            return findIndex(key, lowerBoundIdx, middleIdx);
        } else {
            return findIndex(key, middleIdx, upperBoundIdx);
        }
    }

    private long getKey(long idx) {
        return memorySegment.get(ValueLayout.JAVA_LONG.withOrder(ByteOrder.BIG_ENDIAN), idx * ENTRY_SIZE);
    }


    /**
     * Deberia leer y decodificar por separado a move y weight
     */
    private static PolyglotEntry createPolyglotEntry(long key, int moveAndWeight) {
        int weight = moveAndWeight & 0b00000000_00000000_11111111_11111111;

        int toFile = ((moveAndWeight & 0b00000000_00000111_00000000_00000000) >>> (16));
        int toRank = ((moveAndWeight & 0b00000000_00111000_00000000_00000000) >>> (16 + 3));

        int fromFile = ((moveAndWeight & 0b00000001_11000000_00000000_00000000) >>> (16 + 6));
        int fromRank = ((moveAndWeight & 0b00001110_00000000_00000000_00000000) >>> (16 + 9));

        return new PolyglotEntry(key, fromFile, fromRank, toFile, toRank, weight);
    }

}
