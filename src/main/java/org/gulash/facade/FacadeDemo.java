package org.gulash.facade;

import org.gulash.facade.model.VideoConversionFacade;

import java.io.File;

public class FacadeDemo {
    public static void main(String[] args) {
        VideoConversionFacade converter = new VideoConversionFacade();
        File mp4Video = converter.convertVideo("youtubevideo.ogg", "mp4");
        // ... работа с результатом
    }
}
