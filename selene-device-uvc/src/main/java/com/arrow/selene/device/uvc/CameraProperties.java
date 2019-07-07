package com.arrow.selene.device.uvc;

import com.arrow.selene.engine.DeviceProperties;

public class CameraProperties extends DeviceProperties {
    private static final long serialVersionUID = 1782789172276692883L;

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
    private static final int DEFAULT_FREQUENCY_IN_SECS = 10;
    private static final String DEFAULT_FORMAT = "jpg";
    private static final boolean DEFAULT_SAVE_LOCAL = false;
    private static final String DEFAULT_DIRECTORY = "/opt/selene/images";
    private static final boolean DEFAULT_DETECT_FACE = false;
    private static final boolean DEFAULT_MARK_DETECTED_FACE = false;
    private static final boolean DEFAULT_OVERLAY_ENABLED = true;
    private static final String DEFAULT_OVERLAY_URL = "http://iot.arrow.com";
    private static final boolean DEFAULT_IP_CAMERA = false;

    private static final Double DEFAULT_FACE_DETECTION_SCALE_FACTOR = 1.1;
    private static final int DEFAULT_FACE_DETECTION_MIN_NEIGHBORS = 3;
    private static final int DEFAULT_FACE_DETECTION_FLAGS = 0;
    private static final int DEFAULT_FACE_DETECTION_MIN_SIZE_X = 30;
    private static final int DEFAULT_FACE_DETECTION_MIN_SIZE_Y = 30;

    private int index = 0;
    private String streamUrl;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private String format = DEFAULT_FORMAT;
    private int frequencyInSecs = DEFAULT_FREQUENCY_IN_SECS;
    private String directory = DEFAULT_DIRECTORY;
    private boolean saveLocal = DEFAULT_SAVE_LOCAL;
    private boolean detectFace = DEFAULT_DETECT_FACE;
    private boolean markDetectedFace = DEFAULT_MARK_DETECTED_FACE;
    private boolean overlayEnabled = DEFAULT_OVERLAY_ENABLED;
    private String overlayUrl = DEFAULT_OVERLAY_URL;
    private boolean ipCamera = DEFAULT_IP_CAMERA;

    private String faceDetectionFile;
    private Double faceDetectionScaleFactor = DEFAULT_FACE_DETECTION_SCALE_FACTOR;
    private int faceDetectionMinNeighbors = DEFAULT_FACE_DETECTION_MIN_NEIGHBORS;
    private int faceDetectionFlags = DEFAULT_FACE_DETECTION_FLAGS;
    private int faceDetectionMinSizeX = DEFAULT_FACE_DETECTION_MIN_SIZE_X;
    private int faceDetectionMinSizeY = DEFAULT_FACE_DETECTION_MIN_SIZE_Y;

    public Double getFaceDetectionScaleFactor() {
        return faceDetectionScaleFactor;
    }

    public void setFaceDetectionScaleFactor(Double faceDetectionScaleFactor) {
        this.faceDetectionScaleFactor = faceDetectionScaleFactor;
    }

    public int getFaceDetectionMinNeighbors() {
        return faceDetectionMinNeighbors;
    }

    public void setFaceDetectionMinNeighbors(int faceDetectionMinNeighbors) {
        this.faceDetectionMinNeighbors = faceDetectionMinNeighbors;
    }

    public int getFaceDetectionFlags() {
        return faceDetectionFlags;
    }

    public void setFaceDetectionFlags(int faceDetectionFlags) {
        this.faceDetectionFlags = faceDetectionFlags;
    }

    public int getFaceDetectionMinSizeX() {
        return faceDetectionMinSizeX;
    }

    public void setFaceDetectionMinSizeX(int faceDetectionMinSizeX) {
        this.faceDetectionMinSizeX = faceDetectionMinSizeX;
    }

    public int getFaceDetectionMinSizeY() {
        return faceDetectionMinSizeY;
    }

    public void setFaceDetectionMinSizeY(int faceDetectionMinSizeY) {
        this.faceDetectionMinSizeY = faceDetectionMinSizeY;
    }

    public String getFaceDetectionFile() {
        return faceDetectionFile;
    }

    public void setFaceDetectionFile(String faceDetectionFile) {
        this.faceDetectionFile = faceDetectionFile;
    }

    public boolean isIpCamera() {
        return ipCamera;
    }

    public void setIpCamera(boolean ipCamera) {
        this.ipCamera = ipCamera;
    }

    public String getOverlayUrl() {
        return overlayUrl;
    }

    public void setOverlayUrl(String overlayUrl) {
        this.overlayUrl = overlayUrl;
    }

    public boolean isOverlayEnabled() {
        return overlayEnabled;
    }

    public void setOverlayEnabled(boolean overlayEnabled) {
        this.overlayEnabled = overlayEnabled;
    }

    public boolean isMarkDetectedFace() {
        return markDetectedFace;
    }

    public void setMarkDetectedFace(boolean markDetectedFace) {
        this.markDetectedFace = markDetectedFace;
    }

    public boolean isDetectFace() {
        return detectFace;
    }

    public void setDetectFace(boolean detectFace) {
        this.detectFace = detectFace;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getFrequencyInSecs() {
        return frequencyInSecs;
    }

    public void setFrequencyInSecs(int frequencyInSecs) {
        this.frequencyInSecs = frequencyInSecs;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isSaveLocal() {
        return saveLocal;
    }

    public void setSaveLocal(boolean saveLocal) {
        this.saveLocal = saveLocal;
    }
}
