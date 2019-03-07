package com.zsf.core.utils;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileLoadUtil {

    /**
     * @param file
     * @param path
     * @return
     */
    public boolean uploadFile(MultipartFile file, String path) {
        File file1 = new File(path + file.getOriginalFilename());
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdir();
        }
        try {
            //上传
            file.transferTo(file1);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重载
     *
     * @param file
     * @param path
     * @param newFileName
     */
    public static boolean uploadFile(MultipartFile file, String path, String newFileName) {
        String sufName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File file1 = new File(path + newFileName + sufName);
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdir();
        }
        try {
            //上传
            file.transferTo(file1);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param files
     * @param path
     */
    public boolean uploadFiles(MultipartFile[] files, String path) {
        if (!ObjectUtils.isEmpty(files)) {
            Arrays.stream(files).forEach(file -> {
                uploadFile(file, path);
            });
            return true;
        }
        return false;
    }

    /**
     * @param files
     * @param path
     * @param prefixName
     */
    public boolean uploadFiles(MultipartFile[] files, String path, String prefixName) {
        if (!ObjectUtils.isEmpty(files)) {
            Arrays.stream(files).forEach(file -> {
                File file1 = new File(path + prefixName + file.getOriginalFilename());
                if (!file1.getParentFile().exists()) {
                    file1.getParentFile().mkdir();
                }
                try {
                    //上传
                    file.transferTo(file1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        return false;
    }

    /**
     * @param filePath
     * @param fileName
     */
    public boolean delete(String filePath, String fileName){

        File file = new File(filePath + fileName);
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
}
