package com.lihua.system.controller;

import com.lihua.annotation.Log;
import com.lihua.config.LihuaConfig;
import com.lihua.enums.LogTypeEnum;
import com.lihua.enums.ResultCodeEnum;
import com.lihua.exception.FileException;
import com.lihua.model.web.BaseController;
import com.lihua.model.web.EditorFileModel;
import com.lihua.model.web.EditorUrlFileModel;
import com.lihua.utils.file.FileUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("system/file")
@Slf4j
public class SysFileController extends BaseController {

    @Resource
    private LihuaConfig lihuaConfig;

    private final String EDITOR_FILE_PREFIX = "editor_";

    private final String[] EDITOR_FILE_TYPE_ARRAY = {".jpg",".jpeg",".gif",".png"};

    /**
     * 附件下载
     * 该接口无需springSecurity鉴权即可访问
     * 其他需要鉴权的接口请注意不要设置接口地址为/system/file/download/**
     * @param filePath 文件路径，可以是被 split 分割的多个文件（多文件将打包下载）
     * @return ResponseEntity 包含 StreamingResponseBody，用于文件下载
     */
    @GetMapping("download")
    @Log(description = "附件下载", type = LogTypeEnum.DOWNLOAD)
    public ResponseEntity<StreamingResponseBody> download(@RequestParam(name = "filePath") String filePath, @RequestParam(name = "split", defaultValue = ",") String split) {
        String[] filePathArray = filePath.split(split);

        // 单文件直接调用下载
        if (filePathArray.length == 1) {
            return success(new File(filePathArray[0]));
        }

        // 多文件创建文件集合
        List<File> files = new ArrayList<>();
        for (String path : filePathArray) {
            files.add(new File(path));
        }

        return success(files);
    }

    /**
     * 用于富文本编辑器的文件下载
     * @param filePath 文件路径
     */
    @GetMapping("download/editor")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam(name = "filePath") String filePath) {
        File file = new File(filePath);
        if (file.getName().startsWith(EDITOR_FILE_PREFIX)) {
            return success(file);
        }

        throw new FileException();
    }

    /**
     * 单文件上传
     */
    @PostMapping("upload")
    @Log(description = "单文件上传", type = LogTypeEnum.UPLOAD)
    public String upload(@RequestParam("file") MultipartFile file) {
        return success(FileUtils.upload(file, null));
    }

    /**
     * 多文件上传
     */
    @PostMapping("uploads")
    @Log(description = "多文件上传", type = LogTypeEnum.UPLOAD)
    public String uploads(@RequestParam("files") MultipartFile[] files) {
        return success(FileUtils.upload(files, null));
    }

    /**
     * 通过 url 上传富文本文件
     * @param editorUrlFileModel 接收返回url实体
     */
    @PostMapping("editor/uploadByUrl")
    public String editorUpload(@RequestBody EditorUrlFileModel editorUrlFileModel) {
        String url = editorUrlFileModel.getUrl();
        String filePath = FileUtils.upload(url, null);
        editorUrlFileModel.setOriginalURL(url);
        editorUrlFileModel.setUrl(filePath);
        return success(ResultCodeEnum.EDITOR_SUCCESS, editorUrlFileModel);
    }

    /**
     * 富文本上传文件
     */
    @PostMapping("editor/uploads")
    public String editorUpload(@RequestParam("files") MultipartFile[] files) {
        // 上传文件
        List<String> upload = FileUtils.upload(files, null);
        // 构建返回
        EditorFileModel editorFileModel = new EditorFileModel();
        Map<String, String> map = new HashMap<>();
        upload.forEach(filePath -> map.put(new File(filePath).getName(), filePath));
        editorFileModel.setErrFiles(Collections.emptyList());
        editorFileModel.setSuccMap(map);
        return success(ResultCodeEnum.EDITOR_SUCCESS, editorFileModel);
    }

    /**
     * 判断文件后缀名是否符合保存条件
     * @param filename 文件路径
     */
    private boolean endsWithEditorFileType(String filename) {
        for (String type : EDITOR_FILE_TYPE_ARRAY) {
            if (filename.toLowerCase().endsWith(type)) { // 忽略大小写进行比较
                return true;
            }
        }

        return false;
    }
}
