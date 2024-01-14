package com.dragonren.blog.controller;

import com.dragonren.blog.runner.SensorConnection;
import com.sun.tools.javac.Main;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Arrays;


@Controller
public class MainController {
    public SensorConnection sensorConnection;
    public MainController(SensorConnection sensorConnection) throws IOException {
        this.sensorConnection = sensorConnection;
    }

    // 起始页
    // index page
    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("name","dragonRen");
        return "index";
    }
    // 登录网页
    // user login
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    // 下载内容
    // download context
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) {
        // 构建文件路径
        String filePath = "./res/" + fileName;
        File targetFile = new File(filePath);

        // 检查文件是否存在
        if (!targetFile.exists()) {
            response.setStatus(404); // 文件不存在，返回404错误
            return;
        }
        // 获取文件长度
        long fileLength = targetFile.length();
        long start = 0, end = fileLength - 1;
        // 获取请求头中的Range信息
        String rangeHeader = request.getHeader("Range");
        if (rangeHeader != null) {
            // 解析Range信息
            String[] rangeValues = rangeHeader.replace("bytes=", "").split("-");
            start = Long.parseLong(rangeValues[0]);

            if (rangeValues.length == 2) {
                end = Long.parseLong(rangeValues[1]);
            }

            if (start >= fileLength || start > end) {
                response.setStatus(416); // 请求范围无效，返回416错误
                return;
            }

            // 部分响应头设置
            response.setStatus(206);
            response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        } else {
            // 全部响应
            response.addHeader("Content-Length", String.valueOf(fileLength));
        }

        // 设置响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");

        try (FileInputStream fis = new FileInputStream(targetFile); OutputStream os = response.getOutputStream()) {
            // 跳过开始位置
            long skip = fis.skip(start);
            byte[] buffer = new byte[1024];
            int bytesRead;

            // 读取文件内容并写入响应输出流
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            response.setStatus(500); // 服务器内部错误，返回500错误
        }
    }
    // 一些热更替的页面
    @GetMapping("/hot/{requestsPath}")
    @ResponseBody
    public String heatmap(@PathVariable("requestsPath") String requestsPath){
        // 构建文件路径
        return this.getFileString("./hot/".concat(requestsPath));
    }
    @GetMapping("/BingSiteAuth.xml")
    @ResponseBody
    public String verBing(){
        return this.getFileString("./hot/BingSiteAuth.xml");
    }
    protected String getFileString(String path){
        // 构建文件路径
        File file = new File(path);
        // 检测文件是否存在
        if (!file.exists() || !file.canRead()) {
            return "404";
        }
        try(FileInputStream layoutInput = new FileInputStream(file)){
            return new String(layoutInput.readAllBytes());
        }catch(IOException e){
            return "404";
        }
    }
    protected byte[] getFileBytes(String path) throws IOException {
        // 构建文件路径
        File file = new File(path);
        // 检测文件是否存在
        if (!file.exists() || !file.canRead()) {
            throw new IOException();
        }
        try(FileInputStream layoutInput = new FileInputStream(file)){
            return layoutInput.readAllBytes();
        }catch(IOException e){
            throw new IOException();
        }
    }
    @GetMapping("/sensor")
    @ResponseBody
    public String sensor(){
        return this.sensorConnection.getSensorData().toString();
    }

}
