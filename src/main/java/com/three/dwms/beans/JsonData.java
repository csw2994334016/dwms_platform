package com.three.dwms.beans;

import com.three.dwms.constant.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JsonData {

    private String code;

    private String msg;

    private Object data;

    public JsonData(ResultCode resultCode) {
        this.code = resultCode.getCode();
    }

    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(ResultCode.SUCCESS);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(ResultCode.SUCCESS);
        jsonData.msg = ResultCode.SUCCESS.getDesc();
        jsonData.data = object;
        return jsonData;
    }

    public static JsonData success() {
        JsonData jsonData = new JsonData(ResultCode.SUCCESS);
        jsonData.msg = ResultCode.SUCCESS.getDesc();
        return jsonData;
    }

    public static JsonData success(String msg) {
        JsonData jsonData = new JsonData(ResultCode.SUCCESS);
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData fail() {
        JsonData jsonData = new JsonData(ResultCode.FAIL);
        jsonData.msg = ResultCode.FAIL.getDesc();
        return jsonData;
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(ResultCode.FAIL);
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData noLogin() {
        JsonData jsonData = new JsonData(ResultCode.NO_LOGIN);
        jsonData.msg = ResultCode.NO_LOGIN.getDesc();
        return jsonData;
    }

    public static JsonData noLogin(String msg) {
        JsonData jsonData = new JsonData(ResultCode.NO_LOGIN);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
