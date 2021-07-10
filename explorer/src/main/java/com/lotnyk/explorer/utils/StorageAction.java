package com.lotnyk.explorer.utils;

import com.lotnyk.explorer.model.Node;

import java.io.File;
import java.util.List;

public interface Action {

    void add(File file);

    void delete(File file);

    void uploadFile(File file);

    List<Node> findFile(String text);
}
