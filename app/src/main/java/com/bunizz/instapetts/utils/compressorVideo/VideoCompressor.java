package com.bunizz.instapetts.utils.compressorVideo;

import android.content.Context;


import com.bunizz.instapetts.utils.compressorVideo.strategies.Android720pFormatStrategy;
import com.bunizz.instapetts.utils.compressorVideo.strategies.MediaFormatStrategy;
import com.bunizz.instapetts.utils.compressorVideo.transcoder.VideoCompressEngine;

import java.io.IOException;

public class VideoCompressor {

    public boolean compressVideo(Context context, String inputPath, String outPath) {
        return compressVideo(context, inputPath, outPath, new Android720pFormatStrategy(1280000, 128000, 1));
    }

    public boolean compressVideo(Context context, String inputPath, String outputPath, MediaFormatStrategy strategy) {
        VideoCompressEngine engine = new VideoCompressEngine();
        try {
            engine.transcodeVideo(context, inputPath, outputPath, strategy);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
