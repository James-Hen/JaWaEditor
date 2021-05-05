package org.xjtujavacourse.common;

import java.io.*;
import java.util.Base64;

public class Base64Serializer {
    public static byte[] convertToBytes(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encode(baos.toByteArray());
    }

    public static Object convertFromBytes(byte[] s) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(s));
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}
