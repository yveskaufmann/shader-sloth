package eu.yvka.shadersloth.app.materialEditor;

import eu.yvka.slothengine.material.Material;
import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

/**
 * Event which indicates creation/modification/changes of materials
 *
 * @author Yves Kaufmann
 * @since 25.07.2016
 */
public class MaterialEvent extends Event {

	/**
	 * Common supertype for all Material event types.
	 */
	public static final EventType<MaterialEvent> ANY =
		new EventType<MaterialEvent>(Event.ANY, "MATERIAL");


	/**
	 * This event occurs when a material is changed.
	 */
	public static final EventType<MaterialEvent> MATERIAL_CHANGED =
		new EventType<MaterialEvent>(MaterialEvent.ANY, "MATERIAL_CHANGED");


	/**
	 * This event occurs when a material name is changed.
	 */
	public static final EventType<MaterialEvent> MATERIAL_NAME_CHANGED =
		new EventType<MaterialEvent>(MaterialEvent.MATERIAL_CHANGED, "MATERIAL_NAME_CHANGED");

	/**
	 * This event occurs when a material is changed.
	 */
	public static final EventType<MaterialEvent> MATERIAL_CREATED =
		new EventType<MaterialEvent>(MaterialEvent.MATERIAL_CHANGED, "MATERIAL_CREATED");

	/**
	 * This event occurs when a material is changed.
	 */
	public static final EventType<MaterialEvent> MATERIAL_DELETED =
		new EventType<MaterialEvent>(MaterialEvent.MATERIAL_CHANGED, "MATERIAL_DELETED");

	private Material affectedMaterial;

	/**
	 * Create MaterialEvent with the specified event type
	 *
	 * @param eventType the specified event type
     */
	public MaterialEvent(@NamedArg("eventType") EventType<? extends Event> eventType, Material affectedMaterial) {
		super(eventType);
		this.affectedMaterial = affectedMaterial;
	}

	public Material getAffectedMaterial() {
		return affectedMaterial;
	}
}
