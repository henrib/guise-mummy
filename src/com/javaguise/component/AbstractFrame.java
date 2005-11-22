package com.javaguise.component;

import java.beans.PropertyChangeListener;
import java.util.Iterator;

import static com.garretwilson.lang.ObjectUtilities.*;

import com.garretwilson.util.Debug;
import com.garretwilson.util.EmptyIterator;
import com.garretwilson.util.ObjectIterator;
import com.javaguise.event.GuisePropertyChangeListener;
import com.javaguise.model.LabelModel;
import com.javaguise.session.GuiseSession;

/**Abstract implementation of a frame.
@author Garret Wilson
@see LayoutPanel
*/
public abstract class AbstractFrame<C extends Frame<C>> extends AbstractComponent<C> implements Frame<C>
{

	/**The default mode of an open, modal frame.*/
	public final static Mode DEFAULT_MODAL_FRAME_MODE=new Mode(){};

	/**The state of the frame.*/
	private State state=State.CLOSED;

		/**@return The state of the frame.*/
		public State getState() {return state;}

		/**Sets the state of the frame.
		This is a bound property.
		@param newState The new state of the frame.
		@exception NullPointerException if the given state is <code>null</code>.
		@see Frame#STATE_PROPERTY 
		*/
		protected void setState(final State newState)
		{
			if(state!=checkNull(newState, "State cannot be null."))	//if the value is really changing
			{
				final State oldState=state;	//get the old value
				state=newState;	//actually change the value
				firePropertyChange(STATE_PROPERTY, oldState, newState);	//indicate that the value changed
				setMode(isModal() && newState!=State.CLOSED ? DEFAULT_MODAL_FRAME_MODE : null);	//set the modal mode if we are open and modal
			}			
		}

	/**Whether the frame is modal if and when it is open.*/
	private boolean modal=false;

		/**@return Whether the frame is modal if and when it is open.*/
		public boolean isModal() {return modal;}

		/**Sets whether the frame is modal if and when it is open.
		This is a bound property of type <code>Boolean</code>.
		@param newModal <code>true</code> if the frame should be modal, else <code>false</code>.
		@see Frame#MODAL_PROPERTY
		*/
		public void setModal(final boolean newModal)
		{
			if(modal!=newModal)	//if the value is really changing
			{
				final boolean oldModal=modal;	//get the current value
				modal=newModal;	//update the value
				firePropertyChange(MODAL_PROPERTY, Boolean.valueOf(oldModal), Boolean.valueOf(newModal));
				setMode(newModal && getState()!=State.CLOSED ? DEFAULT_MODAL_FRAME_MODE : null);	//set the modal mode if we are open and modal
			}
		}

	/**The current mode of interaction, or <code>null</code> if the component is in a modeless state.*/
	private Mode mode=null;

		/**@return The current mode of interaction, or <code>null</code> if the component is in a modeless state.*/
		public Mode getMode() {return mode;}

		/**Sets the mode of interaction.
		This is a bound property.
		@param newMode The new mode of component interaction.
		@see ModalComponent#MODE_PROPERTY 
		*/
		public void setMode(final Mode newMode)
		{
			if(mode!=newMode)	//if the value is really changing
			{
				final Mode oldMode=mode;	//get the old value
				mode=newMode;	//actually change the value
				firePropertyChange(MODE_PROPERTY, oldMode, newMode);	//indicate that the value changed
			}			
		}

	/**Whether the frame is movable.*/
	private boolean movable=true;

		/**@return Whether the frame is movable.*/
		public boolean isMovable() {return movable;}

		/**Sets whether the frame is movable.
		This is a bound property of type <code>Boolean</code>.
		@param newMovable <code>true</code> if the frame should be movable, else <code>false</code>.
		@see Frame#MOVABLE_PROPERTY
		*/
		public void setMovable(final boolean newMovable)
		{
			if(movable!=newMovable)	//if the value is really changing
			{
				final boolean oldMovable=movable;	//get the current value
				movable=newMovable;	//update the value
				firePropertyChange(MOVABLE_PROPERTY, Boolean.valueOf(oldMovable), Boolean.valueOf(newMovable));
			}
		}

	/**Whether the frame can be resized.*/
	private boolean resizable=true;

		/**@return Whether the frame can be resized.*/
		public boolean isResizable() {return resizable;}

		/**Sets whether the frame can be resized.
		This is a bound property of type <code>Boolean</code>.
		@param newResizable <code>true</code> if the frame can be resized, else <code>false</code>.
		@see Frame#RESIZABLE_PROPERTY
		*/
		public void setResizable(final boolean newResizable)
		{
			if(resizable!=newResizable)	//if the value is really changing
			{
				final boolean oldResizable=resizable;	//get the current value
				resizable=newResizable;	//update the value
				firePropertyChange(MOVABLE_PROPERTY, Boolean.valueOf(oldResizable), Boolean.valueOf(newResizable));
			}
		}

