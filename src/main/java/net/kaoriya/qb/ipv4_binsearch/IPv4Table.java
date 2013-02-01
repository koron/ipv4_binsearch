package net.kaoriya.qb.ipv4_binsearch;

public final class IPv4Table<T>
{

    private final IntRangeTable<T> rangeTable = new IntRangeTable<>();

    public IPv4Table() {
    }

    public void add(IPv4 start, IPv4 end, T data) {
        this.rangeTable.add(IPv4Integer.valueOf(start),
                IPv4Integer.valueOf(end), data);
    }

    public void add(int start, int end, T data) {
        this.rangeTable.add(IPv4Integer.valueOf(start),
                IPv4Integer.valueOf(end), data);
    }

    public void add(IPv4Mask mask, T data) {
        this.rangeTable.add(IPv4Integer.valueOf(mask.getStart()),
                IPv4Integer.valueOf(mask.getEnd()), data);
    }

    public void clear() {
        this.rangeTable.clear();
    }

    public T find(IPv4 value) {
        return this.rangeTable.find(IPv4Integer.valueOf(value));
    }

    public T find(int value) {
        return this.rangeTable.find(IPv4Integer.valueOf(value));
    }

}
