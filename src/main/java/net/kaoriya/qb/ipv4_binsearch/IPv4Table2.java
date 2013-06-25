package net.kaoriya.qb.ipv4_binsearch;

import java.io.Closeable;
import java.io.IOException;

import net.kaoriya.qb.serialized_array.Converter;
import net.kaoriya.qb.serialized_array.HeapArray;
import net.kaoriya.qb.serialized_array.OffHeapArray;

/**
 * Less objects version of IPv4Table.
 */
public final class IPv4Table2<T> extends IPv4TableBase<T>
    implements Closeable
{
    private final IntRangeIndexArray indexArray;

    private final OffHeapArray<T> dataArray;

    public IPv4Table2(
            IPv4Table<T> src,
            Converter<T> converter)
        throws Exception
    {
        this.indexArray = new IntRangeIndexArray(src.rangeTable);

        HeapArray heapArray = new HeapArray<T>(converter);
        for (int i = 0, max = src.rangeTable.arrayList.size(); i < max; ++i) {
            heapArray.add(src.rangeTable.getData(i));
        }

        this.dataArray = new OffHeapArray(heapArray);
    }

    public void add(int start, int end, T data) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    private T findRaw(int value) {
        int index = this.indexArray.findIndex(value);
        return index >= 0 ? get(index) : null;
    }

    @Override
    public T find(IPv4 value) {
        return findRaw(IPv4Integer.valueOf(value));
    }

    public T find(int value) {
        return findRaw(IPv4Integer.valueOf(value));
    }

    private T get(int index) {
        try {
            return this.dataArray.get(index);
        } catch (IOException e) {
            return null;
        }
    }

    public void close()
    {
        this.dataArray.close();
    }
}
