package io.capsule.filemanager.service.storage;

import io.capsule.filemanager.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileHandlerService implements FileStorageService {

	@Value("${storage.location.folder.name}")
	private String location;

	Logger logger = LoggerFactory.getLogger(FileHandlerService.class);

	@Override
	public List<Path> getFiles() {
		logger.debug("FileHandlerService: getFiles()");
		List<Path> files = new ArrayList<>();
		try {
			Files.walk(Paths.get(location)).filter(Files::isRegularFile).forEach(files::add);
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		return files;
	}

	@Override
	public void store(MultipartFile file) {
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		logger.debug("FileHandlerService: store() " + fileName);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, Paths.get(String.format("%s/%s", location, fileName)),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException("Failed to store file " + fileName, ex);
		}
	}

	@Override
	public Resource getFile(String fileName) {
		logger.debug("FileHandlerService: getFileNames()");
		Path file = Paths.get(location, fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				logger.error("FileHandlerService: File is Not Available");
				throw new ResourceNotFoundException("File is not available or permission denied.");
			}
		} catch (MalformedURLException ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex.getMessage());
		}
	}

	@Override
	public List<String> getFileNames() {
		logger.debug("FileHandlerService: getFileNames()");
		List<String> fileNames = new ArrayList<>();
		try {
			getFiles().forEach(path -> fileNames.add(path.getFileName().toString()));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		return fileNames;
	}
}
