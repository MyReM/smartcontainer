package com.zsf.core.controller;
import com.zsf.core.service.FileService;
import com.zsf.core.utils.FileLoadUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author yyq
 */
@Controller
@RequestMapping(value = "/api/file")
@PropertySource(value = {"classpath:system.properties"})
public class FileController {

    private final String filePath = "/www/wwwroot/labelPrint/upload/image/";
    private final String templatePath = "/www/wwwroot/labelPrint/upload/pdf";
    private FileLoadUtil fileLoadUtil = new FileLoadUtil();

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/{fileId}")
    @ResponseBody
    public com.zsf.core.entity.File findOne(@NotNull(message = "ID不能为空！") @PathVariable(value = "fileId") Long fileId) {

        com.zsf.core.entity.File file = fileService.findOne(fileId);

        return file;
    }

    @PostMapping(value = "/forService/{fileId}")
    @ResponseBody
    public String forServiceFindOne(@NotNull(message = "ID不能为空！") @PathVariable(value = "fileId") Long fileId) {

        com.zsf.core.entity.File file = fileService.findOne(fileId);

        return file.getFileValue();

    }

    @RequestMapping(value = "/multiUpload")
    public void multiUpload(MultipartFile[] files) {
        //判断file数组不能为空并且长度大于0
        if (!ObjectUtils.isEmpty(files)) {

            //循环获取file数组中得文件
            Arrays.stream(files).forEach(file -> {

                String fileName = file.getOriginalFilename();
                String sufName = fileName.substring(fileName.lastIndexOf("."));
                if (sufName.equals(".pdf")) {
                    fileLoadUtil.uploadFile(file, templatePath);
                } else {
                    fileLoadUtil.uploadFile(file, filePath);
                }
            });
        }
    }

    @RequestMapping(value = "/upload")
    public String upload(MultipartFile file) {

        String fileUrl = null;
        String sufName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //判断file数组不能为空并且长度大于0
        if(sufName.equals(".pdf")){
            fileLoadUtil.uploadFile(file,templatePath);
            fileUrl = templatePath + file.getOriginalFilename();
            return fileUrl;
        }else{
            fileLoadUtil.uploadFile(file,filePath);
            fileUrl = filePath + file.getOriginalFilename();
            return fileUrl;
        }
    }

    /**
     * @param
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/download")
    public ResponseEntity<byte[]> download(String fileName) throws IOException {

        fileName = filePath + fileName;


        File file = null;

        String sufName = fileName.substring(fileName.lastIndexOf("."));
        if (sufName.equals(".pdf")) {
            file = new File(fileName);
        } else {
            file = new File(fileName);
        }

        HttpHeaders headers = new HttpHeaders();

        HttpStatus statusCode=HttpStatus.OK;

        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(null , headers, statusCode);
    }

}
