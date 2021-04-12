package org.xjtujavacourse.client;

import org.xjtujavacourse.common.JaWaDocument;

public class RemoteSaver extends Saver {
    public RemoteSaver(String path) {
        super(path);
    }

    @Override
    public boolean save(JaWaDocument doc) {
        return false;
    }

    @Override
    public JaWaDocument load(String name) {
        return null;
    }
}
