package com.ecommernce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileServiceImpl implements FileService {

	
	@Override
	public String uploadFile(String path, MultipartFile file) throws IOException {
		String originalFile=file.getOriginalFilename();
		
		String randomUII=UUID.randomUUID().toString();
		
		String fileName=randomUII.concat(originalFile.substring(originalFile.lastIndexOf('.')));
		
		String filePath=path+File.separator+fileName;
		
		File folder=new File(path);
		if(!folder.exists()) {
			folder.mkdir();
		}
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		return fileName;
	}

}
