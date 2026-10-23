/**
 * Module designed for working with Syzygy Endgame Tablebases (EGTB) and Polyglot opening books. It provides fast, memory-mapped probing tools for both endgame tables (WDL and DTZ) and opening book files.
 */
module net.chesstango.piazzolla {
    exports net.chesstango.piazzolla.polyglot;
    exports net.chesstango.piazzolla.syzygy;

    requires net.chesstango.gardel;
    requires static lombok;
}