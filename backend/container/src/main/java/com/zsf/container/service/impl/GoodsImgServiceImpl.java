package com.zsf.container.service.impl;
import com.zsf.container.dao.GoodsImgDAO;
import com.zsf.container.entity.GoodsImg;
import com.zsf.container.result.Result;
import com.zsf.container.result.ResultCode;
import com.zsf.container.service.GoodsImgService;
import com.zsf.core.config.web.BusinessException;
import com.zsf.core.service.impl.BaseServiceImpl;
import com.zsf.core.utils.FileLoadUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GoodsImgServiceImpl extends BaseServiceImpl<GoodsImg,Long> implements GoodsImgService{


    private GoodsImgDAO goodsImgDAO;

    @Autowired
    public void setGoodsImgDAO(GoodsImgDAO goodsImgDAO) {
        this.goodsImgDAO = goodsImgDAO;
        setBaseDAO(goodsImgDAO);
    }

    //存图片
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result<Object> saveImg(MultipartFile[] files, Integer commoditiesType){

        // 获取当前项目路径
        String path = System.getProperty("user.dir");
        // 截取当前项目的上一级目录（返回到前端和后台的根目录）
        path = path.substring(0,path.lastIndexOf("\\")+1);
        path.replace("\\","/");
        // 配置图片存放的前段项目地址
        String uploadPath = path + "frontend/static/imgs/";

        //String uploadPath = request.getParameter("uploadPath");
//        String uploadPath = "F:/ZSF/SmartContainer/smartcontainer/frontend/static/imgs/";

        //储存图片
        if (0 != files.length ) {
            for (MultipartFile file: files) { //获取上传的图片数组
                String sf = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()); //创建String时间
                String imgName = file.getOriginalFilename(); //获取图片名字
                String sufName = imgName.substring(imgName.lastIndexOf(".")); //截取.后的格式
                String fileName=imgName.substring(0,imgName.lastIndexOf("."));//获得名字前面部分
                String uploadFileName = sf + "_"+ fileName;  //重命名
                String src = uploadPath+uploadFileName+sufName;

                try {
                    if (sufName.equals(".png") || sufName.equals(".jpg") || sufName.equals(".jpeg") ||sufName.equals(".gif") ||sufName.equals(".bmp")) { //判断格式是否正确

                        FileLoadUtil.uploadFile(file, uploadPath,uploadFileName); //调用util方法执行上传
                        GoodsImg img = GoodsImg.builder().createTime(new Date()).uploadFileName(uploadFileName).sufName(sufName).src(src).commoditiesType(commoditiesType).build(); // 数据写入
                        goodsImgDAO.save(img); //保存

                        // 多出四张后默认删除最早上传的图片
                        GoodsImg goodsImg = new GoodsImg();
                        goodsImg.setCommoditiesType(commoditiesType);
                        Example<GoodsImg> example = Example.of(goodsImg);
                        List<GoodsImg> list = findAll(example);
                        if (list.size() > 4) {
                            goodsImg = list.get(0);
                            deleteById(goodsImg.getId());
                            File file1 = new File(goodsImg.getSrc());
                            if(file1.exists()) {
                                file1.delete();
                            }
                        }
                    } else  {
                        if (uploadPath!=null) {
                            throw new BusinessException("路径出错");
                        }
                        throw  new BusinessException("图片类型不支持！"); //如果判断的不是上面的3种类型，则提示，抛异常
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return Result.failure(ResultCode.FAIL_PATH);
                }
            }
            return Result.success(ResultCode.SUCCESS_FILE);
        }
        return Result.failure(ResultCode.FAIL_FILE);

    }

    //取图片
    @Override
    public Result<Object> getImg(HttpServletRequest request) {
        GoodsImg goodsImg = new GoodsImg();
        String x = request.getParameter("commoditiesType");
        Integer commoditiesType = Integer.valueOf(request.getParameter("commoditiesType"));
        goodsImg.setCommoditiesType(commoditiesType);
        if (commoditiesType == null || commoditiesType.equals("") || commoditiesType==0){
            return Result.failure();
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("commoditiesType", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<GoodsImg> example = Example.of(goodsImg,matcher);
        List<GoodsImg> list = goodsImgDAO.findAll(example);
        return Result.success(list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}) //删除不成功，回滚
    public Result<Object> deleteImg(HttpServletRequest request){
        String src = request.getParameter("src");
        Long id = Long.valueOf(request.getParameter("id"));
        File file = new File(src);
        goodsImgDAO.deleteById(id);
        if(file.exists()){
            file.delete();
            return Result.success("true");
        }
        return Result.failure(ResultCode.FAIL_DELETE,"false");
    }

    @Override
    public void deleteImgBySrc(String src, Long id){
        deleteById(id);
        File file = new File(src);
        if(file.exists()) {
            file.delete();
        }
    }
}
