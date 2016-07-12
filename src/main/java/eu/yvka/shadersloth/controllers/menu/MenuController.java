package eu.yvka.shadersloth.controllers.menu;

import eu.yvka.shadersloth.App;
import eu.yvka.shadersloth.utils.controller.AbstractController;
import javafx.scene.control.ListView;

/**
 * Controller which is responsible to handle
 * menu actions and rendering of the main menu.
 *
 *
 * @Author Yves Kaufmann
 * @since 01.07.2016
 */
public class MenuController extends AbstractController {


	/***
	 * Build the menu.
	 */
	public MenuController() {
		super(App.class.getResource("views/menu/menubar.fxml"));
	}

	@Override
	protected void onFxmlLoaded() {
	}
}
