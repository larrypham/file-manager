package io.capsule.filemanager.controller;

import io.capsule.filemanager.domain.MetaData;
import io.capsule.filemanager.service.metadata.MetaDataService;
import io.capsule.filemanager.service.storage.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {
	@Mock
	StorageService storageServiceMock;

	@Mock
	MetaDataService metaDataServiceMock;

	@Mock
	Resource testFileMock;

	@Mock
	MultipartFile multipartFileMock;

	@InjectMocks
	FileController fileController;

	@Test
	public void getFileNames() {
		when(storageServiceMock.getFileNames()).thenReturn(Collections.singletonList("Test"));
		ResponseEntity<List<String>> entity = fileController.getFileNames();
		verify(storageServiceMock).getFileNames();
		assertEquals("Test", Objects.requireNonNull(entity.getBody()).get(0));
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test(expected = RuntimeException.class)
	public void getFileNames_throwsException() {
		when(storageServiceMock.getFileNames()).thenThrow(new RuntimeException("Error in " +
				"retrieving files"));
		fileController.getFileNames();
	}

	@Test
	public void getFile() {
		String fileName = "test";
		when(storageServiceMock.getFile(fileName)).thenReturn(testFileMock);

		ResponseEntity<Resource> response = fileController.getFile(fileName);

		verify(storageServiceMock).getFile(fileName);
		assertEquals(testFileMock, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
	}

	@Test
	public void storeFile() {
		ResponseEntity<String> responseEntity = fileController.storeFile(multipartFileMock);
		verify(storageServiceMock).store(multipartFileMock);
		verify(metaDataServiceMock).store(any(MetaData.class));
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("File is created", responseEntity.getBody());
	}
}