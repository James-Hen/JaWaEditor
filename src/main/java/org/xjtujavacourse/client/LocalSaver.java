package org.xjtujavacourse.client;

import org.xjtujavacourse.common.JaWaDocument;

public class LocalSaver extends Saver {
    public LocalSaver(String path) {
        super(path);
    }

    @Override
    public boolean save(JaWaDocument doc) {
        // TODO: save doc.content as path+doc.name+.html
        return false;
    }

    @Override
    public JaWaDocument load(String name) {
        // TODO: load path+doc.name+.html as return value
        return null;
    }

    public static void main(String[] args) {

    }
}
