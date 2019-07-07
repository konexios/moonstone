package com.arrow.rhea.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.AcsSystemException;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.repo.params.FileStoreSearchParams;

@RestController(value = "privateRheaFileStoreApi")
@RequestMapping("/api/v1/private/rhea/file-stores")
public class FileStoreApi extends BaseApiAbstract {

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public FileStore findById(@PathVariable(name = "id", required = true) String id) {
		FileStoreSearchParams params = new FileStoreSearchParams();
		params.addIds(id);
		List<FileStore> fileStoreList = getFileStoreService().findBy(params);
		if (fileStoreList != null && !fileStoreList.isEmpty()) {
			return fileStoreList.get(0);
		} else {
			return null;
		}
	}

	@RequestMapping(path = "/ids/{id}/file", method = RequestMethod.GET)
	public void readFile(@PathVariable(name = "id", required = true) String id, HttpServletResponse response) {
		String method = "readFile";
		FileStoreSearchParams params = new FileStoreSearchParams();
		params.addIds(id);
		List<FileStore> fileStoreList = getFileStoreService().findBy(params);
		Assert.isTrue(fileStoreList != null && !fileStoreList.isEmpty(), "fileStore is not found");
		FileStore fileStore = fileStoreList.get(0);
		logInfo(method, "found fileStore name: %s", fileStore.getName());
		MediaType contentType = StringUtils.isNotBlank(fileStore.getContentType())
				? MediaType.valueOf(fileStore.getContentType())
				: MediaType.APPLICATION_OCTET_STREAM;
		response.setContentType(contentType.toString());
		if (StringUtils.isNotBlank(fileStore.getName())) {
			response.setHeader("Content-Disposition", "attachment;filename=" + fileStore.getName().trim());
		}
		try {
			byte[] data = getFileStoreService().readFile(fileStore);
			logInfo(method, "writing to response, data size: %d ...", data == null ? 0 : data.length);
			StreamUtils.copy(data, response.getOutputStream());
		} catch (Exception e) {
			throw new AcsSystemException("error reading file", e);
		}
	}
}
