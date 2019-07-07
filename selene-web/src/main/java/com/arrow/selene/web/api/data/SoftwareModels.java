package com.arrow.selene.web.api.data;

import java.io.Serializable;

public class SoftwareModels {

	public static class UploadFromUrl implements Serializable {

		private static final long serialVersionUID = 9208716818530808856L;
		private String uploadPath;
		private String url;
		private String proxy;
		private int port;

		public UploadFromUrl() {
		}

		public UploadFromUrl(String uploadPath, String url, String proxy, int port) {
			this.uploadPath = uploadPath;
			this.url = url;
			this.proxy = proxy;
			this.port = port;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getProxy() {
			return proxy;
		}

		public void setProxy(String proxy) {
			this.proxy = proxy;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getUploadPath() {
			return uploadPath;
		}

		public void setUploadPath(String uploadPath) {
			this.uploadPath = uploadPath;
		}

	}

	public static class UploadFile implements Serializable {
		private static final long serialVersionUID = -8331442952463157907L;

		private String uploadPath;
		private String inputStream;

		public UploadFile() {
		}

		public UploadFile(String uploadPath, String inputStream) {
			this.uploadPath = uploadPath;
			this.inputStream = inputStream;
		}

		public String getUploadPath() {
			return uploadPath;
		}

		public void setUploadPath(String uploadPath) {
			this.uploadPath = uploadPath;
		}

		public String getInputStream() {
			return inputStream;
		}

		public void setInputStream(String inputStream) {
			this.inputStream = inputStream;
		}

	}

	public static class DownloadFile implements Serializable {

		private static final long serialVersionUID = -4470865177974091905L;

		private String downloadPath;

		public String getDownloadPath() {
			return downloadPath;
		}

		public void setDownloadPath(String downloadPath) {
			this.downloadPath = downloadPath;
		}
	}

}
