package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.controls.MessageBanner;
import eu.yvka.shadersloth.app.controls.validation.Validations;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.geometry.primitives.Sphere;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.light.AmbientLight;
import eu.yvka.slothengine.scene.light.Light;
import eu.yvka.slothengine.scene.light.PointLight;
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

public class LightCreateDialog extends Dialog<Light> {

	   enum LightType {
		   Point(getString("light.create.dlg.type.point")),
		   Ambient(getString("light.create.dlg.type.ambient"));

		   LightType(String label) {
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
	private ValidationSupport validationSupport;
	private MessageBanner messageBanner;

	/* Basic Dialog  */
	private FormPane rootGrid;
	private TextField lightId;
	private ChoiceBox<LightType> modelTypeChoice;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public LightCreateDialog() {
		super();
		initialize();
	}

	private void initialize() {
		setTitle(getString("light.create.dlg.title"));
		setHeaderText(getString("light.create.dlg.header"));
		setGraphic(new ImageView(App.class.getResource("images/bulb_icon_32x32.png").toExternalForm()));
		setResizable(false);

		messageBanner = new MessageBanner();
		String lightIdLabel = getString("light.create.dlg.id.label");
		lightId = TextFields.createClearableTextField();
		lightId.setPromptText(getString("light.create.dlg.id.prompt"));
		lightId.setTooltip(new Tooltip(getString("light.create.dlg.id.tooltip")));

		String lightTypeLabel = getString("light.create.dlg.type");
		modelTypeChoice = new ChoiceBox<>();
		modelTypeChoice.setMaxWidth(Double.MAX_VALUE);
		modelTypeChoice.setTooltip(new Tooltip(getString("light.create.dlg.type.tooltip")));
		modelTypeChoice.getItems().addAll(LightType.values());

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

		rootGrid = new FormPane();
		rootGrid.getColumnConstraints().addAll(col1, col2, col3);
		rootGrid.setAlignment(Pos.CENTER);
		rootGrid.setHgap(5);
		rootGrid.setVgap(5);

		rootGrid.addFormEntry(0, 1, lightIdLabel, lightId);
		rootGrid.addFormEntry(0, 2, lightTypeLabel, modelTypeChoice);
		VBox content = new VBox(5, messageBanner, rootGrid);
		content.setAlignment(Pos.TOP_CENTER);
		getDialogPane().setContent(content);
		getDialogPane().getScene().getWindow().sizeToScene();

		validationSupport = new ValidationSupport();
		validationSupport.registerValidator(lightId, Validations.createEmptyValidator(lightIdLabel));


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
				final String id = this.lightId.getText();
				switch (modelTypeChoice.getValue()) {
					case Point:return new PointLight(id);
					case Ambient: return new AmbientLight(id);
				}
			}
			return null;
		});
	}
}
