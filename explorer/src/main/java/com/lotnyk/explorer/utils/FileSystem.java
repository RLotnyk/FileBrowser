package com.lotnyk.explorer.utils;

import com.lotnyk.explorer.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Component
public class FileSystem implements Action {

    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private int id = 0;

    @Value("${file.path}")
    private String root;


    public List<Node> init() {
        Path path = Paths.get(root);
        try {
            Stream<Path> stream = Files.walk(path);
            stream.forEach(
                    (Path p) -> {
                        if (p.toFile().isDirectory()) {
                            isDirectory(p);
                        } else {
                            isFile(p);
                        }
                    }
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nodes;
    }

    private Node create(String id, String name, String parent, String type) {
        return new Node(id, parent, name, type);
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

    @Override
    public void add(File file) {
        nodes.add(create(file.getPath(), file.getName(), file.getParent(), file.isDirectory() ? "folder" : "file"));
    }

    @Override
    public void delete(File file) {
        nodes.remove(new Node(file.getPath()));
    }
}


