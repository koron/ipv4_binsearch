package net.kaoriya.qb.ipv4_binsearch;

import java.lang.reflect.Array;

public final class IntRangeIndexArray
{
    final int length;
    final int[] rangeArray;

    public IntRangeIndexArray(IntRangeTable<?> table)
    {
        this.length = table.arrayList.size();
        this.rangeArray = new int[this.length * 2];
        // initialize array.
        for (int i = 0; i < this.length; ++i) {
            IntRangeData<?> d = table.arrayList.get(i);
            this.rangeArray[i * 2 + 0] = d.getStart();
            this.rangeArray[i * 2 + 1] = d.getEnd();
        }
    }

    public int findIndex(int value)
    {
        int start = 0;
        int end = this.length - 1;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            if (value < this.rangeArray[mid * 2]) {
                end = mid - 1;
            } else if (value > this.rangeArray[mid * 2 + 1]) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
