package com.arrow.selene.device.uvc;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class UvcProperties extends DeviceProperties {
    private static final long serialVersionUID = 989445125378024003L;

    public static final int DEFAULT_FRAME_RATE = 30;
    public static final int DEFAULT_FRAME_WIDTH = 640;
    public static final int DEFAULT_FRAME_HEIGHT = 480;
    public static final boolean DEFAULT_LIVE_STREAM = true;
    public static final int DEFAULT_LIVE_STREAM_WIDTH = 320;
    public static final int DEFAULT_LIVE_STREAM_HEIGHT = 240;
    public static final boolean DEFAULT_SAVE_TO_FILE = true;
    public static final boolean DEFAULT_SAVE_AS_STREAM = false;
    public static final String DEFAULT_FOURCC = "H264";
    public static final String DEFAULT_DIRECTORY = "/opt/videos";
    public static final String DEFAULT_FILE_NAME_PATTERN = "%s.mp4";
    public static final String DEFAULT_STREAM_NAME_PATTERN = "%s.stream";
    public static final long DEFAULT_VIDEO_LEN_MINS = 2;
    public static final int DEFAULT_SEND_FRAME_COUNTER = 2;
    public static final int DEFAULT_RETRY_INTERVAL_SECS = 5;
    public static final int DEFAULT_DETECT_DISCONNECT_MS = 500;
    public static final int MAX_FRAME_COUNTER = -1;

    private int index;
    private int maxFrameCounter;

    // video file
    private boolean saveToFile = DEFAULT_SAVE_TO_FILE;
    private int frameRate = DEFAULT_FRAME_RATE;
    private int frameWidth = DEFAULT_FRAME_WIDTH;
    private int frameHeight = DEFAULT_FRAME_HEIGHT;
    private String fourcc = DEFAULT_FOURCC;
    private String directory = DEFAULT_DIRECTORY;
    private String fileNamePattern = DEFAULT_FILE_NAME_PATTERN;

    private boolean saveAsStream = DEFAULT_SAVE_AS_STREAM;
    private String streamNamePattern = DEFAULT_STREAM_NAME_PATTERN;

    private long videoLenMins = DEFAULT_VIDEO_LEN_MINS;

    // live stream
    private boolean liveStream = DEFAULT_LIVE_STREAM;
    private int liveFrameWidth = DEFAULT_LIVE_STREAM_WIDTH;
    private int liveFrameHeight = DEFAULT_LIVE_STREAM_HEIGHT;
    private int sendFrameCounter = DEFAULT_SEND_FRAME_COUNTER;

    private int retryIntervalSecs = DEFAULT_RETRY_INTERVAL_SECS;
    private int detectDisconnectMs = DEFAULT_DETECT_DISCONNECT_MS;

    @Override
    public UvcProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        index = Integer.parseInt(map.getOrDefault("index", Integer.toString(index)));
        maxFrameCounter = Integer.parseInt(map.getOrDefault("maxFrameCounter", Integer.toString(maxFrameCounter)));
        saveToFile = Boolean.parseBoolean(map.getOrDefault("saveToFile", Boolean.toString(saveToFile)));
        frameRate = Integer.parseInt(map.getOrDefault("frameRate", Integer.toString(frameRate)));
        frameWidth = Integer.parseInt(map.getOrDefault("frameWidth", Integer.toString(frameWidth)));
        frameHeight = Integer.parseInt(map.getOrDefault("frameHeight", Integer.toString(frameHeight)));
        fourcc = map.getOrDefault("fourcc", fourcc);
        directory = map.getOrDefault("directory", directory);
        fileNamePattern = map.getOrDefault("fileNamePattern", fileNamePattern);
        saveAsStream = Boolean.parseBoolean(map.getOrDefault("saveAsStream", Boolean.toString(saveAsStream)));
        streamNamePattern = map.getOrDefault("streamNamePattern", streamNamePattern);
        videoLenMins = Long.parseLong(map.getOrDefault("videoLenMins", Long.toString(videoLenMins)));
        liveStream = Boolean.parseBoolean(map.getOrDefault("liveStream", Boolean.toString(liveStream)));
        liveFrameWidth = Integer.parseInt(map.getOrDefault("liveFrameWidth", Integer.toString(liveFrameWidth)));
        liveFrameHeight = Integer.parseInt(map.getOrDefault("liveFrameHeight", Integer.toString(liveFrameHeight)));
        sendFrameCounter = Integer.parseInt(map.getOrDefault("sendFrameCounter", Integer.toString(sendFrameCounter)));
        retryIntervalSecs = Integer
                .parseInt(map.getOrDefault("retryIntervalSecs", Integer.toString(retryIntervalSecs)));
        detectDisconnectMs = Integer
                .parseInt(map.getOrDefault("detectDisconnectMs", Integer.toString(detectDisconnectMs)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("index", Integer.toString(index));
        map.put("maxFrameCounter", Integer.toString(maxFrameCounter));
        map.put("saveToFile", Boolean.toString(saveToFile));
        map.put("frameRate", Integer.toString(frameRate));
        map.put("frameWidth", Integer.toString(frameWidth));
        map.put("frameHeight", Integer.toString(frameHeight));
        if (fourcc != null)
            map.put("fourcc", fourcc);
        if (directory != null)
            map.put("directory", directory);
        if (fileNamePattern != null)
            map.put("fileNamePattern", fileNamePattern);
        map.put("saveAsStream", Boolean.toString(saveAsStream));
        if (streamNamePattern != null)
            map.put("streamNamePattern", streamNamePattern);
        map.put("videoLenMins", Long.toString(videoLenMins));
        map.put("liveStream", Boolean.toString(liveStream));
        map.put("liveFrameWidth", Integer.toString(liveFrameWidth));
        map.put("liveFrameHeight", Integer.toString(liveFrameHeight));
        map.put("sendFrameCounter", Integer.toString(sendFrameCounter));
        map.put("retryIntervalSecs", Integer.toString(retryIntervalSecs));
        map.put("detectDisconnectMs", Integer.toString(detectDisconnectMs));
        return map;
    }

    public String getStreamNamePattern() {
        return streamNamePattern;
    }

    public void setStreamNamePattern(String streamNamePattern) {
        this.streamNamePattern = streamNamePattern;
    }

    public boolean isSaveAsStream() {
        return saveAsStream;
    }

    public void setSaveAsStream(boolean saveAsStream) {
        this.saveAsStream = saveAsStream;
    }

    public int getMaxFrameCounter() {
        return maxFrameCounter;
    }

    public void setMaxFrameCounter(int maxFrameCounter) {
        this.maxFrameCounter = maxFrameCounter;
    }

    public int getLiveFrameHeight() {
        return liveFrameHeight;
    }

    public void setLiveFrameHeight(int liveFrameHeight) {
        this.liveFrameHeight = liveFrameHeight;
    }

    public int getLiveFrameWidth() {
        return liveFrameWidth;
    }

    public void setLiveFrameWidth(int liveFrameWidth) {
        this.liveFrameWidth = liveFrameWidth;
    }

    public boolean isLiveStream() {
        return liveStream;
    }

    public void setLiveStream(boolean liveStream) {
        this.liveStream = liveStream;
    }

    public boolean isSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(boolean saveToFile) {
        this.saveToFile = saveToFile;
    }

    public int getDetectDisconnectMs() {
        return detectDisconnectMs;
    }

    public void setDetectDisconnectMs(int detectDisconnectMs) {
        this.detectDisconnectMs = detectDisconnectMs;
    }

    public int getRetryIntervalSecs() {
        return retryIntervalSecs;
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        this.retryIntervalSecs = retryIntervalSecs;
    }

    public int getSendFrameCounter() {
        return sendFrameCounter;
    }

    public void setSendFrameCounter(int sendFrameCounter) {
        this.sendFrameCounter = sendFrameCounter;
    }

    public long getVideoLenMins() {
        return videoLenMins;
    }

    public void setVideoLenMins(long videoLenMins) {
        this.videoLenMins = videoLenMins;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getFourcc() {
        return fourcc;
    }

    public void setFourcc(String fourcc) {
        this.fourcc = fourcc;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }
}
