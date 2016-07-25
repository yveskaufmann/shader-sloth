package eu.yvka.shadersloth.app.menubar;

import com.google.inject.Inject;
import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.shadersloth.app.ShaderSlothController;
import eu.yvka.shadersloth.app.preferences.GlobalUserPreferences;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.yvka.shadersloth.app.ShaderSlothController.*;

/**
 * Controller which is responsible to handle
 * menubar actions and rendering of the menubar bar
 *
 *
 * @Author Yves Kaufmann
 * @since 01.07.2016
 */
public class MenuBarController {

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private MenuBar menuBar;
	@FXML private Menu fileMenu;
	@FXML private MenuItem newMenuItem;
	@FXML private MenuItem openMenuItem;
	@FXML private Menu openRecentMenu;
	@FXML private MenuItem saveMenuItem;
	@FXML private MenuItem saveAsMenuItem;
	@FXML private MenuItem exitMenuItem;
	@FXML private Menu helpMenu;
	@FXML private MenuItem aboutMenuItem;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	@Inject private GlobalUserPreferences userPreferences;
	private ShaderSlothController shaderSlothController;

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	public void initAppController(ShaderSlothController shaderSlothController) {
		if (this.shaderSlothController != null) {
			throw new IllegalStateException("AppController already assigned");
		}
		this.shaderSlothController = shaderSlothController;
	}

	public void initialize() {
		userPreferences.readFromJavaPreference();

		newMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_NEW));
		newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

		openMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_OPEN));
		openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

		openRecentMenu.setOnShown((e) -> updateRecentMenu());

		saveMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_SAVE));
		saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

		saveAsMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_SAVE_AS));
		saveAsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));

		exitMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_QUIT));
		aboutMenuItem.setUserData(new ApplicationMenuItemController(ApplicationAction.ACTION_ABOUT));

		menuBar.getMenus().forEach(this::setupMenuHandlers);

		updateRecentMenu();

	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void setupMenuHandlers(final MenuItem menuItem) {
		if (menuItem instanceof Menu) {
			final Menu menu = (Menu) menuItem;
			menu.getItems().forEach(this::setupMenuHandlers);
			menu.setOnMenuValidation((e) -> {
				handleOnMenuValidation(menu);
			});
		} else {
			if (menuItem.getUserData() instanceof MenuItemController) {
				MenuItemController itemController = (MenuItemController) menuItem.getUserData();
				menuItem.setOnAction((e) -> {
					if (itemController.canPerform()) {
						itemController.perform();
					}
				});
			}
		}
	}

	private void handleOnMenuValidation(Menu menu) {
		for (MenuItem i : menu.getItems()) {
			final boolean disable, selected;
			final String title;
			if (i.getUserData() instanceof MenuItemController) {
				final MenuItemController c = (MenuItemController) i.getUserData();
				boolean canPerform;
				try {
					canPerform = c.canPerform();
				} catch(RuntimeException x) {
					canPerform = false;
					final Exception xx
						= new Exception(c.getClass().getSimpleName()
						+ ".canPerform() did break for menubar item " + i, x); //NOI18N
					xx.printStackTrace();
				}
				disable = !canPerform;
				title = c.getTitle();
				selected = c.isSelected();
			} else {
				if (i instanceof Menu) {
					disable = false;
					selected = false;
					title = null;
				} else {
					disable = true;
					selected = false;
					title = null;
				}
			}
			i.setDisable(disable);
			if (title != null) {
				i.setText(title);
			}
		}
	}

	/**
	 * Updates the entries for the recent menubar which contains
	 * projects which were opened in the past.
	 */
	private void updateRecentMenu() {
		final List<String> recentItems = userPreferences.getRecentOpenedProjects();
		final List<MenuItem> menuItems = new ArrayList<>();

		MenuItem clearRecentItem = new MenuItem(I18N.getString("menu.title.open.recent.clear"));
		clearRecentItem.setOnAction((e) -> {
			userPreferences.clearRecentOpenedProjects();
			updateRecentMenu();
		});

		if (recentItems.isEmpty()) {
			clearRecentItem.setDisable(true);
			menuItems.add(clearRecentItem);
		} else {
			final List<String> recentToRemove = new ArrayList<>();
			final Map<String, Integer> recentProjectNames = new HashMap<>();

			clearRecentItem.setDisable(false);

			// Detects if a item still exists and mark this for removal.
			// Furthermore it determined if names are unique
			for (String recentItem : recentItems) {
				// when the recentItem file is gone remove it from the pref db
				final File recentProjectFolder = new File(recentItem);
				if (recentProjectFolder.exists()) {
					String name = recentProjectFolder.getName();
					if (recentProjectNames.containsKey(name)) {
						recentProjectNames.put(name, recentProjectNames.get(name) + 1);
					} else {
						recentProjectNames.put(name, 1);
					}
				} else {
					recentToRemove.add(recentItem);
				}
			}

			// Second pass for building the menubar items
			for (String recentItem : recentItems) {
				final File recentProjectFolder = new File(recentItem);
				if (recentProjectFolder.exists()) {
					String name = recentProjectFolder.getName();
					MenuItem recentMenuItem = new MenuItem();
					recentMenuItem.setMnemonicParsing(false);
					if (recentProjectNames.get(name) > 1) {
						recentMenuItem.setText(recentProjectFolder.getAbsolutePath());
					} else {
						recentMenuItem.setText(name);
					}
					recentMenuItem.setOnAction((e) -> {
						this.shaderSlothController.performOpenRecentProject(new File(recentItem));
					});
					menuItems.add(recentMenuItem);
				}
			}

			if (! recentToRemove.isEmpty()) {
				userPreferences.removeRecentOpenedProjects(recentToRemove);
			}

			menuItems.add(new SeparatorMenuItem());
			menuItems.add(clearRecentItem);
		}
		openRecentMenu.getItems().setAll(menuItems);
	}

	/******************************************************************************
	 *
	 * Inner Classes
	 *
	 ******************************************************************************/

	class ApplicationMenuItemController extends MenuItemController {

		private ApplicationAction action;

		public ApplicationMenuItemController(ApplicationAction action) {
			this.action = action;
		}

		@Override
		public boolean canPerform() {
			return shaderSlothController.canPerformAction(action);
		}

		@Override
		public void perform() {
			shaderSlothController.performAction(action);
		}
	}

	abstract class MenuItemController {

		public abstract boolean canPerform();

		public abstract void perform();

		public String getTitle() {
			return null;
		}

		public boolean isSelected() {
			return false;
		}
	}
}
