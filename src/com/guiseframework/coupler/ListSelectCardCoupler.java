package com.guiseframework.coupler;

import static com.garretwilson.lang.ClassUtilities.*;

import java.beans.PropertyChangeListener;

import com.garretwilson.lang.ObjectUtilities;
import com.guiseframework.GuiseSession;
import com.guiseframework.component.*;
import com.guiseframework.event.*;
import com.guiseframework.model.*;
import com.guiseframework.validator.ValidationException;

/**Coupler that assocates a {@link ListSelectControl} with a card in a {@link CardControl}.
@param <V> The type of values to select.
When the specified value is selected, the first displayed and enabled specified card within the card control will be selected.
When any of the the associated cards are selected, the specified value is selected.
If the card's constraints implement {@link Displayable}, the list selected value will be displayed based upon the card constraints' displayed status.
If the card's constraints implement {@link Enableable}, the list selected value will be enabled based upon the card constraints' enabled status.
This coupler is only functional when the given card is contained within a {@link CardControl}.
@author Garret Wilson
*/
public class ListSelectCardCoupler<V> extends AbstractCardCoupler
{

	/**The bound property of the list select control.*/
	public final static String LIST_SELECT_PROPERTY=getPropertyName(ListSelectCardCoupler.class, "listSelect");
	/**The value bound property.*/
	public final static String VALUE_PROPERTY=getPropertyName(ListSelectCardCoupler.class, "value");

	/**The property change listener to listen for the value property of the list select control changing.*/
	private final PropertyChangeListener listSelectValueChangeListener=new AbstractGuisePropertyChangeListener<V>()
	{
		public void propertyChange(final GuisePropertyChangeEvent<V> propertyChangeEvent)	//if the list select value changed
		{
			final V newValue=propertyChangeEvent.getNewValue();	//get the new selected value
			if(newValue!=null && ObjectUtilities.equals(newValue, getValue()))	//if the connected value was selected
			{
				try
				{
					selectCard();	//select a connected card
				}
				catch(final ValidationException validationException)	//if the value can't be selected
				{
					try
					{
						listSelect.setValue(propertyChangeEvent.getOldValue());	//go back to the old selected value, if we can
					}
					catch(final ValidationException validationException2)	//if the old value can't be selected, just ignore the error TODO improve
					{
					}
				}
			}
		}		
	};

	/**The list select control to connect to the cards, or <code>null</code> if there is no control coupled with the cards.*/
	private ListSelectControl<V, ?> listSelect=null;

		/**@return The list select control to connect to the cards, or <code>null</code> if there is no control coupled with the cards.*/
		public ListSelectControl<V, ?> getListSelect() {return listSelect;}

		/**Sets the connected list select control.
		This is a bound property.
		@param newListSelect The new list select control to connect to the card, or <code>null</code> if the list select control should not be coupled with the cards.
		@see #LIST_SELECT_PROPERTY
		*/
		public void setListSelect(final ListSelectControl<V, ?> newListSelect)
		{
			if(listSelect!=newListSelect)	//if the value is really changing
			{
				final ListSelectControl<V, ?> oldListSelect=listSelect;	//get the old value
				if(oldListSelect!=null)	//if there is an old list select control
				{
					oldListSelect.removePropertyChangeListener(ListSelectControl.VALUE_PROPERTY, listSelectValueChangeListener);	//stop listening for list selected value changes
				}
				listSelect=newListSelect;	//actually change the value
				if(newListSelect!=null)	//if there is a new action
				{
					newListSelect.addPropertyChangeListener(ListSelectControl.VALUE_PROPERTY, listSelectValueChangeListener);	//list for list selected value changes
				}
				firePropertyChange(LIST_SELECT_PROPERTY, oldListSelect, newListSelect);	//indicate that the value changed
					//TODO replace all this with some sort of update() method in the abstract class
				updateSelected();	//update the control selection based upon the selected card
				updateDisplayed();	//update the displayed status based upon the selected card
				updateEnabled();	//update the enabled status based upon the selected card
				updateTaskStatus();	//update the task status based upon the selected card
			}			
		}

