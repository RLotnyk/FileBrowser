package com.lotnyk.explorer.utils;

import com.lotnyk.explorer.model.Node;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageAction {

    boolean add(File file) throws Exception;

    void delete(File file) throws Exception;

    void uploadFile(File file);

    List<Node> findFile(String text) throws IOException;
}
