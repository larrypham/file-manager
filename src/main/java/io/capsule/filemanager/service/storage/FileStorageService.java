package io.capsule.filemanager.service.storage;

import java.nio.file.Path;
import java.util.List;

public interface FileStorageService extends StorageService {
	List<Path> getFiles();
}
