package com.zsf.core.utils;


import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LibLoader {
    public static void loadLib(String libName) {

        String directory = libName.substring(0, libName.lastIndexOf("/"));

        String folderName = System.getProperty("java.io.tmpdir");

        File folder = new File(folderName);

        folder.mkdirs();

        File libFile = new File(folder, libName);

        if (libFile.exists()) {
            System.load(libFile.getAbsolutePath());
        } else {
            try (InputStream in = LibLoader.class.getResourceAsStream(libName);) {

                FileUtils.copyInputStreamToFile(in, libFile);

                System.load(libFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load required lib", e);
            }
        }
    }
}
