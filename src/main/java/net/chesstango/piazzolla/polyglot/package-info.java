/**
 * Provides Polyglot opening book reading and key calculation capabilities.
 * <p>
 * This package contains the public API for interacting with Polyglot opening books:
 * <ul>
 *   <li>{@link net.chesstango.piazzolla.polyglot.PolyglotBook}: Primary interface for opening and querying Polyglot opening book files.</li>
 *   <li>{@link net.chesstango.piazzolla.polyglot.PolyglotEntry}: Record representing a single book entry (key, move, and weight).</li>
 *   <li>{@link net.chesstango.piazzolla.polyglot.PolyglotKeyBuilder}: Builder for computing 64-bit Polyglot Zobrist hash keys from chess positions.</li>
 * </ul>
 */
package net.chesstango.piazzolla.polyglot;
