package org.xjtujavacourse.client;

import org.xjtujavacourse.common.JaWaDocument;

public abstract class Saver {
    String path;
    public Saver(String path) {
        this.path = path;
    }
    public abstract boolean save(JaWaDocument doc);
    public abstract JaWaDocument load(String name);
}
