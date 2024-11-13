package com.kodilla.integration;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final String INPUT_DIRECTORY = "data/input";
    private static final String OUTPUT_FILE = "data/output/processed_files.txt";

    @PostMapping("/add")
    public String addFile(@RequestParam String fileName, @RequestParam String content) {
        File file = new File(INPUT_DIRECTORY + "/" + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            return "File created: " + file.getName();
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    @GetMapping("/output")
    public List<String> readOutputFile() {
        try {
            return Files.readAllLines(Paths.get(OUTPUT_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Error reading output file", e);
        }
    }
}
