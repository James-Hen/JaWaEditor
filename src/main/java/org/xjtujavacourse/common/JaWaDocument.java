package org.xjtujavacourse.common;

import java.io.Serializable;

public class JaWaDocument implements Serializable {
    public String name;
    public String content;
    public String prevVersion;

    public String versionHash() {
        return "" + ("" + name + "|" + content + "|" + prevVersion).hashCode();
    }
}
