package com.zhixue.demo.controller;

import com.alibaba.fastjson.JSON;
import com.zhixue.demo.constant.StatusCodeConsts;
import com.zhixue.demo.dto.ResultDTO;
import com.zhixue.demo.entity.FileDO;
import com.zhixue.demo.enumeration.ResultEnum;
import com.zhixue.demo.service.FileService;
import com.zhixue.demo.util.FileUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @program:
 * @description: [文件controller]
 * @author:
 * @create: 2019-10-12 17:04
 */
@RestController
@RequestMapping("/admin/file")
@Validated
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    /**
     * MultipartFile.getOriginalFilename() 带后缀的文件名称
     * MultipartFile.getContentType() 文件类型，不是后缀名
     * 如txt文件，contentType为text/plain
     */

    /**
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传文件", notes = "上传文件，上传成功后返回文件id")
    public ResultDTO<?> uploadFile(@ApiParam(value = "上传文件", name = "file", required = true) MultipartFile file,
                                   HttpServletRequest request) {

        if (null == file || file.isEmpty())
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件为空");

        if (null == file.getContentType() || null == file.getOriginalFilename())
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件名或者后缀为空");

        if (!FileUtils.isLegalFile(file))
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件格式非法");

        String originalFilename  = file.getOriginalFilename();

        // 文件名使用UUID存储
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 文件存储路径
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        LocalDateTime ldt = LocalDateTime.now();
        String datePath = ldt.format(dtf);

        String filePath = fileUploadPath + "//" + datePath;
        log.info("文件保存路径-->{}" , filePath);
        try {
            // 保存文件到本地
            FileUtils.saveFile(file.getBytes(), filePath, fileName);
            // 保存记录到数据库
            FileDO fileDO = new FileDO();

            // 文件存储名称 UUID.后缀
            fileDO.setStorageName(fileName);
            // 文件原始名称（带后缀）
            fileDO.setShowName(file.getOriginalFilename());
            fileDO.setUrl(datePath);
            fileDO.setCreateTime(System.currentTimeMillis());
            fileDO.setUpdateTime(System.currentTimeMillis());

            fileService.insertOne(fileDO);

            return ResultDTO.of(ResultEnum.SUCCESS, fileDO.getId());
        } catch (Exception e) {
        	log.error("文件上传异常，{}",e.getMessage());
            return ResultDTO.of(StatusCodeConsts.ERROR, "文件上传异常");
        }
    }

    @PostMapping(value = "/uploadFiles", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传单个或者多个文件", notes = "上传文件，上传成功后返回文件id数组")
    public ResultDTO<?> uploadFiles(
            @ApiParam(value = "上传文件", name = "fileList", required = true) List<MultipartFile> fileList,
            HttpServletRequest request) {

        if (null == fileList || fileList.isEmpty())
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件为空");

        for (MultipartFile file : fileList) {
            if (null == file.getContentType() || null == file.getOriginalFilename())
                return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件名或者后缀为空");
            if (!FileUtils.isLegalFile(file)) {
                return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件格式非法");
            }
        }
        // 不能使用contentType过滤上传文件
        // 因为有些合法的文件类型与非法文件类型的MIME类型是一样的
        // 文件存储路径
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        LocalDateTime ldt = LocalDateTime.now();
        String datePath = ldt.format(dtf);

        String filePath = fileUploadPath + "//" + datePath;

        // 设置文件存储名称和初始化文件对象数组
        List<FileDO> fileEntityList = new LinkedList<>();
        for (MultipartFile file : fileList) {
        	String originalFilename = file.getOriginalFilename();
            // 文件名使用UUID存储
            String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 保存记录到数据库
            FileDO fileDO = new FileDO();
            // 文件存储名称 UUID.后缀
            fileDO.setStorageName(fileName);
            // 文件原始名称（带后缀）
            fileDO.setShowName(file.getOriginalFilename());
            fileDO.setUrl(datePath);
            fileDO.setCreateTime(System.currentTimeMillis());
            fileDO.setUpdateTime(System.currentTimeMillis());

            fileEntityList.add(fileDO);
        }

        try {
            int i = 0;
            List<Long> iList = new LinkedList<>();
            for (MultipartFile file : fileList) {
                // 保存文件到本第地
                FileUtils.saveFile(file.getBytes(), filePath, fileEntityList.get(i).getStorageName());
                // 保存文件记录到数据库
                fileService.insertOne(fileEntityList.get(i));
                iList.add(fileEntityList.get(i++).getId());
            }
            return ResultDTO.of(ResultEnum.SUCCESS, iList.isEmpty() ? null : JSON.toJSONString(iList));

        } catch (Exception e) {
        	log.error("文件上传异常，{}",e.getMessage());
            return ResultDTO.of(StatusCodeConsts.ERROR, "文件上传异常");
        }
    }

    /**
     *
     * @param file
     * @param request
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "/uploadReturnPath", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传文件", notes = "上传文件，上传成功后返回文件访问绝对路径")
    public ResultDTO uploadFileReturnPath(@ApiParam(value = "上传文件", name = "file", required = true) MultipartFile file,
                                          HttpServletRequest request) {


        if (null == file || file.isEmpty())
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件为空");

        if (null == file.getContentType() || null == file.getOriginalFilename())
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件名或者后缀为空");

        if (!FileUtils.isLegalFile(file))
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, "文件格式非法");

        // 不能使用contentType过滤上传文件
        // 因为有些合法的文件类型与非法文件类型的MIME类型是一样的

        // 文件名使用UUID存储
        String originalFilename = file.getOriginalFilename();
        // 文件名使用UUID存储
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 文件存储路径
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        LocalDateTime ldt = LocalDateTime.now();
        String datePath = ldt.format(dtf);
        String filePath = fileUploadPath + "//" + datePath;
        try {
            // 保存文件到本第地
            FileUtils.saveFile(file.getBytes(), filePath, fileName);

            FileDO fileDO = new FileDO();

            // 文件存储名称 UUID.后缀
            fileDO.setStorageName(fileName);
            // 文件原始名称（带后缀）
            fileDO.setShowName(file.getOriginalFilename());
            fileDO.setUrl(datePath);
            fileDO.setCreateTime(System.currentTimeMillis());
            fileDO.setUpdateTime(System.currentTimeMillis());

            fileService.insertOne(fileDO);

            return ResultDTO.of(StatusCodeConsts.SUCCESS, "/" + datePath + fileDO.getStorageName());
        } catch (Exception e) {
        	log.error("文件上传异常，{}",e.getMessage());
            return ResultDTO.of(StatusCodeConsts.ERROR, "文件上传异常");
        }
    }


    @ApiOperation(value = "下载文件", notes = "")
    @GetMapping(value = "/download", produces = { "multipart/form-data;charset=UTF-8" })
    public void download(@ApiParam(value = "文件id", name = "id", required = true,example = "1") @RequestParam Long id,
                         @ApiParam(value = "时间戳", name = "timestamp", required = true,example = "1") @RequestParam Long timestamp,
                         HttpServletRequest req, HttpServletResponse res) {
        // 判断入参是否合法
        if (null == id || null == timestamp)
            return;
        FileDO file = fileService.getByIdAndUploadTime(id, timestamp);
        if (null != file) {
            String fileName = file.getShowName();
            OutputStream os = null;
            InputStream inputStream = null;
            try {
                String userAgent = req.getHeader("user-agent").toLowerCase();
                if (userAgent != null) {
                	// IE浏览器
                	if (userAgent.contains("MSIE"))
                		fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
                        // google,火狐浏览器
                    else if (userAgent.contains("firefox"))
                        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                        // 其他浏览器
                    else
                        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
				}

                res.setCharacterEncoding("UTF-8");
                res.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                res.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                inputStream = new FileInputStream(new File(fileUploadPath + file.getUrl() + file.getStorageName()));
                os = res.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
            }catch (IOException e) {
            	log.error("文件下载异常，{}",e.getMessage());
            }finally {
            	IOUtils.closeQuietly(os);
            	IOUtils.closeQuietly(inputStream);
			}
        }
    }
}
