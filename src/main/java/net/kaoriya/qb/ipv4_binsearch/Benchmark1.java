package net.kaoriya.qb.ipv4_binsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import net.kaoriya.qb.serialized_array.MessagePackableConverter;

public class Benchmark1
{

    public static class Watch {
        private final String message;
        private final long startTime;
        public Watch(String message) {
            this.message = message;
            this.startTime = System.nanoTime();
        }
        public void stop() {
            long d = System.nanoTime() - this.startTime;
            System.out.println(String.format(
                        "[%2$12.6fs] %1$s", this.message, d * 0.000000001));
        }
    }

    public static class Value implements MessagePackable {
        private String stringProperty;
        public Value() { }
        public Value(String stringValue) {
            this.stringProperty = stringValue;
        }
        public String getStringProperty() {
            return this.stringProperty;
        }
        public void setStringProperty(String value) {
            this.stringProperty = value;
        }
        public void readFrom(Unpacker unpacker) throws IOException {
            this.stringProperty = unpacker.readString();
        }
        public void writeTo(Packer packer) throws IOException {
            packer.write(this.stringProperty);
        }
    }

    public static final String CHARS =
        "0123456789_-ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomString(Random r, int len) {
        int unit = len / 10;
        int finalLen = (int)(len + unit * r.nextGaussian());
        StringBuilder s = new StringBuilder(finalLen);
        for (int i = 0; i < finalLen; ++i) {
            s.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }
        return s.toString();
    }

    public static IPv4Table<Value> newTable(long seed, int count, int size) {
        Random r = new Random(seed);
        IPv4Table<Value> t = new IPv4Table<Value>();
        long unit = 0xFFFFFFFFL / count;
        long start = 0;
        for (int i = 0; i < count; ++i) {
            long end = start + unit - 1;
            Value value = new Value(randomString(r, size));
            t.add(start, end, value);
            start += unit;
        }
        return t;
    }

    public static List<IPv4Table<Value>> newTableList(int count) {
        ArrayList<IPv4Table<Value>> list =
            new ArrayList<IPv4Table<Value>>(count);
        for (int i = 0; i < count; ++i) {
            list.add(newTable(i, 1000000, 50));
        }
        return list;
    }

    public static long benchmarkQuery(
            IPv4TableBase<Value> t,
            long msec,
            long seed)
    {
        Random r = new Random(seed);
        long count = 0;
        long end = System.currentTimeMillis() + msec;
        while (System.currentTimeMillis() < end) {
            Value v = t.find(r.nextInt());
            ++count;
        }
        return count;
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Benchmark1 executing:");
        System.out.println();

        Watch w = new Watch("create 5 new IPv4Tables");
        List<IPv4Table<Value>> list = newTableList(5);
        w.stop();

        {
            System.out.print("benchmark query IPv4Table in 10 sec:");
            long count = benchmarkQuery(list.get(0), 10000, 0);
            System.out.println(String.format(" %1$.2f/sec (total %2$d)",
                        count / 10.0, count));
        }

        w = new Watch("convert a IPv4Table to IPv4Table2");
        IPv4Table2<Value> t2 = new IPv4Table2<Value>(list.get(0),
                new MessagePackableConverter<Value>(Value.class));
        w.stop();

        {
            System.out.print("benchmark query IPv4Table2 in 10 sec:");
            long count = benchmarkQuery(t2, 10000, 0);
            System.out.println(String.format(" %1$.2f/sec (total %2$d)",
                        count / 10.0, count));
        }
    }
}
