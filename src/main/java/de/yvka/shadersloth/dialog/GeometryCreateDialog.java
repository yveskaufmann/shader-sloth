package de.yvka.shadersloth.dialog;

import de.yvka.slothengine.scene.Geometry;
import javafx.geometry.HPos;
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
	private GridPane rootGrid;

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
		geometryId.setPromptText("Geomerty Id");
		geometryId.setTooltip(new Tooltip("The Identifier for this geometry"));

		modelTypeChoice = new ChoiceBox<>();
		modelTypeChoice.setMaxWidth(Double.MAX_VALUE);
		modelTypeChoice.setItems(FXCollections.observableArrayList(KIND_CUBE, KIND_SPHERE, KIND_OBJECT_FILE));

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHalignment(HPos.RIGHT);
		col1.setMinWidth(50);
		col1.setPercentWidth(15);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHalignment(HPos.LEFT);
		col2.setPercentWidth(80);
		col2.setFillWidth(true);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setHalignment(HPos.LEFT);
		col3.setPercentWidth(5);



		rootGrid = new GridPane();
		rootGrid.getColumnConstraints().addAll(col1, col2, col3);
		rootGrid.setAlignment(Pos.CENTER);
		rootGrid.setHgap(5);
		rootGrid.setVgap(5);
		rootGrid.setPadding(new Insets(20, 150, 10, 10));
		rootGrid.add(new Label("Id:"), 0, 0);
		rootGrid.add(geometryId, 1, 0);
		rootGrid.add(new Label("Kind:"), 0, 1);
		rootGrid.add(modelTypeChoice, 1, 1);


		modelTypeChoice.valueProperty().addListener((observable, oldValue, newValue) -> {

			if (rootGrid.getChildren().size() > 4) {
				rootGrid.getChildren().remove(4, rootGrid.getChildren().size());
			}

			switch (newValue) {
				case KIND_CUBE: createCubeEditor(); break;
				case KIND_SPHERE:  createSphereEditor();break;
				case KIND_OBJECT_FILE: createObjectFileEditor(); break;
			}
			getDialogPane().requestFocus();
        });

		getDialogPane().setContent(rootGrid);
		getDialogPane().layout();
	}

	private void createObjectFileEditor() {
		BorderStroke borderStroke = new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT);
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

		rootGrid.add(new Label("File:"), 0, 2);
		rootGrid.add(filePath, 1, 2);
		rootGrid.add(fileChooserButton, 2, 2);

	}

	private void createSphereEditor() {
	}


	private void createCubeEditor() {
	}


	public Collection<FileChooser.ExtensionFilter> getSupportedExtensions() {
		return Arrays.asList(
			new FileChooser.ExtensionFilter("Wavefront .obj file", "*.obj")
		);
	}
}
