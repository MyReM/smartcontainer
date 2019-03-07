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
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_log")
@Table(name = "sys_log")
public class Log implements Serializable {

    private static final long serialVersionUID = -8739394340355036326L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long logId;

    private String message;

    private Integer type;

    private Integer level;

    private Date createTime;

    public static class LogType {

        private LogType() {}

        public static final Integer LOGIN = 0;

        public static final Integer BUSINESS = 1;

        public static final Integer SCAN = 2;

        public static Map<Integer, String> getLogType(){
            Map<Integer, String> map = new HashMap<>();
            map.put(LOGIN, "登录");
            map.put(BUSINESS, "业务");
            map.put(SCAN, "扫描");
            return map;
        }

    }

    public static class LogLevel {

        private LogLevel() {}

        public static final Integer INFO = 0;

        public static final Integer DEBUG = 1;

        public static final Integer WARN = 2;

        public static final Integer ERROR = 3;


        public static Map<Integer, String> getLogLevel(){
            Map<Integer, String> map = new HashMap<>();
            map.put(INFO, "成功");
            map.put(DEBUG, "调试");
            map.put(WARN, "警告");
            map.put(ERROR, "错误");
            return map;
        }

    }
}
