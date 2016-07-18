package eu.yvka.shadersloth.share.utils;


import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;


public class NodeUtils {

	public static void centerNode(Node node) {
		Parent parent = node.getParent();
		centerNode(node, parent);
	}

	public static void centerNode(Node node, Node parent) {
		Bounds nodeBounds = node.getLayoutBounds();
		Bounds nodeParent = parent.getLayoutBounds();

		if (parent != null) {
			node.setTranslateX((nodeParent.getWidth() - nodeBounds.getWidth()) * 0.5);
			node.setTranslateY((nodeParent.getHeight() - nodeBounds.getHeight()) * 0.5);
		}
	}

	public static double computeXOffset(double width, double contentWidth, HPos hpos) {
		if (hpos == null) {
			return 0;
		}

		switch(hpos) {
			case LEFT:
				return 0;
			case CENTER:
				return (width - contentWidth) / 2;
			case RIGHT:
				return width - contentWidth;
			default:
				return 0;
		}
	}

	public static double computeYOffset(double height, double contentHeight, VPos vpos) {
		if (vpos == null) {
			return 0;
		}

		switch(vpos) {
			case TOP:
				return 0;
			case CENTER:
				return (height - contentHeight) / 2;
			case BOTTOM:
				return height - contentHeight;
			default:
				return 0;
		}
	}

	public static double computeTextWidth(Font font , String text, double wrappingWidth) {
		Text textLayout = new Text();
		textLayout.setWrappingWidth(wrappingWidth);
		textLayout.setFont(font);
		textLayout.setText(text);
		return textLayout.getLayoutBounds().getWidth();
	}

	public static double computeTextHeight(Font font, String text, double wrappingWidth, TextBoundsType boundsType) {
		return computeTextHeight(font, text, wrappingWidth, 0, boundsType);
	}

	public static double computeTextHeight(Font font, String text, double wrappingWidth, double lineSpacing, TextBoundsType boundsType) {
		return computeTextHeight(font, text, wrappingWidth, lineSpacing, boundsType, null);
	}

	public static double computeTextHeight(Font font, String text, double wrappingWidth, double lineSpacing, TextBoundsType boundsType, String style) {

		if (style == null) style = "";

		Text textLayout = new Text();
		textLayout.setText(text);
		textLayout.setWrappingWidth(wrappingWidth);
		textLayout.setLineSpacing(lineSpacing);
		textLayout.setBoundsType(boundsType);
		textLayout.setStyle(style);

		return textLayout.getLayoutBounds().getHeight();
	}

	public static final void printSizeInfo(final Region node) {
		printSizeInfo(node, null);
	}

	public static final void printSizeInfo(final Region node, String alternativeName) {

		Supplier<String> nameSupplier = () -> {

			String name = alternativeName == null? node.getId() : alternativeName;
			if (name == null) {
				name = node.getClass().getSimpleName();
			}
			return name;
		};

		final String nodeName = nameSupplier.get();

		List<String> sizeProperties = Arrays.asList("Width", "Height");
		List<String> sizeAttributes = Arrays.asList("Min", "Pref", "Max");

		/* Print all size information about a node */
		sizeAttributes.stream()
			.flatMap((attr) -> sizeProperties.stream().map((prop) -> "get" + attr + prop ))
			.forEach((method) -> {
				try {
					System.out.println(nodeName + "." + method + "() = " + ReflectionUtils.invokeMethod(node, method));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			});

		System.out.println(nodeName + ".getLayoutBounds() = " + node.getLayoutBounds());
	}
}
