package eu.yvka.shadersloth.app.project;

import eu.yvka.shadersloth.app.AppFolder;
import eu.yvka.shadersloth.share.controls.FileAlreadyExistsDialog;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.File;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;

/**
 * A dialog which requests a project name from
 * the user for a new project.
 * <p/>
 *
 * @author Yves Kaufmann
 * @since 26.07.2016
 */
public class ProjectCreateDialog extends TextInputDialog {

	public ProjectCreateDialog(boolean isInitialProject) {
		super("untitled");
		setTitle(getString("project.create.dlg.title"));
		setHeaderText(getString("project.create.dlg.header"));
		getEditor().setPromptText(getString("project.create.dlg.name.prompt"));

		final Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
		okButton.addEventFilter(ActionEvent.ACTION, this::onOkButtonClicked);

		// when no project is currently active then a project must created
		if (isInitialProject) {
			getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
			getDialogPane().setStyle("-fx-border-color: black;");
			initStyle(StageStyle.UNDECORATED);
		}
	}

	public void onOkButtonClicked(ActionEvent e) {
		String projectName = getEditor().getText();

		// ensure that the user has entered a project name
		if (projectName.trim().equals("")) {
			e.consume();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(getString("project.create.dlg.missing.name"));
			alert.showAndWait();
		}

		// ensure project isn't already created
		final File projectFolder = new File(AppFolder.getProjectsFolder(), projectName);
		if (projectFolder.exists()) {
			e.consume();
			FileAlreadyExistsDialog fileAlreadyExistsDialog = new FileAlreadyExistsDialog(projectFolder);
			fileAlreadyExistsDialog.showAndWait().ifPresent(buttonType -> {
				if (buttonType.equals(ButtonType.YES)) {
					setResult(projectName);
					hide();
				}
			});
		}
	}

}
