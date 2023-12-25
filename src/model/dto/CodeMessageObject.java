package model.dto;

import java.util.Objects;

public class CodeMessageObject<T> {
    private Integer code;
    private String message;
    private T t;

    public CodeMessageObject() {
    }

    public CodeMessageObject(Integer code, String message, T t) {
        this.code = code;
        this.message = message;
        this.t = t;
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

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeMessageObject<?> that = (CodeMessageObject<?>) o;
        return Objects.equals(code, that.code) && Objects.equals(message, that.message) && Objects.equals(t, that.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, t);
    }

    @Override
    public String toString() {
        return "CodeMessageObject{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", object=" + t +
                '}';
    }
}
