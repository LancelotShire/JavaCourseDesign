package com.teach.javafx.controller.base;

import com.teach.javafx.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class ProjectPdfController {
    @FXML
    private Pagination pagination;
    private PdfModel model;

    @FXML
    public void initialize() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("project.pdf");
        model = new PdfModel(in,0.75f);
        pagination.setPageCount(model.numPages());
        pagination.setPageFactory(index -> new ImageView(model.getImage(index)));
    }
}