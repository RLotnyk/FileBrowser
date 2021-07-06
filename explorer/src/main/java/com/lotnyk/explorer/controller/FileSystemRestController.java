package com.lotnyk.explorer.controller;

import com.lotnyk.explorer.model.Node;
import com.lotnyk.explorer.utils.FileSystem;
import com.lotnyk.explorer.utils.FileSystemAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
public class FileSystemRestController {

    private final FileSystem system;
    private final FileSystemAction action;
    private final List<Node> list;

    @Autowired
    public FileSystemRestController(final FileSystem system, final FileSystemAction action) {
        this.system = system;
        this.action = action;
        this.list = this.system.init();
    }

    @GetMapping(value = "/tree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Node> tree() {
        return list;
    }

    @PostMapping(value = "/create")
    public void create(@RequestParam String id, @RequestParam String name) {
        action.create(id, name);
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestParam String id) throws IOException {
        action.delete(new File(id));
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("path") String path, @RequestParam("file")MultipartFile file) throws IOException {
        File uploadFile = new File(path + File.separator + file.getOriginalFilename());
        System.out.println(uploadFile.getName());
        file.transferTo(uploadFile);
        action.uploadFile(uploadFile);
    }
}
