package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yyq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sys_file")
@Table(name = "sys_file")
public class File implements Serializable {

    private static final long serialVersionUID = -7186434783800529756L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long fileId;

    private String fileName;

    @Column(length = 100000000)
    private String fileValue;

    private Integer fileType;

    private String comment;

    private Date createTime;


    public static class FileType {

        public static final int TEMPLATEPDF = 0;

        public static final int TEMPLATEIMAGE = 1;

        public static Map<Integer, String> fileTypeMap() {
            Map<Integer, String> map = new HashMap<>();

            map.put(TEMPLATEPDF, "PDF模版");
            map.put(TEMPLATEIMAGE, "模版图片");

            return map;
        }

    }
}
