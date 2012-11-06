package net.kaoriya.qb.ipv4_binsearch;

import java.util.ArrayList;

public final class IntRangeTable<T>
{

    private final ArrayList<IntRangeData<T>> arrayList = new ArrayList();

    public IntRangeTable() {
    }

    /**
     * Clear all items in the table.
     */
    public void clear() {
        this.arrayList.clear();
    }

    /**
     * Add an IntRangeData into the table.
     *
     * Overlaped item is not allowed.
     */
    public void add(IntRangeData<T> data) {
        int index = findIndexToAdd(data.getStart(), data.getEnd());
        if (index < 0) {
            throw new RuntimeException("Range overlap: " + data);
        }
        this.arrayList.add(index, data);
    }

    /**
     * Add a range and data value into the table.
     *
     * Overlaped item is not allowed.
     */
    public void add(int start, int end, T data) {
        add(new IntRangeData<T>(start, end, data));
    }

    private int findIndexToAdd(int rangeStart, int rangeEnd) {
        int start = 0;
        int end = this.arrayList.size() - 1;
        int hit = 0;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            IntRangeData<T> c = this.arrayList.get(mid);
            if (rangeEnd < c.getStart()) {
                end = mid - 1;
                hit = mid;
            } else if (rangeStart > c.getEnd()) {
                start = mid + 1;
                hit = start;
            } else {
                return -1;
            }
        }
        return hit;
    }

    /**
     * Find a data value for range which includes argument value.
     */
    public T find(int value) {
        IntRangeData<T> data = findRangeData(value);
        return data == null ? null : data.getData();
    }

    /**
     * Find an IntRangeData which includes argument value.
     */
    public IntRangeData<T> findRangeData(int value) {
        int index = findIndexOfInclude(value);
        return index < 0 ? null : this.arrayList.get(index);
    }

    private int findIndexOfInclude(int value) {
        int start = 0;
        int end = this.arrayList.size() - 1;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            IntRangeData<T> c = this.arrayList.get(mid);
            if (value < c.getStart()) {
                end = mid - 1;
            } else if (value > c.getEnd()) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

}
