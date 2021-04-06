package io.capsule.filemanager.controller;

import io.capsule.filemanager.domain.MetaData;
import io.capsule.filemanager.service.metadata.MetaDataService;
import io.capsule.filemanager.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileController {
	@Resource(name = "fileHandlerService")
	private StorageService storageService;

	@Resource
	private MetaDataService metaDataService;

	Logger logger = LoggerFactory.getLogger(FileController.class);

	@GetMapping()
	public ResponseEntity<List<String>> getFileNames() {
		logger.debug("FileController: getFileNames()");
		return ResponseEntity.ok().body(storageService.getFileNames());
	}

	@GetMapping("/{filename:.+}")
	public ResponseEntity<org.springframework.core.io.Resource> getFile(@PathVariable String filename) {
		logger.debug("FileController: getFile() with file Name " + filename);
		org.springframework.core.io.Resource file = storageService.getFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + "filename=" + file.getFilename())
				.body(file);
	}

	@PostMapping
	public ResponseEntity<String> storeFile(@RequestParam("file") MultipartFile file) {
		logger.debug("FileController: storeFile() with file name " + file.getName());
		storageService.store(file);
		storeMetaData(file);
		return new ResponseEntity<String>("File is created.", HttpStatus.CREATED);
	}

	private void storeMetaData(MultipartFile file) {
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		metaDataService.store(new MetaData(fileName, LocalDateTime.now().toString()));
	}
}
