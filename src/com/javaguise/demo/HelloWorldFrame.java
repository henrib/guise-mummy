package com.javaguise.demo;

import com.javaguise.component.*;
import com.javaguise.session.GuiseSession;

/**Hello World Guise demonstration frame.
Copyright � 2005 GlobalMentor, Inc.
Demonstrates frames, region layouts, and headings.
@author Garret Wilson
*/
public class HelloWorldFrame extends DefaultFrame
{

	/**Guise session constructor.
	@param session The Guise session that owns this frame.
	*/
	public HelloWorldFrame(final GuiseSession<?> session)
	{
		super(session);	//construct the parent class, defaulting to a region layout
		getModel().setLabel("Guise\u2122 Demonstration: Hello World");	//set the frame title
		
		final Heading helloWorldHeading=new Heading(session, 0);	//create a top-level heading
		helloWorldHeading.getModel().setLabel("Hello World!");	//set the text of the heading, using its model
		add(helloWorldHeading);	//add the heading to the frame in the default center
	}

}
