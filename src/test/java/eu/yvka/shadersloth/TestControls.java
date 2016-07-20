package eu.yvka.shadersloth;

import eu.yvka.slothengine.engine.AppSettings;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.light.AmbientLight;
import eu.yvka.shadersloth.app.sceneEditor.SceneTreeCell;
import eu.yvka.shadersloth.app.sceneEditor.GeometryCreateDialog;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.yvka.shadersloth.app.controls.NumberInput;

public class TestControls extends Application {

	private final static Logger Log = LoggerFactory.getLogger(TestControls.class);

	private TreeView<Node> treeView = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws NoSuchMethodException {

		Engine.start(new AppSettings());

		VBox vBox = initializeScene(primaryStage);
		Scene scene = new Scene(vBox, -1, -1, true, SceneAntialiasing.BALANCED);
		primaryStage.setTitle("ShaderSlothRenderer");
		primaryStage.setMinWidth(1024);
		primaryStage.setHeight(768);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest((event -> {
			Engine.shutdown();
		}));
	}

	private static TreeItem<Node> draggedTreeItem;

	private VBox initializeScene(Stage stage) {
		VBox vBox = new VBox();
		vBox.getChildren().addAll(new NumberInput("X", 10), new NumberInput("Y", 10, 0, 20), new Slider());

		Menu newMenu = new Menu("New");
		MenuItem newModel = new MenuItem("Model");
		newMenu.setOnAction((event -> {
			GeometryCreateDialog dialog = new GeometryCreateDialog();
			dialog.initOwner(stage);
			dialog.showAndWait();

		}));
		newMenu.getItems().add(newModel);
		newMenu.getItems().add(new MenuItem("Light"));
		newMenu.getItems().add(new MenuItem("Geometry"));

		ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(newMenu);


		treeView = new TreeView<>();
		treeView.setContextMenu(contextMenu);

		treeView.setCellFactory(new Callback<TreeView<Node>, TreeCell<Node>>() {

			@Override
			public TreeCell<Node> call(TreeView<Node> param) {

				TreeCell<Node> treeCell = new SceneTreeCell();

				treeCell.setOnDragDetected(event -> {

					if (treeCell.isEmpty()) return;

					ClipboardContent content = new ClipboardContent();
					content.putString("TROLOLOL");

					Dragboard dragboard = treeView.startDragAndDrop(TransferMode.MOVE);
					dragboard.setContent(content);

					draggedTreeItem = treeCell.getTreeItem();

					event.consume();
                });

				treeCell.setOnDragOver(event -> {
					Point2D sceneCoordinates = treeCell.localToScene(0d, 0d);

					double height = treeCell.getHeight();

					// get the y coordinate within the control
					double y = event.getSceneY() - (sceneCoordinates.getY());

					// if the drop is three quarters of the way down the control
					// then the drop will be a sibling and not into the tree item

					// set the dnd effect for the required action
					if (y > (height * .75d)) {
						event.acceptTransferModes(TransferMode.LINK);
					} else {

					}

					event.acceptTransferModes(TransferMode.MOVE);


				});

				treeCell.setOnDragDropped(event -> {
					if (draggedTreeItem != null) {
						TreeItem<Node> parentDragged = draggedTreeItem.getParent();
						Node nodeValue = draggedTreeItem.getValue();

						if (parentDragged != draggedTreeItem) {

							parentDragged.getChildren().remove(draggedTreeItem);
							treeCell.getTreeItem().getChildren().add(draggedTreeItem);
							treeCell.getTreeItem().setExpanded(true);
						}
					}

					event.setDropCompleted(true);
					event.consume();
				});

				treeCell.setOnDragEntered(event -> {
					if (treeCell.isEmpty()) return;
					treeCell.setUnderline(true);
					treeCell.setEffect(new Glow());
					treeCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, new CornerRadii(10), BorderWidths.DEFAULT)));
					event.consume();
				});

				treeCell.setOnDragExited(event -> {
					if (treeCell.isEmpty()) return;
					treeCell.setUnderline(false);
					treeCell.setEffect(null);
					treeCell.setBorder(null);
					event.consume();
				});

				return treeCell;
			}
		});

		eu.yvka.slothengine.scene.Scene scene = new eu.yvka.slothengine.scene.Scene();
		Node root = new Node("My Project");
		scene.setRootNode(root);
		TreeItem<Node> rootItem = new TreeItem<>(root);
		rootItem.setExpanded(true);
		treeView.setRoot(rootItem);
		for (int i = 0; i < 10; i++) {
			Node child = null;

			if (i % 3 == 0) {
				child = new Node("Child " + i);
			} else if (i % 3 == 1)  {
				child = new Geometry("Child " + i);
			} else if (i % 3 == 2)  {
				child = new AmbientLight("Light1" + i);
			};


			root.addChild(child);
			TreeItem<Node> item = new TreeItem<>(child);
			item.setExpanded(false);
			rootItem.getChildren().add(item);
		}
		vBox.getChildren().add(treeView);
		return vBox;
	}

}
