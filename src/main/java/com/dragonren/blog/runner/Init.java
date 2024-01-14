package com.dragonren.blog.runner;

import java.io.File;
import java.io.IOException;

public class Init {
    public Init() throws IOException {
        // 检测文件是否存在
        String[] folderList = {"res","hot"};
        for(String folderName:folderList){
            File file = new File(folderName);
            if(!file.exists()){
                if(!file.mkdirs()){
                    throw new IOException("Init Failed!:"+folderName+" not exist and create failed!");
                }
            }
        }
    }
}
