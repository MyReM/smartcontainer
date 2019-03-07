package com.zsf.core.utils;

import com.zsf.core.config.web.BusinessException;

import java.io.*;

public class FileUtils {

    public static void copyInputStreamToFile(InputStream in, File file) throws Exception {

        try (OutputStream outputStream = new FileOutputStream(file);) {

            byte[] bytes = new byte[1024];

            int n = -1;

            while ((n = in.read(bytes, 0, bytes.length)) != -1) {
                outputStream.write(bytes, 0, n);
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("找不到该文件！");
        } catch (IOException e) {
            throw new IOException("文件读取失败！");
        }
    }
}
