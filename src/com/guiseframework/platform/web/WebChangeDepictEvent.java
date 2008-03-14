package com.guiseframework.platform.web;

import java.util.*;
import static java.util.Collections.*;

import static com.globalmentor.java.Objects.*;
import static com.globalmentor.util.Maps.*;


import com.globalmentor.javascript.JSON;
import com.globalmentor.util.NameValuePair;
import com.guiseframework.platform.DepictedObject;

/**Indicates that one or more properties of a depicted object have changed on the web platform.
Each property value can be an object a {@link List}, or a {@link Map} of other properties.
@author Garret Wilson
*/
public class WebChangeDepictEvent extends AbstractWebDepictEvent
{

	/**The read-only map of properties.*/
	private final Map<String, Object> properties;

		/**@return The read-only map of properties.*/
		public Map<String, Object> getProperties() {return properties;}

	/**Depicted object and properties constructor.
	@param depictedObject The depicted object on which the event initially occurred.
	@param command The command.
	@param properties The relevant properties; properties with duplicate names replace earlier properties of the same name.
	@exception NullPointerException if the given depicted object and/or properties is <code>null</code>.
	*/
	public WebChangeDepictEvent(final DepictedObject depictedObject, final NameValuePair<String, Object>... properties)
	{
		super(depictedObject);	//construct the parent class
		this.properties=unmodifiableMap(addAll(new HashMap<String, Object>(properties.length), properties));	//add all the properties to a new map
	}

	/**Depicted object and properties map constructor.
	@param depictedObject The depicted object on which the event initially occurred.
	@param command The command.
	@param properties The map representing the relevant properties.
	@exception NullPointerException if the given depicted object and/or properties is <code>null</code>.
	*/
	public WebChangeDepictEvent(final DepictedObject depictedObject, final Map<String, Object> properties)
	{
		super(depictedObject);	//construct the parent class
		this.properties=unmodifiableMap(new HashMap<String, Object>(checkInstance(properties, "Properties cannot be null.")));
	}

	/**@return A string representation of this event.*/
	public String toString()
	{
		return super.toString()+JSON.serialize(getProperties());
	}
}