package com.lihua.model;

import com.lihua.model.validation.MaxPageSizeLimit;
import jakarta.validation.constraints.Max;
import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import lombok.Data;

@Data
public class BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 最大分页页码
     */
    public static final int MAX_PAGE_NUM = 999999999;
    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;
    /**
     * 默认分页页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页码
     */
    @Max(value = MAX_PAGE_NUM, message = "当前分页参数超出限制", groups = {MaxPageSizeLimit.class})
    protected Integer pageNum;

    /**
     * 当前分页大小
     */
    @Max(value = MAX_PAGE_SIZE, message = "当前分页参数超出限制", groups = {MaxPageSizeLimit.class})
    protected Integer pageSize;

    //在构造方法中设置默认值
    public BaseDTO() {
        this.pageNum = Optional.ofNullable(pageNum).orElse(BaseDTO.DEFAULT_PAGE_NUM);
        this.pageSize = Optional.ofNullable(pageSize).orElse(BaseDTO.DEFAULT_PAGE_SIZE);
    }
}
