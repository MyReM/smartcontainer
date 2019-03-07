package com.zsf.core.config.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yyq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer code;

    private String message;

    private Object data;

    public static class Code {

        private Code() {}

        public static final Integer SUCCESS = 200;

        public static final Integer NOTFOUND = 404;

        public static final Integer UNAUTH = 302;

        public static final Integer ERROR = 500;

    }

}
