package eu.yvka.shadersloth.app.materialEditor.shaders.errors;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;

/**
 * @author Yves Kaufmann
 * @since 18.07.2016
 */
public class ShaderErrorTable extends TableView {

	public ShaderErrorTable() {
		super();

		final Label noErrorLabel = new Label(getString("shader.error.noerrors"));
		noErrorLabel.setStyle("-fx-font-weight: bold");
		setPlaceholder(noErrorLabel);

		TableColumn<ShaderError, String> descriptionColumn = new TableColumn<>(getString("shader.error.descriptionColumn"));
		TableColumn<ShaderError, String> shaderColumn = new TableColumn<>(getString("shader.error.shaderColumn"));
		TableColumn<ShaderError, Integer> locationColumn = new TableColumn<>(getString("shader.error.locationColumn"));

		descriptionColumn.setCellFactory(ShaderErrorCellFactory::getDescriptionCell);

		descriptionColumn.setCellValueFactory(new PropertyValueFactory<ShaderError, String >("description"));
		shaderColumn.setCellValueFactory(new PropertyValueFactory<ShaderError, String>("shaderName"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<ShaderError, Integer>("lineNumber"));

		descriptionColumn.prefWidthProperty().bind(widthProperty().multiply(0.6f));
		shaderColumn.prefWidthProperty().bind(widthProperty().multiply(0.2f));

		descriptionColumn.getStyleClass().add("align-left");
		shaderColumn.getStyleClass().add("align-left");
		locationColumn.getStyleClass().add("align-left");

		getColumns().setAll(descriptionColumn, shaderColumn, locationColumn);

	}

}
