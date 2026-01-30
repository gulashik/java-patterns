package org.gulash.facade.model;

import org.gulash.facade.model.subsystem.AudioMixer;
import org.gulash.facade.model.subsystem.BitrateReader;
import org.gulash.facade.model.subsystem.VideoFile;
import org.gulash.facade.model.subsystem.codec.Codec;
import org.gulash.facade.model.subsystem.codec.CodecFactory;
import org.gulash.facade.model.subsystem.codec.MPEG4CompressionCodec;
import org.gulash.facade.model.subsystem.codec.OggCompressionCodec;

import java.io.File;

/**
 * Фасад для конвертации видео.
 * Он скрывает сложность работы с различными классами подсистемы.
 */
public class VideoConversionFacade {
    public File convertVideo(String fileName, String format) {
        System.out.println("VideoConversionFacade: conversion started.");

        VideoFile file = new VideoFile(fileName);

        Codec sourceCodec = CodecFactory.extract(file);
        Codec destinationCodec;
        if (format.equals("mp4")) {
            destinationCodec = new MPEG4CompressionCodec();
        } else {
            destinationCodec = new OggCompressionCodec();
        }

        VideoFile buffer = BitrateReader.read(file, sourceCodec);
        VideoFile intermediateResult = BitrateReader.convert(buffer, destinationCodec);

        File result = (new AudioMixer()).fix(intermediateResult);
        System.out.println("VideoConversionFacade: conversion completed.");
        return result;
    }
}
