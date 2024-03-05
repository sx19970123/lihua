package com.lihua.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer pageNum;

    private Integer pageSize;
}
