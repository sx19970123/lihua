package com.lihua.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * xxl-job 执行测试，可删除此类
 */
@Component
public class jobTest {
    @XxlJob("jobTest")
    public void jobTest() {
        String jobParam = XxlJobHelper.getJobParam();
        System.out.println("xxl-job 执行测试" + jobParam);
    }
}
