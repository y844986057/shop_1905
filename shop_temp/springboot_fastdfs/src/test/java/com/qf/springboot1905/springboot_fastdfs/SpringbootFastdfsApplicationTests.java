package com.qf.springboot1905.springboot_fastdfs;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@Import(FdfsClientConfig.class)
@SpringBootTest
public class SpringbootFastdfsApplicationTests {

	@Autowired
	private FastFileStorageClient client;

	@Test
	public void contextLoads() throws FileNotFoundException {

		File file = new File("D:\\Documents\\DragonNest\\Screenshot\\20190805204320.png");

		FileInputStream ips = new FileInputStream(file);

		StorePath storePath = client.uploadImageAndCrtThumbImage(ips, file.length(), "png", null);
		System.out.println("文件的路径:"+storePath.getFullPath());
	}



	@Test
	public void  Solution() {
		int answer [] =new int[4];
		int[] A = {1,2,3,4};
		int[][]  queries = {{1,0},{-3,1},{-4,0},{2,3}};
		for(int i=0;i<A.length;i++){
			A[queries[i][1]]=A[queries[i][1]]+queries[i][0];
			for (int j=0;j<A.length;j++){
				if (A[j]%2==0){
					answer [i]+=A[j];
				}
			}
		}




	}
}
