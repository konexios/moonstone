package moonstone.selene.web.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;

import moonstone.acs.JsonUtils;
import moonstone.selene.SeleneException;
import moonstone.selene.model.StatusModel;
import moonstone.selene.web.api.data.OsModel;

@RestController
@RequestMapping("/api/selene/log")
public class LogApi extends BaseApi {

	public String readingGzipCompressedFile(String filePath) {
		String logs = "";

		try {
			FileInputStream fileStream = new FileInputStream(filePath);
			GZIPInputStream gzipStream = new GZIPInputStream(fileStream);
			Reader reader = new InputStreamReader(gzipStream);
			BufferedReader bufferReader = new BufferedReader(reader);

			try {
				String line;
				while ((line = bufferReader.readLine()) != null)
					logs += line + '\n';
			} finally {
				fileStream.close();
				gzipStream.close();
				reader.close();
				bufferReader.close();
			}

			return logs;
		} catch (IOException e) {
			throw new SeleneException("Error in reading gzip file", e);
		}
	}

	public String readingZipCompressedFile(String filePath) {
		String logs = "";

		try {
			FileInputStream fileStream = new FileInputStream(filePath);
			ZipInputStream zipStream = new ZipInputStream(fileStream);
			zipStream.getNextEntry();
			Reader reader = new InputStreamReader(zipStream);
			BufferedReader bufferReader = new BufferedReader(reader);

			try {
				String line;
				while ((line = bufferReader.readLine()) != null)
					logs += line + '\n';
			} finally {
				fileStream.close();
				zipStream.close();
				reader.close();
				bufferReader.close();
			}

			return logs;
		} catch (IOException e) {
			throw new SeleneException("Error in reading gzip file", e);
		}
	}

