package eu.yvka.shadersloth.app.shaders.errors;

import eu.yvka.shadersloth.app.App;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Yves Kaufmann
 * @since 18.07.2016
 */
public class ShaderErrorCellFactory {
	public static TableCell<ShaderError, String> getDescriptionCell(TableColumn<ShaderError, String> shaderErrorStringTableColumn) {
		return new TableCell<ShaderError, String>() {

			private Image errorIcon;
			private Image warningIcon;
			private ImageView imageView;

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					if (imageView == null) {
						imageView = new ImageView();
						imageView.setFitWidth(16);
						imageView.setFitHeight(16);
					}

					if (errorIcon == null) {
						errorIcon = new Image(App.class.getResource("images/error_icon_16x16.png").toExternalForm());
					}

					if (warningIcon == null) {
						warningIcon = new Image(App.class.getResource("images/warning_icon_16x16.png").toExternalForm());
					}

					if (item.contains("error")) {
						imageView.setImage(errorIcon);
					} else {
						imageView.setImage(warningIcon);
					}


					setGraphic(imageView);
					setText(item);
				}
			}
		};
	}
}
