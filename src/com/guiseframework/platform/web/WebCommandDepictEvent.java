package com.guiseframework.platform.web;

import java.util.*;
import static java.util.Collections.*;

import static com.globalmentor.java.Objects.*;
import static com.globalmentor.util.Maps.*;

import com.globalmentor.util.NameValuePair;
import com.guiseframework.platform.DepictedObject;

/**A command to or from a depicted object on the web platform.
@param <C> The type of command.
@author Garret Wilson
Copyright (c) 2007 GlobalMentor, Inc.
*/
public class WebCommandDepictEvent<C extends Enum<C> & WebPlatformCommand> extends AbstractWebDepictEvent implements WebPlatformCommandMessage<C>
{

	/**The command.*/
	private final WebPlatformCommand command;

		/**@return The command.*/
		@SuppressWarnings("unchecked")
		public C getCommand() {return (C)command;}

	/**The read-only map of parameters, which will be encoded in JavaScript Object Notation (JSON).*/
	private final Map<String, Object> parameters;

		/**@return The read-only map of parameters, which will be encoded in JavaScript Object Notation (JSON).*/
		public Map<String, Object> getParameters() {return parameters;}

	/**Depicted object, command, and parameters constructor.
	@param depictedObject The depicted object on which the event initially occurred.
	@param command The command.
	@param parameters The parameters of the command; parameters with duplicate names replace earlier parameters of the same name.
	@exception NullPointerException if the given depicted object, command, and/or parameters is <code>null</code>.
	*/
	public WebCommandDepictEvent(final DepictedObject depictedObject, final C command, final NameValuePair<String, Object>... parameters)
	{
		super(depictedObject);	//construct the parent class
		this.command=checkInstance(command, "Command cannot be null.");
		this.parameters=unmodifiableMap(addAll(new HashMap<String, Object>(parameters.length), parameters));	//add all the parameters to a new map
	}

	/**Depicted object, command, and parameters map constructor.
	@param depictedObject The depicted object on which the event initially occurred.
	@param command The command.
	@param parameters The map representing the parameters of the command.
	@exception NullPointerException if the given depicted object, command, and/or parameters is <code>null</code>.
	*/
	public WebCommandDepictEvent(final DepictedObject depictedObject, final C command, final Map<String, Object> parameters)
	{
		super(depictedObject);	//construct the parent class
		this.command=checkInstance(command, "Command cannot be null.");
		this.parameters=unmodifiableMap(new HashMap<String, Object>(checkInstance(parameters, "Parameters cannot be null.")));
	}

}