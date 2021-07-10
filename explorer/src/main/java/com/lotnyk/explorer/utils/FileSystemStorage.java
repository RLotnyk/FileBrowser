package com.lotnyk.explorer.utils;

import com.lotnyk.explorer.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


@Component
public class FileSystem implements Action {

    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private int id = 0;

    @Value("${file.path}")
    private String root;


    public List<Node> init() throws IOException {
        Files.walk(Paths.get(root)).forEach(
                (Path p) -> {
                    if (p.toFile().isDirectory()) {
                        isDirectory(p);
                    } else {
                        isFile(p);
                    }
                }
        );
        return nodes;
    }

    private Node create(String id, String name, String parent, String type) {
        Node node = new Node();
        node.setId(id);
        node.setParent(parent);
        node.setText(name);
        node.setType(type);
        return node;
    }

    private void isDirectory(Path path) {
        if (id == 0) {
            nodes.add(create(path.toFile().getPath(), path.toFile().getName(), "#", "folder"));
        } else {
            nodes.add(create(path.toFile().getPath(), path.toFile().getName(), path.toFile().getParent(), "folder"));
        }

        id++;
    }

    private void isFile(Path path) {
        nodes.add(create(path.toFile().getPath(), path.toFile().getName(), path.toFile().getParent(), "file"));
    }

    private Node checkDuplicateNode(File file) {
        Node node = new Node();
        node.setId(file.getPath());
        return nodes.stream().filter(n->n.equals(node)).findAny().orElse(null);
    }

    @Override
    public void add(File file) {
        nodes.add(create(file.getPath(), file.getName(), file.getParent(), file.isDirectory() ? "folder" : "file"));
    }

    @Override
    public void delete(File file) {
        Node node = new Node();
        node.setId(file.getPath());
        nodes.remove(node);
    }

    @Override
    public void uploadFile(File file) {
        Node node = this.checkDuplicateNode(file);
        if (node == null) {
            add(file);
        }
        return;
    }

    @Override
    public List<Node> findFile(String text) {
        return nodes.stream().filter(n->n.getText().equals(text)).collect(Collectors.toList());
    }
}


