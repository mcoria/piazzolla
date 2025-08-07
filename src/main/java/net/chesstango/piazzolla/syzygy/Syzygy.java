package net.chesstango.piazzolla.syzygy;

import java.nio.file.Path;

/**
 * @author Mauricio Coria
 */
public interface Syzygy {
    int tb_probe_root(SyzygyPosition pos, int[] results);

    int tb_largest();

    static Syzygy open(Path syzygyDirectory) {
        SyzygyImp syzygy = new SyzygyImp(syzygyDirectory);
        syzygy.tb_init();
        return syzygy;
    }
}
