package net.chesstango.piazzolla.polyglot;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Mauricio Coria
 */
class MappedPolyglotBook implements PolyglotBook {
    private final static int ENTRY_SIZE = 16;
    private FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;
    private int maxUpperBoundIdx = 0;

    void load(Path pathToRead) throws IOException {
        fileChannel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

        maxUpperBoundIdx = (int) (fileChannel.size() / ENTRY_SIZE) - 1;

        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
    }

    @Override
    public void close() throws IOException {
        if (fileChannel != null) {
            fileChannel.close();
        }
    }

    @Override
    public List<PolyglotEntry> search(long key) {
        int idx = findIndex(key, 0, maxUpperBoundIdx + 1);

        if (getKey(idx) == key) {
            List<PolyglotEntry> polyglotEntryList = new ArrayList<>();

            // Corregir idx para encontrar la base
            while (idx > 0 && getKey(idx - 1) == key) {
                idx--;
            }

            while (idx <= maxUpperBoundIdx && getKey(idx) == key) {

                int moveAndWeight = mappedByteBuffer.getInt(idx * ENTRY_SIZE + 8);

                PolyglotEntry entry = createPolyglotEntry(key, moveAndWeight);

                polyglotEntryList.add(entry);

                idx++;
            }

            return polyglotEntryList;
        }

        return Collections.emptyList();
    }

    private int findIndex(final long key, final int lowerBoundIdx, final int upperBoundIdx) {
        if (lowerBoundIdx + 1 >= upperBoundIdx) {
            return lowerBoundIdx;
        }

        final int middleIdx = (lowerBoundIdx + upperBoundIdx) / 2;

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

    private long getKey(int idx) {
        return mappedByteBuffer.getLong(idx * ENTRY_SIZE);
    }

    private static PolyglotEntry createPolyglotEntry(long key, int moveAndWeight) {
        int weight = moveAndWeight & 0b00000000_00000000_11111111_11111111;

        int toFile = ((moveAndWeight & 0b00000000_00000111_00000000_00000000) >>> (16));
        int toRank = ((moveAndWeight & 0b00000000_00111000_00000000_00000000) >>> (16 + 3));

        int fromFile = ((moveAndWeight & 0b00000001_11000000_00000000_00000000) >>> (16 + 6));
        int fromRank = ((moveAndWeight & 0b00001110_00000000_00000000_00000000) >>> (16 + 9));

        return new PolyglotEntry(key, fromFile, fromRank, toFile, toRank, weight);
    }

}
