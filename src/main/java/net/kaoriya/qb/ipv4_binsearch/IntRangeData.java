package net.kaoriya.qb.ipv4_binsearch;

public class IntRangeData<T>
{

    private final int start;

    private final int end;

    private final T data;

    public IntRangeData(int start, int end, T data) {
        if (start <= end) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
        this.data = data;
    }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }

    public T getData() { return this.data; }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append("IntRangeData{")
            .append("start=").append(this.start).append(",")
            .append("end=").append(this.end).append(",")
            .append("data=").append(this.data).append("}");
        return s.toString();
    }

}
