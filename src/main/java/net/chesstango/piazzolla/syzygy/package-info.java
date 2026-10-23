/**
 * Provides Syzygy endgame tablebase (WDL and DTZ) probing capabilities.
 * <p>
 * This package contains the public API for interacting with Syzygy tablebases:
 * <ul>
 *   <li>{@link net.chesstango.piazzolla.syzygy.Syzygy}: Primary interface for probing WDL and DTZ tables and decoding result codes.</li>
 *   <li>{@link net.chesstango.piazzolla.syzygy.SyzygyImp}: Default implementation of the tablebase prober.</li>
 *   <li>{@link net.chesstango.piazzolla.syzygy.SyzygyPosition}: Bitboard representation of a chess position formatted for tablebase probing.</li>
 *   <li>{@link net.chesstango.piazzolla.syzygy.SyzygyPositionBuilder}: Builder for creating position representations from chess board state.</li>
 * </ul>
 */
package net.chesstango.piazzolla.syzygy;
