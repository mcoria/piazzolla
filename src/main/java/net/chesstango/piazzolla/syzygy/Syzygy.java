package net.chesstango.piazzolla.syzygy;

import java.nio.file.Path;

/**
 * @author Mauricio Coria
 */
public interface Syzygy {
    int TB_MAX_MOVES = (192 + 1);
    int TB_WIN = 4;                /* WIN  */
    int TB_RESULT_CHECKMATE = SyzygyConstants.TB_SET_WDL(0, TB_WIN);
    int TB_DRAW = 2;               /* DRAW */
    int TB_RESULT_STALEMATE = SyzygyConstants.TB_SET_WDL(0, TB_DRAW);
    int TB_RESULT_FAILED = 0xFFFFFFFF;
    int TB_LOSS = 0;               /* LOSS */
    int TB_BLESSED_LOSS = 1;       /* LOSS but 50-move draw */
    int TB_CURSED_WIN = 3;         /* WIN but 50-move draw  */

    static int TB_GET_WDL(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_WDL_MASK) >>> SyzygyConstants.TB_RESULT_WDL_SHIFT;
    }

    static int TB_GET_DTZ(int _res) {
        return ((_res) & SyzygyConstants.TB_RESULT_DTZ_MASK) >> SyzygyConstants.TB_RESULT_DTZ_SHIFT;
    }

    int tb_probe_root(SyzygyPosition pos, int[] results);

    int tb_largest();

    static Syzygy open(Path syzygyDirectory) {
        SyzygyImp syzygy = new SyzygyImp(syzygyDirectory);
        syzygy.tb_init();
        return syzygy;
    }
}
