package com.zsf.container.result;

public enum ResultCode {
	FAIL_FILE(500100, "文件上传失败"),
	FAIL_PATH(500101, "找不到路径"),
	FAIL_DELETE(500102, "图片不存在"),
	FAIL_COUNT(500102, "上传图片达到上限"),
	SUCCESS_FILE(200100, "文件上传成功"),

	SUCCESS(200, "成功"),
	FAIL(500, "失败");

	private Integer code;
	
	private String message;
	
	ResultCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
