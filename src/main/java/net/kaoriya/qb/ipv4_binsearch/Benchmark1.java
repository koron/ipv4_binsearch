package net.kaoriya.qb.ipv4_binsearch;

import java.io.IOException;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

public class Benchmark1
{

    static class Value implements MessagePackable {
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

    public static void main(String[] args)
    {
        System.out.println("Benchmark1");
    }
}
