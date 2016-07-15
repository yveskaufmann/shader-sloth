package eu.yvka.shadersloth.app.controls.dialog;

import eu.yvka.shadersloth.app.renderView.ShaderSlothRenderer;
import eu.yvka.shadersloth.app.controls.MessageBanner;
import eu.yvka.shadersloth.app.controls.validation.Validations;
import eu.yvka.shadersloth.app.geometry.LoadMeshTask;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.geometry.primitives.Sphere;
import eu.yvka.slothengine.scene.Geometry;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;

import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;

public class GeometryCreateDialog extends Dialog<Geometry> {

	   enum GeometryType {
		   CUBE(getString("geometry.create.dlg.type.cube")),
		   SPHERE(getString("geometry.create.dlg.type.sphere")),
		   CREATE_FROM_FILE(getString("geometry.create.dlg.type.file"));

		   GeometryType(String label) {
			   this.label = label;
		   }
		   String label;

		   @Override
		   public String toString() {
			   return label;
		   }
	  }

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/
	public static final double DEFAULT_CUBE_SIZE = 1.0;
	private NumberFormat numberFormatInstance;
	private NumberFormat integerOnlyNumberFormat;
	private ValidationSupport validationSupport;
	private MessageBanner messageBanner;

	/* Basic Dialog  */
	private GridPane rootGrid;
	private TextField geometryId;
	private ChoiceBox<GeometryCreateDialog.GeometryType> modelTypeChoice;

	/* File Dialog  */
	private FileChooser fileModelChooser = null;
	private TextField pathToMeshTextfield;
	private Button fileChooserButton;

	/* Sphere Dialog  */
	private TextField radiusField;
	private TextField ringsField;
	private TextField segmentsField;

	/* Model Dialog  */
	private TextField cubesizeField;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public GeometryCreateDialog() {
		super();
		initialize();
		numberFormatInstance = NumberFormat.getNumberInstance();
		integerOnlyNumberFormat = (NumberFormat) numberFormatInstance.clone();
		integerOnlyNumberFormat.setParseIntegerOnly(true);

	}

	private void initialize() {
		setTitle(getString("geometry.create.dlg.title"));
		setHeaderText(getString("geometry.create.dlg.header"));
		setGraphic(new ImageView(ShaderSlothRenderer.class.getResource("images/mesh_icon_32x32.png").toExternalForm()));
		setResizable(false);

		messageBanner = new MessageBanner();
		String geometryIdLabel = getString("geometry.create.dlg.id.label");
		geometryId = TextFields.createClearableTextField();
		geometryId.setPromptText(getString("geometry.create.dlg.id.prompt"));
		geometryId.setTooltip(new Tooltip(getString("geometry.create.dlg.id.tooltip")));

		String geometryTypeLabel = getString("geometry.create.dlg.type");
		modelTypeChoice = new ChoiceBox<>();
		modelTypeChoice.setMaxWidth(Double.MAX_VALUE);
		modelTypeChoice.setTooltip(new Tooltip(getString("geometry.create.dlg.type.tooltip")));
		modelTypeChoice.getItems().addAll(GeometryCreateDialog.GeometryType.values());
		modelTypeChoice.valueProperty().addListener(this::onModelTypeChanged);

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

		addFormEntry(0, 1, geometryIdLabel, geometryId);
		addFormEntry(0, 2, geometryTypeLabel, modelTypeChoice);
		VBox content = new VBox(5, messageBanner, rootGrid);
		content.setAlignment(Pos.TOP_CENTER);
		getDialogPane().setContent(content);
		getDialogPane().getScene().getWindow().sizeToScene();

		validationSupport = new ValidationSupport();
		validationSupport.registerValidator(geometryId, Validations.createEmptyValidator(geometryIdLabel));

		createObjectFileEditor();
		createSphereEditor();
		createCubeEditor();


		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);

		// Ensure dialog can only submit if input is valid
		okButton.addEventFilter(ActionEvent.ACTION, event -> {
			if (validationSupport.isInvalid()) {
				messageBanner.show(validationSupport);
				event.consume();
			} else {
				messageBanner.hide();
			}

			getDialogPane().getScene().getWindow().sizeToScene();
		});