	/**The related component such as a popup source, or <code>null</code> if the frame is not related to another component.*/
	private Component<?> relatedComponent=null;

		/**@return The related component such as a popup source, or <code>null</code> if the frame is not related to another component.*/
		public Component<?> getRelatedComponent() {return relatedComponent;}

		/**Sets the related component
		This is a bound property.
		@param newRelatedComponent The new related component, or <code>null</code> if the frame is not related to another component.
		@see Frame#RELATED_COMPONENT_PROPERTY 
		*/
		public void setRelatedComponent(final Component<?> newRelatedComponent)
		{
			if(relatedComponent!=newRelatedComponent)	//if the value is really changing
			{
				final Component<?> oldRelatedComponent=relatedComponent;	//get the old value
				relatedComponent=newRelatedComponent;	//actually change the value
				firePropertyChange(RELATED_COMPONENT_PROPERTY, oldRelatedComponent, newRelatedComponent);	//indicate that the value changed
			}			
		}

	/**Whether the title bar is visible.*/
	private boolean titleVisible=true;

		/**@return Whether the title bar is visible.*/
		public boolean isTitleVisible() {return titleVisible;}

		/**Sets whether the title bar is visible.
		This is a bound property of type <code>Boolean</code>.
		@param newTitleVisible <code>true</code> if the title bar should be visible, else <code>false</code>.
		@see Frame#TITLE_VISIBLE_PROPERTY
		*/
		public void setTitleVisible(final boolean newTitleVisible)
		{
			if(titleVisible!=newTitleVisible)	//if the value is really changing
			{
				final boolean oldTitleVisible=titleVisible;	//get the current value
				titleVisible=newTitleVisible;	//update the value
				firePropertyChange(TITLE_VISIBLE_PROPERTY, Boolean.valueOf(oldTitleVisible), Boolean.valueOf(newTitleVisible));
			}
		}

	/**@return The data model used by this component.*/
	public LabelModel getModel() {return (LabelModel)super.getModel();}

	/**The single child component, or <code>null</code> if this frame does not have a child component.*/
	private Component<?> content;

		/**@return The single child component, or <code>null</code> if this frame does not have a child component.*/
		public Component<?> getContent() {return content;}

		/**Sets the single child component.
		This is a bound property.
		@param newContent The single child component, or <code>null</code> if this frame does not have a child component.
		@see Frame#CONTENT_PROPERTY
		*/
		public void setContent(final Component<?> newContent)
		{
			if(content!=newContent)	//if the value is really changing
			{
				final Component<?> oldComponent=content;	//get the old value
				content=newContent;	//actually change the value
				if(oldComponent!=null)	//if there was an old component
				{
					oldComponent.setParent(null);	//tell the old component it no longer has a parent
				}
				if(content!=null)	//if there is a new component
				{
					content.setParent(this);	//tell the new component who its parent is					
				}
				firePropertyChange(CONTENT_PROPERTY, oldComponent, newContent);	//indicate that the value changed
			}
		}

	/**@return Whether this component has children.*/
	public boolean hasChildren() {return getContent()!=null;}

	/**Retrieves the child component with the given ID.
	@param id The ID of the component to return.
	@return The child component with the given ID, or <code>null</code> if there is no child component with the given ID. 
	*/
	public Component<?> getComponent(final String id)
	{
		final Component<?> component=getContent();	//get the child component, if there is one
		return (component!=null && id.equals(component.getID())) ? component : null;	//return the child component if it has the correct ID
	}

	/**@return An iterator to the single child component, if there is one.*/
	public Iterator<Component<?>> iterator()
	{
		final Component<?> component=getContent();	//get the child component, if there is one
		return component!=null ? new ObjectIterator<Component<?>>(getContent()) : new EmptyIterator<Component<?>>();
	}

	/**Session, ID, model, and component constructor.
	@param session The Guise session that owns this component.
	@param id The component identifier, or <code>null</code> if a default component identifier should be generated.
	@param model The component data model.
	@param component The single child component, or <code>null</code> if this frame should have no child component.
	@exception NullPointerException if the given session and/or model is <code>null</code>.
	@exception IllegalArgumentException if the given identifier is not a valid component identifier.
	*/
	public AbstractFrame(final GuiseSession session, final String id, final LabelModel model, final Component<?> component)
	{
		super(session, id, model);	//construct the parent class
		this.content=component;	//set the child component
	}

