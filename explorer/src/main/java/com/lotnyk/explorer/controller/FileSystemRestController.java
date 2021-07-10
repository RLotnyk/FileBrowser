package com.lotnyk.explorer.controller;

import com.lotnyk.explorer.model.Node;
import com.lotnyk.explorer.utils.FileSystemStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class FileSystemRestController {

    private final FileSystemStorage storage;
    private final List<Node> list;

    @Autowired
    public FileSystemRestController(FileSystemStorage storage) throws IOException {
        this.storage = storage;
        this.list = this.storage.createTree();
    }

    @GetMapping(value = "/tree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Node> tree() throws IOException {
        return list;
    }

    @PostMapping(value = "/create")
    public void create(@RequestBody Node node) throws Exception {
        File file = new File(String.format("%s%s%s", node.getId(), File.separator, node.getText()));
        System.out.println(file);
        if (storage.add(file)) {
            log.debug(String.format("Created File: %s", file.getName()));
        }
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestBody Node node) throws Exception {
        File file = new File(node.getId());
        storage.delete(file);
        log.info(String.format("Deleted File: %s", file));
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file) throws Exception {
        File uploadFile = new File(path + File.separator + file.getOriginalFilename());
        file.transferTo(uploadFile);
        System.out.println(uploadFile);
        storage.uploadFile(uploadFile);
    }

    @GetMapping("/search")
    public List<Node> findNode(@RequestParam("str") String text) throws IOException {
        return storage.findFile(text);
    }

    @ExceptionHandler(Exception.class)
    public String handle(IllegalArgumentException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
