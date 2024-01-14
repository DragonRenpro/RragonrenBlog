package com.dragonren.blog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import java.util.Collections;
import java.lang.Integer;


@SpringBootApplication
public class BlogAppApplication {

	public static void main(String[] args) {
		int port = 80;
		//SetPort 设置端口
		for(int index = 0; index<args.length;index++){
			if(args[index].equals("-port")){
				if(index+1 != args.length)
					port = Integer.parseInt(args[index+1]);
			}
		}
		SpringApplication app = new SpringApplication(BlogAppApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port",port));
		app.run(args);
	}

}
