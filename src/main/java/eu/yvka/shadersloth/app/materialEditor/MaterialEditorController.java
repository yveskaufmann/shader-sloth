package eu.yvka.shadersloth.app.materialEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.ShaderSlothController;
import eu.yvka.shadersloth.app.materialEditor.shaders.ShaderTemplateHelper;
import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.app.sceneEditor.PropertyEditors;
import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.shader.Shader;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanPropertyUtils;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.util.Optional;

/***
 * Controller for editing and creating materials
 */
public class MaterialEditorController extends AbstractController {


	private static final Logger Log = LoggerFactory.getLogger(MaterialEditorController.class);

	/***
	 * List cell for material is responsible to render a material in
	 * a list view.
	 */
	private class MaterialCell extends ListCell<Material> {

		private ImageView imageView;
		private TextField editTextField;

		@Override
		protected void updateItem(Material item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null || empty) {
				setText(null);
				setGraphic(null);
				return;
			}

			if (isEditing()) {
				if (editTextField != null) {
					editTextField.setText(item.getMaterialName());
					setText(null);
					setGraphic(editTextField);
				}

			} else {
				if (imageView == null) {
					Image image = new Image(App.class.getResource("images/material_icon_32x32.png").toExternalForm());
					imageView = new ImageView(image);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(16);
				}
				setText(item.getMaterialName());
				setGraphic(imageView);
			}
		}


		@Override
		public void startEdit() {
			super.startEdit();

			if (isEditing()) {
				if (editTextField == null) {
					editTextField = new TextField(getItem().getMaterialName());
					editTextField.setOnAction((event) -> {
						String newMame = editTextField.getText();
						Material mat = getItem();
						String oldName = mat.getMaterialName();
						try {
							Engine.materialManager().renameMaterial(mat, newMame);
							getRoot().fireEvent(new MaterialEvent(MaterialEvent.MATERIAL_NAME_CHANGED, mat));
							editTextField.getStyleClass().remove("invalid");
							commitEdit(mat);
						} catch (NameAlreadyInUseException e) {
							editTextField.getStyleClass().remove("invalid");
							editTextField.getStyleClass().add("invalid");
						}
						event.consume();
					});
					editTextField.setOnKeyReleased((event -> {
						if (KeyCode.ESCAPE.equals(event.getCode()) && !editTextField.getStyleClass().contains("invalid")) {
							cancelEdit();
							event.consume();
						}

					}));
				}
				setText(null);
				setGraphic(editTextField);
				editTextField.requestFocus();
				editTextField.selectAll();
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setText(getItem().getMaterialName());
			setGraphic(imageView);
		}
	}

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	/**
	 * Button for add Materials
	 */
	@FXML private Button addMaterialButton;

	/***
	 * Button for removing materials
	 */
	@FXML private Button removeMaterialButton;

	/***
	 * View for all available materials
	 */
	@FXML private ListView<Material> materialList;

	/**
	 * Shows all properties of the current selected material
	 */
	@FXML private PropertySheet materialProperties;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	/**
	 * The current selected material
	 */
	Optional<Material> currentMaterial = Optional.empty();

	/**
	 * The next material number which is used for
	 * generating atomic material ids, if
	 * no name is provided for this material
	 */
	private int nextMaterialNumber;

	/**
	 * Shader-Controller Instance
	 */
	private final ShaderSlothController shaderSlothController;

	/**
	 * The current loaded project
	 *
     */
	private  Project project;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public MaterialEditorController(ShaderSlothController controller) {
		super(App.class.getResource("view/materialEditor.fxml"));
		this.shaderSlothController = controller;
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	@Override
	protected void onFxmlLoaded() {
		materialList.setCellFactory(listView -> new MaterialCell());
		materialList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		materialList.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			if (newSelection != null && newSelection != oldSelection) {
				selectedMaterial.set(newSelection);
				readMaterialProperties(newSelection);
			}
		});

		addMaterialButton.setOnAction((event) -> {
			createNewMaterial();
		});

		removeMaterialButton.setOnAction((event) -> {
			deleteCurrentMaterial();
		});

		materialProperties.setPropertyEditorFactory(new DefaultPropertyEditorFactory() {
			@Override
			public PropertyEditor<?> call(PropertySheet.Item item) {
				PropertyEditor<?> propertyEditor = null;
				propertyEditor = super.call(item);
				if (propertyEditor == null) {
					Class<?> type = item.getType();
					if (type == Color.class) {
						propertyEditor = PropertyEditors.createColorEditor(item);
					}
				}
				return propertyEditor;
			}
		});

	}


	public void loadProject(Project project) {
		this.project = project;
		materialList.getItems().setAll(project.getMaterials());
		if (! materialList.getItems().isEmpty()) {
			materialList.getSelectionModel().selectFirst();
		}

	}

	private void readMaterialProperties(Material material) {
		try {
			materialProperties.getItems().setAll(buildMaterialPropertiesItems(material));
			materialProperties.setDisable(false);
		} catch (IntrospectionException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private ObservableList<PropertySheet.Item> buildMaterialPropertiesItems(Material material) throws IntrospectionException, NoSuchMethodException {
		ObservableList<PropertySheet.Item> properties = BeanPropertyUtils.getProperties(material.getRenderState(), propertyDescriptor -> {
			return propertyDescriptor.getWriteMethod() != null;
		});

		properties.addAll(BeanPropertyUtils.getProperties(material, (propertyDescriptor) -> {
			return (propertyDescriptor.getPropertyType().equals(Number.class)
			|| propertyDescriptor.getPropertyType().equals(Color.class)
			|| propertyDescriptor.getPropertyType().isPrimitive()) &&
				!propertyDescriptor.getPropertyType().equals(Boolean.TYPE);

		}));


		return properties;
	}



	private void createNewMaterial() {
		assert project != null;



		String name = I18N.getString("material.name.template", nextMaterialNumber++);
		while (Engine.materialManager().isMaterialNameInUse(name)) {
			name = I18N.getString("material.name.template", nextMaterialNumber++);
		}

		Shader shader = ShaderTemplateHelper.createShader(project.getProjectFolder(), name);
		Material material = new BasicMaterial(shader);
		material.setMaterialName(name);

		try {
            Engine.materialManager().registerMaterial(material);
        } catch (NameAlreadyInUseException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }

		project.getMaterials().add(material);
		materialList.getItems().add(material);
		materialList.getSelectionModel().selectLast();
		getRoot().fireEvent(new MaterialEvent(MaterialEvent.MATERIAL_CREATED, material));
	}

	private void deleteCurrentMaterial() {
		int currentMaterial = materialList.getSelectionModel().getSelectedIndex();
		if (currentMaterial != -1) {
			Material material = materialList.getItems().remove(currentMaterial);
			project.getMaterials().remove(material);
			Engine.materialManager().unregisterMaterial(material);
			getRoot().fireEvent(new MaterialEvent(MaterialEvent.MATERIAL_DELETED, material));
		}

		if (materialList.getItems().isEmpty()) {
			selectedMaterial.set(null);
		}
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<Material> selectedMaterial;
	public ReadOnlyObjectProperty<Material> selectedMaterialProperty() {
		if (selectedMaterial == null) {
			selectedMaterial = new SimpleObjectProperty<>(this, "selectedMaterialProperty");

		}
		return selectedMaterial;
	}
}
