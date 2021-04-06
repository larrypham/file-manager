package io.capsule.filemanager.service.storage;

import io.capsule.filemanager.service.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class S3StorageService implements StorageService {
	@Override
	public void store(MultipartFile file) {

	}

	@Override
	public Resource getFile(String fileName) {
		return null;
	}

	@Override
	public List<String> getFileNames() {
		return null;
	}
}
