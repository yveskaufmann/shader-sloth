package de.yvka.shadersloth.dialog;

import de.yvka.slothengine.scene.Geometry;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import de.yvka.shadersloth.ShaderSloth;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

public class GeometryCreateDialog extends Dialog<Geometry> {

	public static final String KIND_CUBE = "Cube";
	public static final String KIND_SPHERE = "Sphere";
	public static final String KIND_OBJECT_FILE = "Object File";
	private final FileChooser modelChooser = new FileChooser();
	private ChoiceBox<String> modelTypeChoice;
	private TextField geometryId;
	private GridPane modelKindEditor;

	public GeometryCreateDialog() {
		super();
		initialize();
	}

	private void initialize() {
		setTitle("Create Geometry");
		setHeaderText("Specify your desired geometry");
		setGraphic(new ImageView(ShaderSloth.class.getResource("images/mesh_icon_32x32.png").toExternalForm()));

		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDisable(true);

		geometryId = new TextField();
		modelTypeChoice = new ChoiceBox<>();
		modelTypeChoice.setMaxWidth(Double.MAX_VALUE);
		modelTypeChoice.setItems(FXCollections.observableArrayList(KIND_CUBE, KIND_SPHERE, KIND_OBJECT_FILE));

		modelKindEditor =  new GridPane();
		modelKindEditor.setHgap(10);
		modelKindEditor.setVgap(10);
		modelKindEditor.setGridLinesVisible(true);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.add(new Label("Id:"), 0, 0);
		grid.add(geometryId, 1, 0);
		grid.add(new Label("Kind:"), 0, 1);
		grid.add(modelTypeChoice, 1, 1);
		grid.add(modelKindEditor, 0, 2, 3, 1);


		modelTypeChoice.valueProperty().addListener((observable, oldValue, newValue) -> {
			modelKindEditor.getChildren().clear();
			modelKindEditor.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			switch (newValue) {
				case KIND_CUBE: createCubeEditor(); break;
				case KIND_SPHERE:  createSphereEditor();break;
				case KIND_OBJECT_FILE: createObjectFileEditor(); break;
			}
			getDialogPane().requestFocus();
        });

		getDialogPane().setContent(grid);
		getDialogPane().requestLayout();
	}

	private Node createObjectFileEditor() {

		TextField filePath = new TextField();
		Button fileChooserButton = new Button("...");
		fileChooserButton.setOnAction((event) -> {
			event.consume();
			modelChooser.setInitialDirectory(new File("assets/models"));
			modelChooser.setTitle("Choose a model file");
			modelChooser.getExtensionFilters().addAll(getSupportedExtensions());
			//
			// modelChooser.showOpenDialog(new Stage());

			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);
		});

		modelKindEditor.add(new Label("File"), 0, 1);
		modelKindEditor.add(filePath, 1, 1);
		modelKindEditor.add(fileChooserButton, 2, 1);

		return modelKindEditor;
	}

	private Node createSphereEditor() {
		return null;
	}


	private Node createCubeEditor() {
		return null;
	}


	public Collection<FileChooser.ExtensionFilter> getSupportedExtensions() {
		return Arrays.asList(
			new FileChooser.ExtensionFilter("Wavefront .obj file", "*.obj")
		);
	}
}
