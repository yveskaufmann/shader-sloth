package eu.yvka.shadersloth.controller;

import javafx.beans.property.FloatProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;
import eu.yvka.shadersloth.model.Vector3fModel;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class VectorEditorController implements Initializable {

	@FXML
	private TextField xTextField;

	@FXML
	private TextField yTextField;

	@FXML
	private TextField zTextField;

	@FXML
	private Label vectorLabel;

	private Vector3fModel model;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new Vector3fModel();

		NumberFormat format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(2);
		NumberStringConverter converter = new NumberStringConverter(format);

		setupNumberField(xTextField, converter, model.xProperty());
		setupNumberField(yTextField, converter, model.yProperty());
		setupNumberField(zTextField, converter, model.zProperty());
	}

	private void setupNumberField(TextField textField, NumberStringConverter converter, FloatProperty floatProperty) {
		TextFormatter<Number> formatter = new TextFormatter<>(converter);
		formatter.valueProperty().addListener((observable, oldNumber, newNumber) -> {
			floatProperty.setValue(newNumber);
		});
		formatter.valueProperty().bindBidirectional(floatProperty);
		textField.setTextFormatter(formatter);
	}

	public static Pane getView() {
		try {
			URL viewURL = VectorEditorController.class.getResource("../view/vectorEditorView.fxml");
			FXMLLoader loader = new FXMLLoader(viewURL);
			loader.setController(new VectorEditorController());
			return loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