	/**Determines whether the models of this component and all of its child components are valid.
	This version returns <code>true</code> if all its child components are valid.
	@return Whether the models of this component and all of its child components are valid.
	*/
	public boolean isValid()
	{
		if(!super.isValid())	//if the component doesn't pass the default checks
		{
			return false;	//this component isn't valid
		}
		for(final Component<?> childComponent:this)	//for each child component
		{
			if(!childComponent.isValid())	//if this child component isn't valid
			{
				return false;	//indicate that this component is consequently not valid
			}
		}
		return true;	//indicate that all child components are valid
	}

	/**Validates the model of this component and all child components.
	The component will be updated with error information.
	This version validates the this component and all child components.
	@exception ComponentExceptions if there was one or more validation error.
	*/
	public void validate() throws ComponentExceptions
	{
		ComponentExceptions componentExceptions=null;	//we'll store any component exceptions here and keep going
		try
		{
			super.validate();	//validate the component normally
		}
		catch(final ComponentExceptions superComponentExceptions)	//if the super version returns an error
		{
			if(componentExceptions==null)	//if this is our first component exception
			{
				componentExceptions=superComponentExceptions;	//store the exception and continue processing events with other child components
			}
			else	//if we already have component exceptions
			{
				componentExceptions.addAll(superComponentExceptions);	//add all the exceptions to the exception we already have
			}
		}
		for(final Component<?> childComponent:this)	//for each child component
		{
			try
			{
				childComponent.validate();	//validate the child
			}
			catch(final ComponentExceptions childComponentExceptions)	//if a child returns an error
			{
				if(componentExceptions==null)	//if this is our first component exception
				{
					componentExceptions=childComponentExceptions;	//store the exception and continue processing events with other child components
				}
				else	//if we already have component exceptions
				{
					componentExceptions.addAll(childComponentExceptions);	//add all the child component exceptions to the exception we already have
				}
			}
		}
		if(componentExceptions!=null)	//if we encountered one or more component exceptions
		{
			throw componentExceptions;	//throw the exception, which may contain multiple exceptions
		}
	}

	/**Opens the frame with the currently set modality.
	Opening the frame registers the frame with the session.
	If the frame is already open, no action occurs.
	@see #getState() 
	@see Frame#STATE_PROPERTY
	*/
	public void open()
	{
		if(getState()==State.CLOSED)	//if the state is closed
		{
			getSession().addFrame(this);	//add the frame to the session
			setState(State.OPEN);	//change the state
		}		
	}

	/**Opens the frame, specifying modality.
	Opening the frame registers the frame with the session.
	If the frame is already open, no action occurs.
	@param modal <code>true</code> if the frame should be opened as a modal frame, else <code>false</code>.
	@see #getState() 
	@see Frame#STATE_PROPERTY
	*/
	public void open(final boolean modal)
	{
		setModal(modal);	//update the modality
		open();	//open the frame normally
	}

	/**Opens the frame as modal and installs the given property change listener to listen for the mode changing.
	This is a convenience method that adds the mode change listener using {@link #addPropertyChangeListener(String, PropertyChangeListener)} and then calls {@link #open(boolean)} with a value of <code>true</code>.
	@param modeChangeListener The mode property change listener to add.
	@see ModalComponent#MODE_PROPERTY 
	*/
	public void open(final GuisePropertyChangeListener<? super C, Mode> modeChangeListener)
	{
		addPropertyChangeListener(MODE_PROPERTY, modeChangeListener);	//add the mode property change listener
		open(true);	//open modally
	}

	/**Determines whether the frame should be allowed to close.
	This implementation returns <code>true</code>.
	This method is called from {@link #close()}.
	@return <code>true</code> if the frame should be allowed to close.
	*/
	public boolean canClose()
	{
		return true;	//by default always allow the frame to be closed
	}

	/**Closes the frame.
	Closing the frame unregisters the frame with the session.
	If the frame is already closed, no action occurs.
	This method calls {@link #canClose()} and only performs closing functionality if that method returns <code>true</code>.
	This method delegates actual closing to {@link #closeImpl()}, and that method should be overridden rather than this one.
	@see #getState() 
	@see Frame#STATE_PROPERTY
	*/
	public final void close()
	{
		if(getState()!=State.CLOSED)	//if the frame is not already closed
		{
			if(canClose())	//if the frame can close
			{
				closeImpl();	//actually close the frame
			}
		}
	}

	/**Implementation of frame closing.*/
	protected void closeImpl()
	{
//TODO del Debug.trace("ready to remove frame");
		getSession().removeFrame(this);	//remove the frame from the session
		setState(State.CLOSED);	//change the state
	}
}
