package com.arrow.rhea.client.api;

import java.io.OutputStream;
import java.net.URI;

import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.FileStore;

@Component("RheaClientFileStoreApi")
public class ClientFileStoreApi extends ClientApiAbstract {

	private static final String FILE_STORES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/file-stores";
	private static final String FILE_STORE_URL = FILE_STORES_ROOT_URL + "/ids/%s";
	private static final String FILE_STORE_FILE_URL = FILE_STORE_URL + "/file";

	public FileStore findById(String id) {
		try {
			URI uri = buildUri(String.format(FILE_STORE_URL, id));
			FileStore result = execute(new HttpGet(uri), FileStore.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public long readFile(String id, OutputStream outputStream) {
		Assert.notNull(outputStream, "outputStream is null");
		try {
			URI uri = buildUri(String.format(FILE_STORE_FILE_URL, id));
			return execute(new HttpGet(uri), outputStream);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
