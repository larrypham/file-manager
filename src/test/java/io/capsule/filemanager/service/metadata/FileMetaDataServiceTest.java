package io.capsule.filemanager.service.metadata;

import io.capsule.filemanager.domain.MetaData;
import io.capsule.filemanager.service.storage.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FileMetaDataServiceTest {

	@Mock
	FileStorageService fileStorageServiceMock;

	@InjectMocks
	FileMetaDataService fileMetaDataService;

	@Test
	public void verify_store_metadata() {
		String fileName = "test1";
		MetaData metaData = new MetaData(fileName, "testTime");
		fileMetaDataService.store(metaData);

		assertEquals(metaData, fileMetaDataService.get(fileName));
	}
}