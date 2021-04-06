package io.capsule.filemanager.service.metadata;

import io.capsule.filemanager.domain.MetaData;
import io.capsule.filemanager.service.storage.FileHandlerService;
import io.capsule.filemanager.service.storage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileMetaDataService implements MetaDataService {

	@Resource(name = "fileHandlerService")
	FileStorageService fileStorageService;

	Map<String, MetaData> metaDataStore = new HashMap<>();

	Logger logger = LoggerFactory.getLogger(FileHandlerService.class);

	@PostConstruct
	private void init() {
		logger.debug("FileMetaDataService: init()");
		this.populateMetaData();
	}

	@Override
	public MetaData get(String fileName) {
		logger.debug("FileMetaDataService: get()");
		return metaDataStore.get(fileName);
	}

	@Override
	public List<MetaData> getAll() {
		logger.debug("FileMetaDataService: getAll()");
		return new ArrayList<MetaData>(metaDataStore.values());
	}

	@Override
	public void store(MetaData metaData) {
		logger.debug("FileMetaDataService: store()");
		metaDataStore.put(metaData.getFileName(), metaData);
	}

	private void populateMetaData() {
		logger.debug("FileMetaDataService: populateMetaData()");
		try {
			for (Path path : fileStorageService.getFiles()) {
				BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
				this.store(new MetaData(path.getFileName().toString(),
						millsToLocalDateTimeString(attr.creationTime().toMillis())));
			}
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}

	public String millsToLocalDateTimeString(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date.toString();
	}
}
