package com.arrow.selene.web.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.selene.SeleneException;
import com.arrow.selene.web.api.data.SoftwareModels;

@RestController
@RequestMapping(value = "/api/selene/software")
public class SoftwareApi extends BaseApi {

	private void saveInputStreamToFile(InputStream input, File file) {

		String method = "saveInputStreamToFile";

		try {
			// File where to be downloaded

			if (file.exists()) {
				if (file.isDirectory())
					throw new IOException(String.format("File '%s' is a directory", file.getAbsolutePath()));

				if (!file.canWrite())
					throw new IOException(String.format("File '%s' cannot be written", file.getAbsolutePath()));
			} else {
				File parent = file.getParentFile();
				if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
					throw new IOException(String.format("File '%s' could not be created", file.getAbsolutePath()));
				}
			}

			FileUtils.copyInputStreamToFile(input, file);

			input.close();

			logInfo(method, "File '%s' save file successfully!", file.getAbsolutePath());
		} catch (IOException ioEx) {
			throw new SeleneException("Error in save file", ioEx);
		}
	}

	@RequestMapping(value = "/uploadFileFromUrl", method = RequestMethod.POST)
	public void uploadFileFromUrl(@RequestBody(required = false) SoftwareModels.UploadFromUrl data) {
		Assert.notNull(data.getUrl(), "url is null");
		Assert.notNull(data.getUploadPath(), "uploadPath is null");
		try {
			URL url = new URL(data.getUrl());

			URLConnection conn;
			if (StringUtils.isNotBlank(data.getProxy())) {
				SocketAddress addr = new InetSocketAddress(data.getProxy(), data.getPort());
				Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				conn = url.openConnection(proxy);
			} else {
				conn = url.openConnection();
			}

			InputStream input = conn.getInputStream();

			File file = new File(data.getUploadPath(), new File(url.getPath()).getName());

			saveInputStreamToFile(input, file);

		} catch (Exception e) {
			throw new SeleneException("Error in download file from url", e);
		}
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestBody SoftwareModels.UploadFile data) {
		Assert.notNull(data.getUploadPath(), "uploadPath is null");

		InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data.getInputStream()));

		File file = new File(data.getUploadPath());
		saveInputStreamToFile(inputStream, file);

	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public ResponseEntity<Object> downloadFromPath(@RequestBody SoftwareModels.DownloadFile data) {
		Assert.notNull(data.getDownloadPath(), "downloadPath is null");

		String encodedString = null;
		try {
			File file = new File(data.getDownloadPath());
			if (file.exists()) {
				if (file.isDirectory()) {
					throw new IOException(String.format("Path '%s' is a directory", data.getDownloadPath()));
				}
			} else {
				throw new IOException(String.format("File '%s' exists", data.getDownloadPath()));
			}
			Base64.Encoder encoder = Base64.getEncoder();
			encodedString = encoder.encodeToString(Files.readAllBytes(file.toPath()));

		} catch (IOException ioEx) {
			throw new SeleneException("Error in save file", ioEx);
		}

		return ResponseEntity.ok().body(encodedString);
	}
}