package com.arrow.selene.device.uvc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import com.arrow.acs.AcsUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.device.uvc.UvcDataImpl.Type;
import com.arrow.selene.engine.DeviceModuleAbstract;

import net.coobird.thumbnailator.Thumbnails;

public class UvcModule extends DeviceModuleAbstract<UvcInfo, UvcProperties, UvcStates, UvcData> {

    private static final String DATE_TIME_FORMATTER = "yyyyMMddHHmmss";
    private static final int FRAME_COUNTER_LOGGING = 150;

    @Override
    protected void startDevice() {
        super.startDevice();

        // load native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // TODO temporary (wait for redis to initialize)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
        }

        String method = "sendCommand";
        VideoCapture camera = null;
        VideoWriter videoWriter = null;
        int width = getProperties().getFrameWidth();
        int height = getProperties().getFrameHeight();
        int rate = getProperties().getFrameRate();
        boolean saveToFile = getProperties().isSaveToFile();
        boolean saveAsStream = getProperties().isSaveAsStream();

        boolean liveStream = getProperties().isLiveStream();
        int liveFrameWidth = getProperties().getLiveFrameWidth();
        int liveFrameHeight = getProperties().getLiveFrameHeight();

        int maxFrameCounter = getProperties().getMaxFrameCounter();
        boolean maxFrameCounterReached = false;

        int detectDisconnectMs = getProperties().getDetectDisconnectMs();
        int sendFrameCounter = getProperties().getSendFrameCounter();

