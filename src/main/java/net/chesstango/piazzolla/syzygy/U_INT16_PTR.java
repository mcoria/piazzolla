package net.chesstango.piazzolla.syzygy;

/**
 * @author Mauricio Coria
 */
class U_INT16_PTR implements Cloneable{
    final MappedFile mappedFile;
    long ptr = 0;

    U_INT16_PTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    void incPtr(long inc) {
        ptr += 2 * inc;
    }

    short read_le_u16(long offset) {
        return mappedFile.read_le_u16(ptr + 2 * offset);
    }

    @Override
    public U_INT16_PTR clone() {
        U_INT16_PTR u_int16_ptr = new U_INT16_PTR(mappedFile);
        u_int16_ptr.ptr = ptr;
        return u_int16_ptr;
    }
}
