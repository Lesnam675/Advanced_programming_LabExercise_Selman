package com.aastu.notepadfx.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {

    public static String readFile(File file)
            throws IOException {

        return Files.readString(file.toPath());
    }

    public static void writeFile(
            File file,
            String content
    ) throws IOException {

        Files.writeString(
                file.toPath(),
                content
        );
    }
}