        while (!isShuttingDown() && !maxFrameCounterReached) {
            boolean error = false;
            boolean newCapture = false;
            String timestamp = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)
                    .format(ZonedDateTime.now(ZoneOffset.UTC));
            String prefix = getDevice().getHid() + '-' + timestamp;
            String streamName = String.format(getProperties().getStreamNamePattern(), prefix);
            String fileName = String.format(getProperties().getFileNamePattern(), prefix);
            DataOutputStream stream = null;
            FileOutputStream fos = null;
            try {
                if (camera == null) {
                    camera = new VideoCapture(getProperties().getIndex());
                    camera.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
                    camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
                    camera.set(Videoio.CAP_PROP_FPS, rate);
                    newCapture = true;
                } else {
                    logInfo(method, "camera already opens");
                    newCapture = false;
                }

                if (saveToFile) {
                    if (saveAsStream) {
                        File file = new File(getProperties().getDirectory(), streamName);
                        logInfo(method, "new stream file: %s", file.getAbsolutePath());
                        fos = new FileOutputStream(file);
                        stream = new DataOutputStream(fos);
                    } else {
                        File file = new File(getProperties().getDirectory(), fileName);
                        logInfo(method, "new video file: %s", file.getAbsolutePath());
                        String fourcc = getProperties().getFourcc();
                        videoWriter = new VideoWriter(file.getAbsolutePath(), VideoWriter.fourcc(fourcc.charAt(0),
                                fourcc.charAt(1), fourcc.charAt(2), fourcc.charAt(3)), rate, new Size(width, height),
                                true);
                    }
                } else {
                    logInfo(method, "WARNING: videos are not saved to file");
                }

                if (!camera.isOpened()) {
                    throw new SeleneException("ERROR: webcam is not connected!");
                } else if (videoWriter != null && !videoWriter.isOpened()) {
                    throw new SeleneException("ERROR: unable to capture webcam, possibly configuration error");
                } else {
                    Mat frame = new Mat();
                    int counter = 0;
                    long lastTs = 0;
                    long videoStart = Instant.now().toEpochMilli();

                    // send start telemetry
                    if (saveToFile) {
                        sendTelemetry(new UvcDataImpl().withType(Type.VIDEO_START).withFileName(fileName));
                    }
                    if (liveStream && newCapture) {
                        sendTelemetry(new UvcDataImpl().withType(Type.STREAM_START).withFileName(streamName));
                        newCapture = false;
                    }

                    boolean createNewFile = false;
                    while (!isShuttingDown() && !maxFrameCounterReached) {
                        if (camera.read(frame)) {
                            long now = Instant.now().toEpochMilli();
                            long elapsed = now - lastTs;
                            if (lastTs == 0 || elapsed < detectDisconnectMs) {
                                ++counter;
                                lastTs = now;

                                // logging
                                if (counter % FRAME_COUNTER_LOGGING == 0) {
                                    logInfo(method, "captured %d frame ", counter);
                                }

                                // check live stream
                                if (liveStream && (counter % sendFrameCounter == 0)) {

                                    // convert frame to image
                                    BufferedImage buffer = matToBufferedImage(frame);

                                    // resize image
                                    if (width != liveFrameWidth || height != liveFrameHeight) {
                                        buffer = Thumbnails.of(buffer).size(liveFrameWidth, liveFrameHeight)
                                                .asBufferedImage();
                                    }

                                    // add timestamp overlay
                                    buffer = addTimestampOverlay(buffer);

                                    // convert image to JPG
                                    byte[] image = bufferedImageToByteArray(buffer);

                                    // send image as telemetry
                                    sendTelemetry(new UvcDataImpl().withType(Type.STREAM_DATA).withFileName(streamName)
                                            .withFrame(counter).withImage(image));
                                }

                                // check save to file
                                if (saveToFile) {
                                    createNewFile = lastTs - videoStart > getProperties().getVideoLenMins() * 60 * 1000;
                                    if (saveAsStream) {
                                        byte[] image = bufferedImageToByteArray(
                                                addTimestampOverlay(matToBufferedImage(frame)));
                                        stream.writeLong(now);
                                        stream.writeInt(image.length);
                                        stream.write(image);
                                        stream.flush();

                                        // sync to disk
                                        fos.getFD().sync();
                                        if (createNewFile) {
                                            logInfo(method, "videoLength exceeded, writing to new stream ...");
                                            AcsUtils.close(stream);
                                            stream = null;
                                            break;
                                        }
                                    } else {
                                        videoWriter.write(frame);
                                        if (createNewFile) {
                                            logInfo(method, "videoLength exceeded, writing to new file ...");
                                            releaseVideoWriter(videoWriter);
                                            videoWriter = null;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                throw new Exception("ERROR: looks like the webcam is disconnected!");
                            }
                        } else {
                            throw new Exception("ERROR; unable to capture frame");
                        }

                        maxFrameCounterReached = maxFrameCounter > 0 && maxFrameCounter < counter;
                    }
                    logInfo(method,
                            "WARNING: capturing stopped, isShuttingDown: %s, maxFrameCounterReached: %s, createNewFile: %s",
                            isShuttingDown(), maxFrameCounterReached, createNewFile);

                    if (saveToFile) {
                        sendTelemetry(new UvcDataImpl().withType(Type.VIDEO_STOP).withFileName(fileName));
                    }
                }
            } catch (Throwable e) {
                error = true;
                logError(method, "webcam system error", e);
                releaseVideoWriter(videoWriter);
                releaseCamera(camera);
                videoWriter = null;
                camera = null;
            } finally {
                if (liveStream) {
                    sendTelemetry(new UvcDataImpl().withType(Type.STREAM_STOP).withFileName(streamName));
                }
            }

            if (maxFrameCounterReached) {
                logInfo(method, "WARNING: webcam stopped due to max frame counter reached: %s", maxFrameCounter);
            } else if (error) {
                logInfo(method, "will retry webcam in %s seconds ...", getProperties().getRetryIntervalSecs());
                try {
                    Thread.sleep(getProperties().getRetryIntervalSecs() * 1000);
                } catch (Exception e) {
                }
            }
        }

        // final release
        releaseVideoWriter(videoWriter);
        releaseCamera(camera);

        logInfo(method, "exiting ...");
    }

    private BufferedImage addTimestampOverlay(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.YELLOW);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        graphics.drawString(Instant.now().toString(), 16, image.getHeight() - 16);
        return image;
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        byte[] data = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
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
            ImageIO.write(image, "jpg", bos);
            bos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        } finally {
            AcsUtils.close(bos);
        }
    }

    private void releaseVideoWriter(VideoWriter videoWriter) {
        String method = "releaseVideoWriter";
        if (videoWriter != null) {
            try {
                logInfo(method, "releasing videoWriter ...");
                videoWriter.release();
            } catch (Exception e) {
            }
        }
    }

    private void releaseCamera(VideoCapture camera) {
        String method = "releaseCamera";
        if (camera != null) {
            try {
                logInfo(method, "releasing camera ...");
                camera.release();
            } catch (Exception e) {
            }
        }
    }

    private void sendTelemetry(final UvcData data) {
        queueDataForSending(data);
    }

    @Override
    protected UvcProperties createProperties() {
        return new UvcProperties();
    }

    @Override
    protected UvcInfo createInfo() {
        return new UvcInfo();
    }

    @Override
    protected UvcStates createStates() {
        return new UvcStates();
    }
}