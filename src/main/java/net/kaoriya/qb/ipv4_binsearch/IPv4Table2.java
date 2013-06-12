package net.kaoriya.qb.ipv4_binsearch;

import java.io.IOException;

import net.kaoriya.qb.serialized_array.BytesStore;
import net.kaoriya.qb.serialized_array.Converter;
import net.kaoriya.qb.serialized_array.SerializedArray;

/**
 * Less objects version of IPv4Table.
 */
public final class IPv4Table2<T> extends IPv4TableBase<T>
{
    private final IntRangeIndexArray indexArray;

    private final SerializedArray<T> dataArray;

    public IPv4Table2(
            IPv4Table<T> src,
            Converter<T> converter,
            BytesStore bytesStore)
    {
        this.indexArray = new IntRangeIndexArray(src.rangeTable);
        this.dataArray = new SerializedArray<T>(converter, bytesStore);
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
        try {
            return this.dataArray.get(index);
        } catch (IOException e) {
            return null;
        }
    }
}
