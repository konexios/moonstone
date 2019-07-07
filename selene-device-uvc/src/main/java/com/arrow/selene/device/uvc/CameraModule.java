package com.arrow.selene.device.uvc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.Utils;
import com.arrow.selene.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class CameraModule extends DeviceModuleAbstract<CameraInfo, CameraProperties, CameraStates, CameraData> {

    private static final String DATE_TIME_FORMATTER = "yyyyMMddHHmmss";
    private static final String FACE_DETECTION_LIBRARY = "/opencv/haarcascade_frontalface_default.xml";
    private static TypeReference<Map<String, String>> mapTypeRef;

    private VideoCapture camera;
    private Timer timer;
    private long currentFrequencyInSecs;
    private CascadeClassifier faceDetection;

    @Override
    protected void startDevice() {
        String method = "sendCommand";
        super.startDevice();

        // load native library
        logInfo(method, "loading opencv library ...");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String file = getProperties().getFaceDetectionFile();
        if (StringUtils.isEmpty(file)) {
            file = new File(getClass().getResource(FACE_DETECTION_LIBRARY).getPath()).getAbsolutePath();
        }

        logInfo(method, "loading face detection database from %s", file);
        faceDetection = new CascadeClassifier(file);

        currentFrequencyInSecs = getProperties().getFrequencyInSecs();
        restartTimer();
    }

    @Override
    public void stop() {
        String method = "stop";
        super.stop();

        if (timer != null) {
            logInfo(method, "cancelling timer ...");
            timer.cancel();
            timer = null;
        }
        closeCamera();
    }

    @Override
    public StatusModel performCommand(byte... bytes) {
        String method = "performCommand";
        Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
        String command = params.get("command");
        String payload = params.get("payload");
        StatusModel result = StatusModel.OK;
        logInfo(method, "command: %s, payload: %s", command, payload);
        if (StringUtils.equalsIgnoreCase(command, "capture")) {
            capture();
        } else {
            logInfo(method, "command was ignored");
            result.withMessage("command was ignored");
        }
        return result;
    }

    @Override
    public void notifyPropertiesChanged(Map<String, String> properties) {
        String method = "notifyPropertiesChanged";
        super.notifyPropertiesChanged(properties);
        if (properties.containsKey("frequencyInSecs")) {
            try {
                long frequency = getProperties().getFrequencyInSecs();
                if (frequency != currentFrequencyInSecs) {
                    currentFrequencyInSecs = frequency;
                    logInfo(method, "restartTimer with new frequencyInSecs: %d", currentFrequencyInSecs);
                    restartTimer();
                }
            } catch (Throwable e) {
                logError(method, e);
            }
        }
    }

    private void restartTimer() {
        String method = "restartTimer";
        if (timer != null) {
            timer.cancel();
        }
        if (currentFrequencyInSecs > 0L) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new CaptureTimerTask(), currentFrequencyInSecs * 1000L,
                    currentFrequencyInSecs * 1000L);
        } else {
            logWarn(method, "timer is disabled because currentFrequencyInSecs is negative");
        }
    }

    private synchronized void capture() {
        String method = "checkAndOpenCamera";
        try {
            // open camera
            if (getProperties().isIpCamera()) {
                String streamUrl = getProperties().getStreamUrl();
                if (StringUtils.isEmpty(streamUrl)) {
                    throw new SeleneException("streamUrl is not defined!");
                }
                logInfo(method, "opening camera from streamUrl: %s", streamUrl);
                camera = new VideoCapture(streamUrl);
            } else {
                logInfo(method, "opening camera with index: %d", getProperties().getIndex());
                camera = new VideoCapture(getProperties().getIndex());
            }

            int width = getProperties().getWidth();
            if ((int) camera.get(Videoio.CAP_PROP_FRAME_WIDTH) != width) {
                logInfo(method, "set frameWidth: %d", width);
                camera.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
            }

            int height = getProperties().getHeight();
            if ((int) camera.get(Videoio.CAP_PROP_FRAME_HEIGHT) != height) {
                logInfo(method, "set frameHeight: %d", height);
                camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
            }

            Mat frame = new Mat();

            logInfo(method, "capturing image ...");
            if (camera.read(frame)) {

                CameraDataImpl data = new CameraDataImpl();

                if (getProperties().isDetectFace()) {
                    Mat grayscale = new Mat();
                    Imgproc.cvtColor(frame, grayscale, Imgproc.COLOR_BGR2GRAY);

                    logInfo(method, "detecting face ...");
                    MatOfRect faces = new MatOfRect();
                    faceDetection.detectMultiScale(grayscale, faces, getProperties().getFaceDetectionScaleFactor(),
                            getProperties().getFaceDetectionMinNeighbors(), getProperties().getFaceDetectionFlags(),
                            new Size(getProperties().getFaceDetectionMinSizeX(),
                                    getProperties().getFaceDetectionMinSizeY()),
                            new Size(getProperties().getWidth(), getProperties().getHeight()));
                    List<Rect> list = faces.toList();
                    data.setFaceDetected(!list.isEmpty());
                    logInfo(method, "detected: %d", list.size());
                    for (Rect rect : list) {
                        logInfo(method, "---> x:%d, y:%d, w:%d, h:%d", rect.x, rect.y, rect.width, rect.height);
                        if (getProperties().isMarkDetectedFace()) {
                            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                                    new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0.0, 255.0, 0.0));
                        }
                    }
                }

                // convert frame to image
                BufferedImage buffer = matToBufferedImage(frame);
                data.setWidth(buffer.getWidth());
                data.setHeight(buffer.getHeight());
                data.setFormat(getProperties().getFormat());

                // add timestamp overlay
                if (getProperties().isOverlayEnabled()) {
                    buffer = addOverlay(buffer);
                }

                // convert image to JPG
                byte[] image = bufferedImageToByteArray(buffer);
                logInfo(method, "image size: %d", image.length);
                data.setSize(image.length);
                data.setImage(image);

                if (getProperties().isSaveLocal()) {
                    if (!StringUtils.isEmpty(getProperties().getDirectory())) {
                        try {
                            String timestamp = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)
                                    .format(ZonedDateTime.now(ZoneOffset.UTC));
                            String fileName = String.format("%s-%s.jpg", getDevice().getUid(), timestamp);
                            File file = new File(getProperties().getDirectory(), fileName);
                            Files.write(file.toPath(), image);
                            logInfo(method, "saved image to file: %s", file.getAbsolutePath());
                        } catch (Exception e) {
                            logError(method, "unable to save image locally", e);
                        }
                    } else {
                        logWarn(method, "image can't be saved locally because the directory is not defined");
                    }
                }

                logInfo(method, "queueDataForSending ...");
                queueDataForSending(data);
            } else {
                logError(method, "camera is not available");
            }
        } catch (Throwable t) {
            logError(method, "error capturing image", t);
        } finally {
            closeCamera();
        }
    }

    private BufferedImage addOverlay(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.YELLOW);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        graphics.drawString(getProperties().getOverlayUrl(), 16, image.getHeight() - 16);
        graphics.drawString(Instant.now().toString(), 16, image.getHeight() - 48);
        return image;
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        byte[] data = new byte[mat.rows() * mat.cols() * (int) mat.elemSize()];
        mat.get(0, 0, data);
        if (mat.channels() == 3) {
            for (int i = 0; i < data.length; i += 3) {
                byte temp = data[i];
                data[i] = data[i + 2];
                data[i + 2] = temp;
            }
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    private byte[] bufferedImageToByteArray(BufferedImage image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, getProperties().getFormat(), bos);
            bos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            return Utils.EMPTY_BYTE_ARRAY;
        } finally {
            AcsUtils.close(bos);
        }
    }

    private void closeCamera() {
        String method = "closeCamera";
        if (camera != null) {
            try {
                logInfo(method, "closing camera ...");
                camera.release();
            } catch (Throwable t) {
            }
            camera = null;
        }
    }

    @Override
    protected CameraProperties createProperties() {
        return new CameraProperties();
    }

    @Override
    protected CameraInfo createInfo() {
        return new CameraInfo();
    }

    @Override
    protected CameraStates createStates() {
        return new CameraStates();
    }

    private class CaptureTimerTask extends TimerTask {
        private AtomicBoolean running = new AtomicBoolean(false);

        @Override
        public void run() {
            if (!isShuttingDown()) {
                if (running.compareAndSet(false, true)) {
                    capture();
                    running.set(false);
                }
            }
        }
    }

    private static TypeReference<Map<String, String>> getMapTypeRef() {
        return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
        });
    }
}
