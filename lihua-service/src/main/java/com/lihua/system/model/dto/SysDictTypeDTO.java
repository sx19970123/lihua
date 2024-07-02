package com.lihua.system.model.dto;

import com.lihua.model.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictTypeDTO extends BaseDTO {
    private String name;
    private String code;
    private String status;
    private List<LocalDateTime> startEndTime;
}
