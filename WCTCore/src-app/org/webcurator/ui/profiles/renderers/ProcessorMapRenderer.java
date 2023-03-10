/*
 *  Copyright 2006 The National Library of New Zealand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webcurator.ui.profiles.renderers;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.ModuleAttributeInfo;
import org.webcurator.core.profiles.ComplexProfileElement;
import org.webcurator.core.profiles.HeritrixProfile;
import org.webcurator.core.profiles.ProfileElement;
import org.webcurator.core.profiles.RendererManager;

/**
 * Renders a ProcessorMap.
 * @author bbeaumont
 *
 */
public class ProcessorMapRenderer extends Renderer {

	/* (non-Javadoc)
	 * @see org.webcurator.ui.profiles.renderers.Renderer#render(org.webcurator.core.profiles.ProfileElement, javax.servlet.jsp.PageContext, org.webcurator.ui.profiles.renderers.RendererFilter)
	 */
	public void render(ProfileElement element, PageContext context, RendererFilter filter) throws IOException {
		// Get the writer.
		JspWriter out = context.getOut();
		
		out.println("<div class=\"profileMainHeading\">" + element.getName() + "</div>");
		out.println("<div class=\"profileSublevel\">");
		
		ComplexProfileElement complexElement = (ComplexProfileElement) element;
		
		// Render all the simple elements.
		List<ProfileElement> simpleChildren = complexElement.getSimpleChildren();
		if(simpleChildren.size() > 0) {
			out.println("<table>");
			for(ProfileElement p: simpleChildren) {
				out.print("<tr><td>");
				out.print(p.getName());
				out.print("</td><td>");
				RendererManager.getRenderer(p).render(p, context);
				out.println("</td></tr>");
			}
			out.println("</table>");
		}

		// Render the component to add new items.
		MapType mt = (MapType) complexElement.getValue();
		List<String> availableOptions = HeritrixProfile.getOptionsForType(mt.getContentType());
        
		// add our custom post-processor for writing the host's Ip-address to crawl.log file..
		String typeName = mt.getContentType().getName();
        String simpleName = typeName.substring(typeName.lastIndexOf(".")+1);
		if (simpleName.equalsIgnoreCase("Processor")) {
			availableOptions.add("org.archive.crawler.postprocessor.IPAddressAnnotationInserter|AnnotationInserter");
		}
		
		// Construct a list of all the items already selected.
		MBeanAttributeInfo m[] = mt.getMBeanInfo().getAttributes();
		List<String> currentOptions = new LinkedList<String>();
		for(int i=0; i< m.length; i++) {
			ModuleAttributeInfo att = (ModuleAttributeInfo)m[i];
			currentOptions.add(att.getType() + "|" + att.getName());
		}
		  
		// Remove the currently selected elements from the list.
		availableOptions.removeAll(currentOptions);
		
		out.print("<select id=\"" + element.getAbsoluteName() + ".type\">");
		
		Iterator it = availableOptions.iterator();
		
		while(it.hasNext()) {
			String type = (String) it.next();
			out.println("<option value=\"" + type + "\">" + type.substring(0, type.indexOf('|')) + "</option>");
		}
		
		out.println("</select>");
		
		out.println("<input type=\"image\" src=\"images/subtabs-add-btn.gif\" style=\"vertical-align: bottom\" onclick=\"addProcessorMapElement('"+ element.getAbsoluteName()+"');\">");

		// Render the items in the map.
		for(ProfileElement p: complexElement.getComplexChildren()) {
			out.print("<div class=\"profileMapHeading\">");
			out.print("<a href=\"javascript:mapAction('"+complexElement.getAbsoluteName()+"','"+p.getName()+"','up')\">Up</a>");
			out.print(" | ");
			out.print("<a href=\"javascript:mapAction('"+complexElement.getAbsoluteName()+"','"+p.getName()+"','down')\">Down</a>");
			out.print(" | ");
			out.print("<a href=\"javascript:mapAction('"+complexElement.getAbsoluteName()+"','"+p.getName()+"','remove')\">Remove</a>");
			out.print(" " + p.getName() + " <span class=\"className\">" + p.getType() + "</span>");
			out.println("</div>");
			
			out.println("<div class=\"profileSublevel\">");
			
			// Now print out the map contents. We do not want to render the
			// child as a single element, as it is complex and shouldn't be 
			// recursed quite the same.
			// Render all the simple elements.
			simpleChildren = ((ComplexProfileElement)p).getSimpleChildren();
			if(simpleChildren.size() > 0) {
				out.println("<table>");
				for(ProfileElement child: simpleChildren) {
					out.print("<tr><td>");
					out.print(child.getName());
					out.print("</td><td>");
					RendererManager.getRenderer(child).render(child, context);
					out.println("</td></tr>");
				}
				out.println("</table>");
			}
			
			for(ProfileElement child: ((ComplexProfileElement)p).getComplexChildren()) {
				RendererManager.getRenderer(child).render(child, context);	
			}
			
			out.println("</div>");
			
		}

		out.println("</div>");
	}

}
