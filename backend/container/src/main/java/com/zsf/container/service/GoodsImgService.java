package com.zsf.container.service;
import com.zsf.container.entity.GoodsImg;
import com.zsf.container.result.Result;
import com.zsf.core.config.web.BusinessException;
import com.zsf.core.service.BaseService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GoodsImgService extends BaseService<GoodsImg,Long> {

    Result<Object> saveImg(MultipartFile[] files, Integer commoditiesType) throws IOException, BusinessException;

    Result<Object> getImg(HttpServletRequest request) throws IOException;

    Result<Object> deleteImg(HttpServletRequest request) throws IOException;

    void deleteImgBySrc(String src, Long id);
}
