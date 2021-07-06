package com.lotnyk.explorer.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class FileSystemAction {

    private final FileSystem system;

    @Autowired
    public FileSystemAction(final FileSystem system) {
        this.system = system;
    }

    public void create(String path, String name) {
        File file = new File(path + File.separator + name);
        if (!file.exists()) {
            file.mkdir();
            system.add(file);
        }
    }

    public void delete(File file) throws IOException {

        Stream<Path> stream = Files.walk(file.toPath());
        stream.forEach(
                (Path path) -> {
                    system.delete(path.toFile());
                }
        );
        if (file.isFile()) {
            file.delete();
        } else {
            FileUtils.cleanDirectory(file);
            FileUtils.forceDelete(file);
        }
    }

    public void uploadFile(File file) {
        system.add(file);
    }
}
