package net.itarray.sikuli.api.visual;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.nodes.PShadow;
import net.itarray.sikuli.api.visual.element.*;

import java.awt.*;
import java.awt.image.BufferedImage;

class PNodeFactory {

	static public PNode createFrom(Element element){
		Class<? extends Element> clazz = element.getClass();
		if (clazz == LabelElement.class){
			return createFrom((LabelElement)element);
		}else if (clazz == BoxElement.class){
			return createFrom((BoxElement) element);
		}else if (clazz == CircleElement.class){
			return createFrom((CircleElement) element);
		}else if (clazz == ImageElement.class){
			return createFrom((ImageElement) element);
		}else if (clazz == DotElement.class){
			return createFrom((DotElement) element);
		}else if (clazz == RefreshableImageElement.class){
			return createFrom((RefreshableImageElement) element);
		}
		return new PNode();
	}

	static public PNode createFrom(LabelElement element){
		final PText txt = new PText(element.getText());
		txt.setTextPaint(Color.black);
		txt.setPaint(element.getBackgroundColor());
		txt.setTextPaint(element.getColor());
		txt.setFont(txt.getFont().deriveFont(element.getFontSize()));

		PNode labelNode = new PNode();
		labelNode.setPaint(element.getBackgroundColor());		
		labelNode.addChild(txt);
		labelNode.setHeight(txt.getHeight()+2);
		labelNode.setWidth(txt.getWidth()+4);
		txt.setOffset(2,1);
		
		element.addListener(new LabelElement.Listener(){
			@Override
			public void textUpdated(String newText){
				txt.setText(newText);
				txt.repaint();
//				applyAlignment(labelNode, element);
				//p.repaint();//invalidateFullBounds();
			}			
		});
		
		applyAlignment(labelNode, element);		
		return applyTransparencyAndShadow(labelNode, element);
	}	
	
	static public PNode createFrom(DotElement element){
		PPath p = PPath.createEllipse(0,0,4,4);
		p.setStrokePaint(element.getColor());
		p.setPaint(element.getColor());		
		p.setStroke(new BasicStroke(element.getLineWidth()));
		
		final PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight());
		foregroundNode.setWidth(p.getWidth());
		foregroundNode.setOffset(element.x-2, element.y-2);

		final PNode node = applyTransparencyAndShadow(foregroundNode, element);
		return node;
	}
	
	static public PNode createFrom(CircleElement element){
		PPath p = PPath.createEllipse(0,0,element.width,element.height);
		p.setStrokePaint(element.getLineColor());
		p.setPaint(null);		
		p.setStroke(new BasicStroke(element.getLineWidth()));

		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight());
		foregroundNode.setWidth(p.getWidth());		
		foregroundNode.setOffset(element.x, element.y);

		return applyTransparencyAndShadow(foregroundNode, element);
	}

	static public PNode createFrom(BoxElement element){
		PPath p = PPath.createRectangle(0,0,element.width,element.height);
		p.setStrokePaint(element.getLineColor());
		p.setPaint(element.getBackgroundColor());		
		p.setStroke(new BasicStroke(element.getLineWidth()));

		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight());
		foregroundNode.setWidth(p.getWidth());
		foregroundNode.setOffset(element.x, element.y);

		return applyTransparencyAndShadow(foregroundNode, element);
	}
	
//	static public PNode createFrom(ImageElement element){
//		PImage p = new PImage(element.getImage());
//
//		PNode foregroundNode = new PNode();
//		foregroundNode.addChild(p);
//		foregroundNode.setHeight(p.getHeight());
//		foregroundNode.setWidth(p.getWidth());
//		foregroundNode.setOffset(element.x, element.y);
//
//		applyAlignment(foregroundNode, element);		
//		return applyTransparencyAndShadow(foregroundNode, element);
//	}
	
	static public PNode createFrom(ImageElement element){
		final PImage p = new PImage(element.getImage());

		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight());
		foregroundNode.setWidth(p.getWidth());
		foregroundNode.setOffset(element.x, element.y);
		
		element.addListener(new ImageElement.Listener(){
			@Override
			public void imageUpdated(BufferedImage newImage){
				p.setImage(newImage);
				p.repaint();//invalidateFullBounds();
			}			
		});

		applyAlignment(foregroundNode, element);
		return foregroundNode;//(foregroundNode, element);
	}

	static private void applyAlignment(PNode node, Element element){
		double width = node.getWidth();
		double height = node.getHeight();
		
		double x = element.x; 
		double y = element.y;
		
		if (element.verticalAlignment == Element.VerticalAlignment.TOP){
			y = element.y;
		}else if (element.verticalAlignment == Element.VerticalAlignment.MIDDLE){
			y = element.y - height/2;		
		}else if (element.verticalAlignment == Element.VerticalAlignment.BOTTOM){
			y = element.y - height;
		}
		
		if (element.horizontalAlignment == Element.HorizontalAlignment.RIGHT){
			x = element.x - width;
		}else if (element.horizontalAlignment == Element.HorizontalAlignment.LEFT){
			x = element.x;
		}else if (element.horizontalAlignment == Element.HorizontalAlignment.CENTER){
			x = element.x - width / 2;
		}
		node.setOffset(x,y);
	}

	static private PNode applyTransparencyAndShadow(PNode node, Element element){
		PNode shadowedNode = addShadow(node);		
		shadowedNode.setTransparency(element.getTransparency());
		return shadowedNode;
	}

	
	static private final Color SHADOW_PAINT = new Color(10, 10, 10, 255);
	static private PNode addShadow(PNode contentNode){

		PNode contentNodeWithShadow = new PNode();

		double xoffset = contentNode.getXOffset();
		double yoffset = contentNode.getYOffset();

		int blurRadius = 4;
		int tx = 5;
		int ty = 5;

		PShadow shadowNode = new PShadow(contentNode.toImage(), SHADOW_PAINT, blurRadius );		
		contentNode.setOffset(tx, ty);
		shadowNode.setOffset(tx - (2 * blurRadius) + 1.0d, ty - (2 * blurRadius) + 1.0d);	
		contentNodeWithShadow.addChild(shadowNode);
		contentNodeWithShadow.addChild(contentNode);		      
//		contentNodeWithShadow.setOffset(xoffset - tx  - blurRadius, yoffset - ty - blurRadius);
		contentNodeWithShadow.setOffset(xoffset - tx, yoffset - ty);
		contentNodeWithShadow.setBounds(0,0, contentNode.getWidth() + 2*blurRadius + tx, contentNode.getHeight() + 2*blurRadius + ty);
		return contentNodeWithShadow;
	}
}