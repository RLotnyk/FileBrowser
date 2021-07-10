package com.lotnyk.explorer.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DataFileSystem {

    public boolean create(String path) {
        return new File(path).mkdir();
    }

    public List<Path> delete(File file) throws IOException {
        List<Path> paths = Files.walk(file.toPath()).collect(Collectors.toList());
        if (file.isFile()) {
            file.delete();
        } else {
            FileUtils.cleanDirectory(file);
            FileUtils.forceDelete(file);
        }
        return paths;
    }

    public List<File> findFile(String path, String text) throws IOException {
        return Files.walk(Paths.get(path)).map(Path::toFile).filter(
                file -> file.getName().equalsIgnoreCase(text)
        ).collect(Collectors.toList());
    }
}
