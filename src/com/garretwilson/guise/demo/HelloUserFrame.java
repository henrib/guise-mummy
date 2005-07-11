package com.garretwilson.guise.demo;

import com.garretwilson.beans.*;
import com.garretwilson.guise.component.*;
import com.garretwilson.guise.component.layout.*;
import com.garretwilson.guise.model.ValueModel;
import com.garretwilson.guise.session.GuiseSession;
import com.garretwilson.guise.validator.RegularExpressionStringValidator;

/**Hello User Guise demonstration frame.
Copyright � 2005 GlobalMentor, Inc.
Demonstrates hidden components, text controls, control labels,
	text control regular expression validators, buttons, and model value change listeners.
@author Garret Wilson
*/
public class HelloUserFrame extends DefaultFrame
{

	/**Guise session constructor.
	@param session The Guise session that owns this frame.
	*/
	public HelloUserFrame(final GuiseSession<?> session)
	{
		super(session);	//construct the parent class
		getModel().setLabel("Guise\u2122 Demonstration: Hello User");	//set the frame title	

		final Panel panel=new Panel(session, new FlowLayout(Axis.Y));	//create a panel flowing vertically
		final Label helloUserLabel=new Label(session);	//create a label
		helloUserLabel.setVisible(false);	//don't show the label initially
		panel.add(helloUserLabel);	//add the label to the panel
		
		final TextControl<String> userInput=new TextControl<String>(session, String.class);	//create a text input control to retrieve a string
		userInput.getModel().setLabel("What's your name?");	//add a label to the text input control
		userInput.getModel().setValidator(new RegularExpressionStringValidator(session, "\\S+.*", true));	//require at least a single non-whitespace character followed by any other characters
		userInput.getModel().addPropertyChangeListener(ValueModel.VALUE_PROPERTY, new AbstractPropertyValueChangeListener<String>()
				{
					public void propertyValueChange(PropertyValueChangeEvent<String> propertyValueChangeEvent)
					{
						final String user=propertyValueChangeEvent.getNewValue();	//get the name the user entered
						helloUserLabel.getModel().setLabel("Hello, "+user+"!");	//update the label
						helloUserLabel.setVisible(true);	//make the label visible
					}
				});
		panel.add(userInput);	//add the user input control to the panel
		
		setContent(panel);	//set the panel as the frame's content
	}

}