		setResultConverter(buttonType -> {
			if (buttonType.equals(ButtonType.OK) && modelTypeChoice.getValue() != null) {
				Geometry geometry = new Geometry(geometryId.getText());
				switch (modelTypeChoice.getValue()) {
					case CUBE:
						Cube cube = new Cube();
						geometry.setMesh(cube);
						geometry.setScale(((Number)cubesizeField.getTextFormatter().getValue()).floatValue());
						break;
					case SPHERE:
						Sphere sphere = new Sphere(
						((Number) radiusField.getTextFormatter().getValue()).floatValue(),
						((Number) ringsField.getTextFormatter().getValue()).intValue(),
						((Number) segmentsField.getTextFormatter().getValue()).intValue());
						geometry.setMesh(sphere);
						break;
					case CREATE_FROM_FILE:
						String meshToLoad = pathToMeshTextfield.getText();
						LoadMeshTask loadMeshTask = new LoadMeshTask(meshToLoad);
						loadMeshTask.setOnFailed(event -> {
							Throwable causingExecption = loadMeshTask.getException();
							String cause = causingExecption == null ? "" : "\n" + causingExecption.getMessage();
 							Alert errorCouldNotLoad = new Alert(Alert.AlertType.ERROR, getString("geometry.create.dlg.file.loadFailed", meshToLoad));
							errorCouldNotLoad.setTitle(getString("geometry.create.dlg.file.loadFailed.title"));
							errorCouldNotLoad.setHeaderText(getString("geometry.create.dlg.file.loadFailed.title"));
							errorCouldNotLoad.initOwner(getOwner());
							errorCouldNotLoad.show();
                        });
						loadMeshTask.setOnSucceeded((event) -> geometry.setMesh(loadMeshTask.getValue()));
						loadMeshTask.run();
						break;
				}

				return geometry;
			}
			return null;
		});
	}


	private void createObjectFileEditor() {
		pathToMeshTextfield = TextFields.createClearableTextField();
		fileChooserButton = new Button("...");
		fileChooserButton.setOnAction(this::onButtonChooserButtonPressed);
	}

	private void createSphereEditor() {
		radiusField = createNumberOnlyTextfield(3);
		radiusField.setPromptText(getString("geometry.create.dlg.sphere.radius.prompt"));

		ringsField = createNumberOnlyTextfield(3);
		ringsField.setPromptText(getString("geometry.create.dlg.sphere.rings.prompt"));

		segmentsField = createNumberOnlyTextfield(3);
		segmentsField.setPromptText(getString("geometry.create.dlg.sphere.segments.prompt"));

	}

	private void createCubeEditor() {
		cubesizeField = createNumberOnlyTextfield(DEFAULT_CUBE_SIZE);
		cubesizeField.setPromptText("Kantenlänge des Würfels");
		cubesizeField.setTooltip(new Tooltip("Bestimmt wie groß der Würfel ist"));
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void showCubeEditor() {
		String cubeSizeLabel = getString("geometry.create.dlg.cube.size.label");
		addFormEntry(0, 3, cubeSizeLabel, cubesizeField);

		if (! validationSupport.getRegisteredControls().contains(cubesizeField)) {
			validationSupport.registerValidator(cubesizeField, false, Validations.createMinimumValidator(cubeSizeLabel, 1.0));
		}
	}

	private void showSphereEditor() {
		String radiusLabel = getString("geometry.create.dlg.sphere.radius.label");
		String ringsLabel = getString("geometry.create.dlg.sphere.rings.label");
		String segmentsLabel= getString("geometry.create.dlg.sphere.segments.label");

		addFormEntry(0, 3, radiusLabel, radiusField);
		addFormEntry(0, 4, ringsLabel, ringsField);
		addFormEntry(0, 5, segmentsLabel, segmentsField);

		if (! validationSupport.getRegisteredControls().contains(radiusField)) {
			validationSupport.registerValidator(radiusField, true, Validations.createEmptyValidator(radiusLabel));
			validationSupport.registerValidator(ringsField, true, Validations.createMinimumValidator(ringsLabel, 3));
			validationSupport.registerValidator(segmentsField, true, Validations.createMinimumValidator(segmentsLabel, 3));
		}
	}

	private void showObjectFileEditor() {
		addFormEntry(0, 3, getString("geometry.create.dlg.file.label"), pathToMeshTextfield, fileChooserButton);
	}

	public Collection<FileChooser.ExtensionFilter> getSupportedExtensions() {
		return Arrays.asList(
			new FileChooser.ExtensionFilter("Wavefront .obj file", "*.obj")
		);
	}

	/******************************************************************************
	 *
	 * Event Handling
	 *
	 ******************************************************************************/

	private void onModelTypeChanged(ObservableValue<? extends GeometryType> observable, GeometryType oldType, GeometryType newType) {
		if (rootGrid.getChildren().size() > 4) {
			rootGrid.getChildren().remove(4, rootGrid.getChildren().size());
		}

		radiusField.setText("3");
		ringsField.setText("3");
		segmentsField.setText("3");
		cubesizeField.setText(DEFAULT_CUBE_SIZE + "");
		pathToMeshTextfield.setText("");
		validationSupport.initInitialDecoration();

		switch (newType) {
			case CUBE: showCubeEditor(); break;
			case SPHERE:  showSphereEditor();break;
			case CREATE_FROM_FILE: showObjectFileEditor(); break;
		}

		getDialogPane().getScene().getWindow().sizeToScene();
	}

	private void onButtonChooserButtonPressed(ActionEvent event) {
		event.consume();
		fileModelChooser = new FileChooser();
		fileModelChooser.setInitialDirectory(new File("assets/models"));
		fileModelChooser.setTitle("Choose a model file");
		fileModelChooser.getExtensionFilters().addAll(getSupportedExtensions());
		File file = fileModelChooser.showOpenDialog(getOwner());
		if (file != null && file.exists()) {
			pathToMeshTextfield.setText(file.getAbsolutePath());
		}

	}

	/******************************************************************************
	 *
	 * Utils
	 *
	 ******************************************************************************/

	private void addFormEntry(int col, int row, String labelText, Control ...controls) {
		Label label = new Label(labelText);
		label.setAlignment(Pos.BASELINE_LEFT);
		rootGrid.add(label, col, row);
		col = col + 1;
		for (int i = 0; i < controls.length; i++) {
			rootGrid.add(controls[i], col + i, row);
		}
	}

	private TextField createNumberOnlyTextfield(double defaultSize) {
		TextField textField = new TextField();
		textField.setMaxWidth(Double.MAX_VALUE);
		NumberStringConverter numberStringConverter = new NumberStringConverter(numberFormatInstance);
		textField.setTextFormatter(new TextFormatter<>(numberStringConverter, defaultSize, change -> {
			if (change.isAdded() || change.isReplaced()) {
				String text = change.getText();
				for (int i = 0; i < text.length(); i++) {
					char chr = text.charAt(i);
					if (!(Character.isDigit(chr) || chr == '.' || chr == ',' )) return null;
				}
			}
			return change;
		}));
		return textField;
	}
}
