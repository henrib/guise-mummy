package com.guiseframework.component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.Collections.*;

import com.guiseframework.GuiseSession;
import com.guiseframework.component.layout.*;
import com.guiseframework.model.ValueModel;

/**Abstract implementation of a frame for communication of an option.
An option frame defaults to a single composite child panel with a row of options along the bottom.
The contents of an option dialog frame should be accessed by {@link #getOptionContent()} and {@link #setOptionContent(Component)}.
This implementation does not allow its frame content to be changed.
@param <O> The type of options available.
@author Garret Wilson
*/
public abstract class AbstractOptionDialogFrame<O, C extends OptionDialogFrame<O, C>> extends AbstractDialogFrame<O, C> implements OptionDialogFrame<O, C>
{

	/**Sets the single child component.
	This implementation throws an exception because the frame content is not allowed to be changed.
	@param newContent The single child component, or <code>null</code> if this frame does not have a child component.
	@exception IllegalArgumentException if any different content is provided.
	*/
	public void setContent(final Component<?> newContent)
	{
		if(newContent!=getContent())	//if the content is changing
		{
			throw new IllegalArgumentException("Option dialog frame content cannot be changed.");
		}
	}

	/**@return The container component used to hold content, including the option child component.*/
	protected Container<?> getContentContainer() {return (Container<?>)super.getContent();}

	/**@return The component representing option contents, or <code>null</code> if this frame does not have an option contents component.*/ 
	public Component<?> getOptionContent()
	{
		return ((RegionLayout)getContentContainer().getLayout()).getComponent(Region.CENTER);	//return the center component, if there is one
	}

	/**Sets the component representing option contents.
	This implementation adds the option content component to the center region of the child container.
	@param newOptionContent The single option contents component, or <code>null</code> if this frame does not have an option contents component.
	*/
	public void setOptionContent(final Component<?> newOptionContent)
	{
		final Component<?> oldOptionContents=getOptionContent();	//get the current component
		if(oldOptionContents!=newOptionContent)	//if the value is really changing
		{
			final Container<?> contentsContainer=getContentContainer();	//get our container
			if(oldOptionContents!=null)	//if an old content component was present
			{
				contentsContainer.remove(oldOptionContents);	//remove the old component
			}
			if(newOptionContent!=null)	//if a new content component is given
			{
				contentsContainer.add(newOptionContent, new RegionConstraints(getSession(), Region.CENTER));	//add the component to the center of the container
			}
		}
	}

	/**The container containing the options.*/
	private final Container<?> optionContainer;

		/**@return The container containing the options.*/
		public Container<?> getOptionContainer() {return optionContainer;}

	/**The read-only list of available options in order.*/
	private final List<O> options;

		/**@return The read-only list of available options in order.*/
		public List<O> getOptions() {return options;}

	/**The map of components representing options.*/
	private final Map<O, Component<?>> optionComponentMap=new ConcurrentHashMap<O, Component<?>>();

		/**Returns the component that represents the specified option.
		@param option The option for which a component should be returned.
		@return The component, such as a button, that represents the given option, or <code>null</code> if there is no component that represents the given option.
		*/
		public Component<?> getOptionComponent(final O option) {return optionComponentMap.get(option);}
		
	/**Session, ID, model, component, and options constructor.
	Duplicate options are ignored.
	@param session The Guise session that owns this component.
	@param id The component identifier, or <code>null</code> if a default component identifier should be generated.
	@param model The component data model.
	@param component The component representing the content of the option dialog frame, or <code>null</code> if there is no content component.
	@param options The available options.
	@exception NullPointerException if the given session, model, and/or options is <code>null</code>.
	@exception IllegalArgumentException if the given identifier is not a valid component identifier.
	*/
	public AbstractOptionDialogFrame(final GuiseSession session, final String id, final ValueModel<O> model, final Component<?> component, final O... options)
	{
		super(session, id, model, new LayoutPanel(session, new RegionLayout(session)));	//construct the parent class using a layout panel as a container
		final List<O> optionList=new ArrayList<O>();	//create a list of options
		for(final O option:options)	//put all the options in the list without duplicates
		{
			if(!optionList.contains(option))	//if this option isn't already in the list
			{
				optionList.add(option);	//add this option to the list
			}
		}
		this.options=unmodifiableList(optionList);	//save the list of options without duplicates
		setOptionContent(component);	//set the component, if there is one
		optionContainer=createOptionContainer();	//create the option container
		getContentContainer().add(optionContainer, new RegionConstraints(session, Region.PAGE_END));	//add the option container at the bottom
		initializeOptionContainer(optionContainer, this.options);	//initialize the option container
	}

	/**Creates a container for holding the options.
	This implementation creates a horizontal layout panel.
	@return a container for holding the options.
	*/
	protected Container<?> createOptionContainer()
	{
		return new LayoutPanel(getSession(), new FlowLayout(getSession(), Flow.LINE));	//create a horizontal layout panel
	}

	/**Initializes the option container with the available options.
	Each component is added to the option container and to the map of option components.
	@param optionContainer The container to the options.
	@param options The available options.
	*/
	protected void initializeOptionContainer(final Container<?> optionContainer, final List<O> options)
	{
		final GuiseSession session=getSession();	//get the session
		for(final O option:options)	//for each option
		{
			final Component<?> optionComponent=createOptionComponent(option);	//create a component for this option
			optionComponentMap.put(option, optionComponent);	//store this component in the map keyed to the component
			optionContainer.add(optionComponent);	//add the component to the container
		}
	}

	/**Creates a component to represent the given option.
	@param option The option for which a component should be created.
	*/
	protected abstract Component<?> createOptionComponent(final O option);
}
