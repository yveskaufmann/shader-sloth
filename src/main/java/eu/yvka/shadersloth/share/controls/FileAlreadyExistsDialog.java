package eu.yvka.shadersloth.share.controls;

import eu.yvka.shadersloth.share.I18N.I18N;
import javafx.beans.NamedArg;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.text.MessageFormat;
import java.util.Dictionary;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;

/**
 * @author Yves Kaufmann
 * @since 26.07.2016
 */
public class FileAlreadyExistsDialog extends Alert {

	public FileAlreadyExistsDialog(File file) {
		this(file.getAbsolutePath());
	}

	public FileAlreadyExistsDialog(String file) {
		super(AlertType.CONFIRMATION);

		setTitle(getString("alreadyexists.dlg.title"));
		setContentText(getString("alreadyexists.dlg.content", file));
		getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
	}
}
