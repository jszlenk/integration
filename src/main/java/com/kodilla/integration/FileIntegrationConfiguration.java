package com.kodilla.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.core.GenericHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class FileIntegrationConfiguration {

    private static final String OUTPUT_FILE_PATH = "data/output/processed_files.txt";

    @Bean
    FileReadingMessageSource fileAdapter() {
        FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setDirectory(new File("data/input"));
        return fileSource;
    }

    @Bean
    GenericHandler<File> fileProcessor() {
        return (file, headers) -> {
            try (FileWriter writer = new FileWriter(OUTPUT_FILE_PATH, true)) {
                writer.write(file.getName() + System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException("Error writing to output file", e);
            }
            return null;
        };
    }

    @Bean
    IntegrationFlow fileIntegrationFlow(FileReadingMessageSource fileAdapter, GenericHandler<File> fileProcessor) {
        return IntegrationFlow.from(fileAdapter, config -> config.poller(Pollers.fixedDelay(1000)))
                .handle(fileProcessor)
                .get();
    }
}