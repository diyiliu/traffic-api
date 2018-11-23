package com.tiza.traffic.support;

import lombok.Data;

/**
 * Description: RespBody
 * Author: DIYILIU
 * Update: 2018-11-09 09:57
 */

@Data
public class RespBody<T> {

    private Boolean success;

    private String message;

    private T data;
}
