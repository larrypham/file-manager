package io.capsule.filemanager.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
	void store(MultipartFile file);
	Resource getFile(String fileName);
	List<String> getFileNames();
}