	/**The list select value to indicate selection, or <code>null</code> if there is no value.*/
	private V value=null;

		/**@return The list select value to indicate selection, or <code>null</code> if there is no value.*/
		public V getValue() {return value;}

		/**Sets the list select value to indicate selection.
		This is a bound property.
		@param newValue The list select value to indicate selection, or <code>null</code> if there is no value.
		@see #VALUE_PROPERTY
		*/
		public void setValue(final V newValue)
		{
			if(!ObjectUtilities.equals(value, newValue))	//if the value is really changing
			{
				final V oldValue=value;	//get the old value
				value=newValue;	//actually change the value
				firePropertyChange(VALUE_PROPERTY, oldValue, newValue);	//indicate that the value changed
					//TODO replace all this with some sort of update() method in the abstract class
				updateSelected();	//update the control selection based upon the selected card
				updateDisplayed();	//update the displayed status based upon the selected card
				updateEnabled();	//update the enabled status based upon the selected card
				updateTaskStatus();	//update the task status based upon the selected card
			}			
		}
		
	/**Session constructor.
	@param session The Guise session that owns these constraints.
	@exception NullPointerException if the given session is <code>null</code>.
	*/
	public ListSelectCardCoupler(final GuiseSession session)
	{
		this(session, null, null);	//construct the class with no list select control, value, or cards
	}
	
	/**Session, list select, value, and cards constructor.
	@param session The Guise session that owns these constraints.
	@param listSelect The list select control to connect to the cards, or <code>null</code> if there is no control coupled with the cards.
	@param value The value in the list to indicate the cards should be selected, or <code>null</code> if there is no value to indicate selection.
	@param cards The new cards to connect, if any.
	@exception NullPointerException if the given session is <code>null</code>.
	*/
	public ListSelectCardCoupler(final GuiseSession session, final ListSelectControl<V, ?> listSelect, final V value, final Component<?>... cards)
	{
		super(session, cards);	//construct the parent class
		setListSelect(listSelect);	//set the list select control
		setValue(value);	//set the value
	}

	/**Updates the current displayed status.
	This implementation updates the list select control's displayed status of the connected value.
	If no list select control or no value is connected, no action occurs.
	@param displayed The new displayed status.
	*/
	protected void updateDisplayed(final boolean displayed)
	{
		final ListSelectControl<V, ?> listSelect=getListSelect();	//get the list select control
		final V value=getValue();	//get the specified value
		if(listSelect!=null && value!=null)	//if there is a list select control and value specified
		{
			listSelect.setValueDisplayed(value, displayed);	//update the displayed status of the list select control for the specified value
		}
	}

	/**Updates the current enabled status.
	This implementation updates the list select control's enabled status of the connected value.
	If no list select control or no value is connected, no action occurs.
	@param enabled The new enabled status.
	*/
	protected void updateEnabled(final boolean enabled)
	{
		final ListSelectControl<V, ?> listSelect=getListSelect();	//get the list select control
		final V value=getValue();	//get the specified value
		if(listSelect!=null && value!=null)	//if there is a list select control and value specified
		{
			listSelect.setValueEnabled(value, enabled);	//update the enabled status of the list select control for the specified value
		}
	}

	/**Updates the current selected status.
	This implementation selects the connected value in the conntected list select control if the new selected state is selected.
	If no list select control or no value is connected, no action occurs.
	@param selected The new selected status.
	*/
	protected void updateSelected(final boolean selected)
	{
		if(selected)	//if one of the connected cards is selected
		{
			final ListSelectControl<V, ?> listSelect=getListSelect();	//get the list select control
			final V value=getValue();	//get the specified value
			if(listSelect!=null && value!=null)	//if there is a list select control and value specified
			{
				try
				{
					listSelect.setValue(value);	//select the requested value
//TODO why does this throw a ClassCastException?					listSelect.setSelectedValues(value);	//select the requested value
				}
				catch(final ValidationException validationException)	//if the value can't be selected, just ignore the error
				{
				}
			}
		}
	}
}