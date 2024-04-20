package com.teach.javafx.controller.base;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * PDF文書のページを表すクラス。
 *
 * @author toru
 */
public class PdfModel {
    private static final Logger logger = Logger.getLogger(PdfModel.class.getName());

    private PDDocument document;
    private PDFRenderer renderer;

    private float scale= 0.75f;
    public PdfModel(Path path) {
        try {
            document = PDDocument.load(path.toFile());
            renderer = new PDFRenderer(document);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDDocument thorws IOException file=" + path, ex);
        }
    }
    public PdfModel(byte [] data,float scale) {
        this.scale =scale;
        try {
            document = PDDocument.load(data);
            renderer = new PDFRenderer(document);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDDocument thorws IOException file=" , ex);
        }
    }
    public PdfModel(InputStream in,float scale) {
        this.scale =scale;
        try {
            document = PDDocument.load(in);
            renderer = new PDFRenderer(document);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDDocument thorws IOException file=" , ex);
        }
    }

    public int numPages() {
        return document.getPages().getCount();
    }

    public Image getImage(int pageNumber) {
        BufferedImage pageImage;
        try {
            pageImage = renderer.renderImage(pageNumber,scale);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDFRenderer throws IOException", ex);
        }
        return SwingFXUtils.toFXImage(pageImage, null);
    }
}