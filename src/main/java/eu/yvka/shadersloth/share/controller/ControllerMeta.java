package eu.yvka.shadersloth.share.controller;

import eu.yvka.shadersloth.share.actions.ActionEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Meta data about a controllers
 */
public class ControllerMeta {

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public ControllerMeta() {

	}

	/******************************************************************************
	 *
	 * Fields & Properties
	 *
	 ******************************************************************************/

	/**
	 * List of action entries of this constructor
	 */
	private ObservableList<ActionEntry> actionEntries;
	public ObservableList<ActionEntry> actionEntriesProperty() {
		if (actionEntries == null) {
			actionEntries = FXCollections.observableArrayList();
			actionEntries.sorted((o1, o2) -> {
				int p1 = o1.getPriority();
				int p2 = o2.getPriority();
				return p1 == p2 ? 0 : p1 < p2 ? -1 : 1;
			});
		}
		return actionEntries;
	}

	/**
	 * Defines the title of a controllers
	 *
	 * @defaultValue empty
	 */
	private StringProperty title;

	/**
	 * @see #title
     */
	public StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title");
		}
		return title;
	}

	/**
	 * @see #title
	 */
	public String getTitle() {
		return title == null ? "" : title.get();
	}

	/**
	 * @see #title
	 */
	public void setTitle(String title) {
		titleProperty().set(title);
	}

	/**
	 * Defines the graphic node of a controllers
	 *
	 * @defaultValue empty
	 */
	private ObjectProperty<Node> graphicNode;

	/**
	 * @see #title
	 */
	public ObjectProperty<Node> graphicNodeProperty() {
		if (graphicNode == null) {
			graphicNode = new SimpleObjectProperty<Node>(this, "graphicNode");
		}
		return graphicNode;
	}

	/**
	 * @see #title
	 */
	public Node getGraphicNode() {
		return graphicNode == null ? null : graphicNode.get();
	}

	/**
	 * @see #title
	 */
	public void setGraphicNode(Node graphicNode) {
		graphicNodeProperty().set(graphicNode);
	}
}
