package io.capsule.filemanager.service.metadata;

import io.capsule.filemanager.domain.MetaData;

import java.util.List;

public interface MetaDataService {
	MetaData get(String fileName);
	List<MetaData> getAll();
	void store(MetaData metaData);
}
