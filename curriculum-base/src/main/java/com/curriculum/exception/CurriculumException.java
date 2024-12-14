package com.curriculum.exception;

/**
 * @description 本项目自定义异常类型
 */
public class CurriculumException extends RuntimeException {

    private String errMessage;

    public CurriculumException() {
    }

    public CurriculumException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message){
        throw new CurriculumException(message);
    }
    public static void cast(CurriculumException error){
        throw new CurriculumException(error.getErrMessage());
    }

}
