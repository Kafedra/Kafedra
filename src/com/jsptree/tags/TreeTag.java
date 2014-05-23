
package com.jsptree.tags;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ru.guap.treeview.BurdenManager;

import com.jsptree.bean.Node;


public class TreeTag extends SimpleTagSupport {
	private Properties properties = new Properties();
	private Boolean isauthum;

	public void setIsauthum(Boolean val) {
		this.isauthum = val;
	}
	
	// This method will invoke when tree structure called from JSP page
	public void doTag() throws IOException, JspException {
		try {
			initProperties();

			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init(properties);

			Template velocityTemplate = velocityEngine.getTemplate(properties.getProperty("apache.velocity.template.name"));
			VelocityContext context = new VelocityContext();

			Node root = BurdenManager.getInstance().getRootNode(isauthum);
			context.put("node", root);
			
			StringWriter writer = new StringWriter();
			velocityTemplate.merge(context, writer);
			JspWriter out = getJspContext().getOut();
			out.print(writer.toString());
			
		} catch (Exception e) {
			reportError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void reportError(String error) {
		// Initializing the Apache Velocity engine
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init(properties);
		
		// Loading the Apache Velocity template
		System.out.println("template: " + properties.getProperty("apache.velocity.template.name"));
		Template velocityTemplate = velocityEngine.getTemplate(properties.getProperty("apache.velocity.template.name"));
		VelocityContext context = new VelocityContext();
		Node root = new Node("root");
		
		root.setChildren(new ArrayList<Node>());
		root.setErrorDescription(error);
		
		context.put("node", root);
		
		StringWriter writer = new StringWriter();
		velocityTemplate.merge(context, writer);
		
		JspWriter out = getJspContext().getOut();
		
		try {
			out.print(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void initProperties() {
		try (InputStream propertyInputStream = this.getClass().getResourceAsStream("config.properties")) {
			properties.load(propertyInputStream);
			properties.setProperty(properties.getProperty("key.resource.loader"), properties.getProperty("value.resource.loader.location"));
			properties.setProperty(properties.getProperty("key.class.resource.loader"), properties.getProperty("value.class.resource.loader"));	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}