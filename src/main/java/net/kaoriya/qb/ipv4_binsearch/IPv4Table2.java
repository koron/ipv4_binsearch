package net.kaoriya.qb.ipv4_binsearch;

/**
 * Less objects version of IPv4Table.
 */
public final class IPv4Table2<T> extends IPv4TableBase<T>
{
    private final IntRangeIndexArray indexArray;

    public IPv4Table2(IPv4Table<T> src) {
        this.indexArray = new IntRangeIndexArray(src.rangeTable);
        // TODO:
    }

    public void add(int start, int end, T data) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public T find(int value) {
        int index = this.indexArray.findIndex(value);
        return index >= 0 ? get(index) : null;
    }

    private T get(int index) {
        // TODO:
        return null;
    }
}
