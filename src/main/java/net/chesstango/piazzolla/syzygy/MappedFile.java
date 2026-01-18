package net.chesstango.piazzolla.syzygy;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/**
 * @author Mauricio Coria
 */
class MappedFile implements Closeable {

    RandomAccessFile file;
    MemorySegment memorySegment;

    boolean map_tb(Path directory, String fileName, String suffix) {
        Path pathToRead = directory.resolve(String.format("%s%s", fileName, suffix));
        try {

            Arena globalArena = Arena.global();

            file = new RandomAccessFile(pathToRead.toFile(), "r");

            FileChannel channel = file.getChannel();

            memorySegment = channel.map(READ_ONLY, 0, channel.size(), globalArena);

            return true;

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    byte read_uint8_t(long idx) {
        return memorySegment.get(ValueLayout.JAVA_BYTE, idx);
    }

    short read_le_u16(long idx) {
        return memorySegment.get(ValueLayout.JAVA_SHORT_UNALIGNED, idx);
    }

    int read_le_u32(long idx) {
        return memorySegment.get(ValueLayout.JAVA_INT_UNALIGNED, idx);
    }

    long read_le_u64(long idx) {
        return memorySegment.get(ValueLayout.JAVA_LONG_UNALIGNED, idx);
    }

    static boolean test_tb(Path directory, String fileName, String suffix) {
        Path file = directory.resolve(String.format("%s%s", fileName, suffix));
        return Files.exists(file);
    }
}