	private String getLogFileAttribute(String attributeName) {

		try {
			String log4jPath = getConfigService().getWebProperties().getEngineLog4jPath();

			if (StringUtils.isEmpty(log4jPath)) {
				log4jPath = OsModel.getConfigPath();
			}

			File fXmlFile = new File(log4jPath, "log4j2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Node node = doc.getElementsByTagName("RollingFile").item(0);
			Element eElement = (Element) node;

			return eElement.getAttribute(attributeName);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new SeleneException("ERROR in getting log file", e);
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public StatusModel getLogFile(@RequestBody String data) {
		String method = "getLogFile";

		Assert.notNull(data, "Data is null");
		JsonNode payload = JsonUtils.fromJson(data, JsonNode.class);
		String fileName = payload.get("logfilename").asText();
		Assert.notNull(fileName, "log file name is null");
		String filePattern = getLogFileAttribute("filePattern");
		Assert.notNull(filePattern, "log pattern is null");
		String logPath = getLogFileAttribute("fileName");

		File file = new File(logPath);
		if (!new File(logPath).isAbsolute()) {
			file = new File(getConfigService().getWebProperties().getEngineJarPath(), logPath);
		}

		logPath = file.getParent();

		String logs = "";
		try {
			fileName += ".log";
			if (Objects.equals(file.getName(), fileName)) {
				logInfo(method, "Reading %s log file", fileName);
				logs = new String(Files.readAllBytes(file.toPath()));
			} else {
				Date fileDate = new SimpleDateFormat("MM-dd-yyyy")
						.parse(fileName.substring(fileName.indexOf('-') + 1, fileName.lastIndexOf('-')));
				String formattedDate = new SimpleDateFormat("yyyy-MM").format(fileDate);

				if (filePattern.endsWith(".zip")) {
					logInfo(method, "Reading %s log file", fileName);
					logs = readingZipCompressedFile(Paths.get(logPath, formattedDate, fileName + ".zip").toString());
				} else if (filePattern.endsWith(".gz")) {
					logInfo(method, "Reading %s log file", fileName);
					logs = readingGzipCompressedFile(Paths.get(logPath, formattedDate, fileName + ".gz").toString());
				}
			}
			return new StatusModel().withStatus("OK").withMessage(logs);
		} catch (IOException | ParseException e) {
			throw new SeleneException("ERROR in reading log file", e);
		}
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ResponseEntity<Object> downloadLogFile(@RequestBody String data) {
		String method = "downloadLogFile";

		JsonNode payload = JsonUtils.fromJson(data, JsonNode.class);

		Assert.notNull(data, "Data is null");
		String fileName = payload.get("logfilename").asText();
		Assert.notNull(fileName, "log file name is null");
		String filePattern = getLogFileAttribute("filePattern");
		Assert.notNull(filePattern, "log pattern is null");
		String logPath = getLogFileAttribute("fileName");

		File logFile = new File(logPath);
		if (!new File(logPath).isAbsolute()) {
			logFile = new File(getConfigService().getWebProperties().getEngineJarPath(), logPath);
		}

		logPath = logFile.getParent();

		try {
			File file;
			fileName += ".log";
			InputStreamResource resource = null;

			if (Objects.equals(logFile.getName(), fileName)) {
				logInfo(method, "Downloading %s log file", fileName);
				file = new File(logPath, fileName);
				resource = new InputStreamResource(new FileInputStream(file));
			} else {
				Date fileDate = new SimpleDateFormat("MM-dd-yyyy")
						.parse(fileName.substring(fileName.indexOf('-') + 1, fileName.lastIndexOf('-')));
				String formattedDate = new SimpleDateFormat("yyyy-MM").format(fileDate);
				String filePath = "";

				if (filePattern.endsWith(".zip")) {
					filePath = Paths.get(logPath, formattedDate, fileName + ".zip").toString();
					logInfo(method, "Downloading %s log file", fileName);
					ZipInputStream zipStream = new ZipInputStream(new FileInputStream(filePath));
					zipStream.getNextEntry();
					resource = new InputStreamResource(zipStream);
				} else if (filePattern.endsWith(".gz")) {
					filePath = Paths.get(logPath, formattedDate, fileName + ".gz").toString();
					logInfo(method, "Downloading %s log file", fileName);
					GZIPInputStream gzipStream = new GZIPInputStream(new FileInputStream(filePath));
					resource = new InputStreamResource(gzipStream);
				}
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", fileName);
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/log"))
					.body(resource);
		} catch (ParseException | IOException e) {
			throw new SeleneException("ERROR downloading log file", e);
		}
	}

	@RequestMapping(value = "/{date}/getall", method = RequestMethod.GET)
	public List<String> getLogFiles(@PathVariable String date) {
		String method = "getLogFiles";

		Assert.notNull(date, "Date is null");
		List<String> logFileList = new ArrayList<>();
		String filePattern = getLogFileAttribute("filePattern");
		Assert.notNull(filePattern, "log pattern is null");
		String extension = ".log" + filePattern.substring(filePattern.lastIndexOf('.'));
		String logPath = getLogFileAttribute("fileName");
		File currentLogFile = new File(logPath);
		if (!currentLogFile.isAbsolute()) {
			currentLogFile = new File(getConfigService().getWebProperties().getEngineJarPath(), logPath);
		}

		try {
			SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM");
			Date currentDate = new Date();
			logInfo(method, "Searching log files for %s month", date);

			if (date.equals(stf.format(currentDate))) {
				logFileList.add(currentLogFile.getName().substring(0, currentLogFile.getName().indexOf('.')));
			}

			File dateLogPath = new File(currentLogFile.getParent(), date);
			if (dateLogPath.exists()) {
				File[] listOfFiles = dateLogPath.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(extension);
					}
				});

				for (File file : listOfFiles) {
					if (file.isFile()) {
						logFileList.add(file.getName().substring(0, file.getName().indexOf('.')));
					}
				}

			}
			logInfo(method, "found %d log files for %s month", logFileList.size(), date);

			return logFileList;
		} catch (

		SeleneException e) {
			throw new SeleneException("Error!!!", e);
		}
	}
}