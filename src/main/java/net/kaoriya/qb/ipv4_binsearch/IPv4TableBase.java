package net.kaoriya.qb.ipv4_binsearch;

public abstract class IPv4TableBase<T> implements IPv4Map<T>
{
    public void add(IPv4 start, IPv4 end, T data) {
        add(IPv4Integer.valueOf(start), IPv4Integer.valueOf(end), data);
    }

    public void add(IPv4Mask mask, T data) {
        add(IPv4Integer.valueOf(mask.getStart()),
                IPv4Integer.valueOf(mask.getEnd()), data);
    }

    public T find(IPv4 value) {
        return find(IPv4Integer.valueOf(value));
    }
}
