package com.lotnyk.explorer.utils;

import com.lotnyk.explorer.model.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileSystemStorage implements StorageAction {

    private final List<Node> nodes = new CopyOnWriteArrayList<>();
    private final DataFileSystem data;
    private int id = 0;

    @Value("${file.path}")
    private String root;

    public List<Node> createTree() throws IOException {
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

    private Node checkDuplicateNode(File file) {
        return nodes.stream().filter(n->n.equals(new Node(file.getPath()))).findAny().orElse(null);
    }

    @Override
    public boolean add(File file) throws Exception {
        if (data.create(file.getPath())) {
            return nodes.add(create(file.getPath(), file.getName(), file.getParent(), "folder"));
        }
        throw new Exception("File Not Created!");
    }

    @Override
    public void delete(File file) throws Exception {
        List<Path> paths = data.delete(file);
        paths.stream().forEach(path->nodes.remove(new Node(path.toFile().getPath())));
    }

    @Override
    public void uploadFile(File file) {
        Node node = this.checkDuplicateNode(file);
        if (node == null) {
            nodes.add(create(file.getPath(), file.getName(), file.getParent(), "file"));
        }
    }

    @Override
    public List<Node> findFile(String text) throws IOException {
        if (data.findFile(root, text).size() > 0) {
            return nodes.stream().filter(n -> n.getText().equalsIgnoreCase(text)).collect(Collectors.toList());
        } else {
            throw new IOException("Not Found!");
        }
    }
}
