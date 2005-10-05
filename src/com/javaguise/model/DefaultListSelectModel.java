package com.javaguise.model;

import java.util.*;

import com.garretwilson.lang.ObjectUtilities;
import com.javaguise.event.*;
import com.javaguise.session.GuiseSession;
import com.javaguise.validator.ValidationException;
import com.javaguise.validator.Validator;

import static com.garretwilson.lang.ObjectUtilities.*;

/**The default implementation of a model for selecting one or more values from a list.
The model is thread-safe, synchronized on itself. Any iteration over values should include synchronization on the instance of this class. 
@param <V> The type of values contained in the model.
@author Garret Wilson
*/
public class DefaultListSelectModel<V> extends AbstractValueModel<V> implements ListSelectModel<V>
{

	/**@return The selected value, or <code>null</code> if there is no selected value.*/
	public V getValue() {return getSelectedValue();}

	/**Sets the input value.
	This is a bound property that only fires a change event when the new value is different via the <code>equals()</code> method.
	If a validator is installed, the value will first be validated before the current value is changed.
	Validation always occurs if a validator is installed, even if the value is not changing.
	@param newValue The input value of the model.
	@exception ValidationException if the provided value is not valid.
	@see #getValidator()
	@see ValueModel#VALUE_PROPERTY
	*/
	public void setValue(final V newValue) throws ValidationException
	{
		final Validator<V> validator=getValidator();	//get the currently installed validator, if there is one
		if(validator!=null)	//if a validator is installed, always validate the value, even if it isn't changing, so that an initial value that may not be valid will throw an error when it's tried to be set to the same, but invalid, value
		{
			validator.validate(newValue);	//validate the new value, throwing an exception if anything is wrong
		}
		setSelectedValues(newValue);
	}

	/**Resets the value to a default value, which may be invalid according to any installed validators.
	No validation occurs.
	@see ValueModel#VALUE_PROPERTY
	*/
	public void resetValue()
	{
//TODO del when works		setSelectedValues();	//select no values
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		getSelectionStrategy().setSelectedValues(this);	//delegate to the selection strategy, selecting no values
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**The list of values, all access to which will be synchronized on this.*/
	private final List<V> values=new ArrayList<V>();

	/**@return The number of values in the model.*/
	public synchronized int size() {return values.size();}

	/**@return Whether this model contains no values.*/
	public synchronized boolean isEmpty() {return values.isEmpty();}

	/**Determines whether this model contains the specified value.
	@param value The value the presence of which to test.
	@return <code>true</code> if this model contains the specified value.
	*/
	public synchronized boolean contains(final Object value) {return values.contains(value);}

	/**@return An iterator over the values in this model.*/
	public synchronized Iterator<V> iterator() {return values.iterator();}

	/**@return An array containing all of the values in this model.*/
	public synchronized Object[] toArray() {return values.toArray();}

	/**Returns an array containing all of the values in this model.
	@param array The array into which the value of this collection are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose.
	@return An array containing the values of this model.
	@exception ArrayStoreException if the runtime type of the specified array is not a supertype of the runtime type of every value in this model.
	@exception NullPointerException if the specified array is <code>null</code>.
	*/
	public synchronized <T> T[] toArray(final T[] array) {return values.toArray(array);}

	/**Appends the specified value to the end of this model.
	This version delegates to {@link #add(int, Object)}.
	@param value The value to be appended to this model.
	@return <code>true</code>, indicating that the model changed as a result of the operation.
	*/
	public synchronized boolean add(final V value)
	{
		add(values.size(), value);	//add the value to the end of the list
		return true;	//this operation always modifies the list
	}

	/**Removes the first occurrence in this model of the specified value. 
	@param value The value to be removed from this model, if present.
	@return <code>true</code> if this model contained the specified value.
	*/
	@SuppressWarnings("unchecked")	//we only cast the value if the list was modified, which implies the value was in the list, implying that that list is of the appropriate type or it wouldn't have been in the list to begin with
	public synchronized boolean remove(final Object value)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final boolean modified=values.remove(value);	//remove the value from the list
		if(modified)	//if the list was modified
		{
			fireListModified(-1, null, (V)value);	//indicate the value was removed from an unknown index
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		}
		return modified;	//indicate whether the list was modified
	}

	/**Determines if this model contains all of the values of the specified collection.
	@param collection The collection to be checked for containment in this model.
	@return <code>true</code> if this model contains all of the values of the specified collection.
	@exception NullPointerException if the specified collection is <code>null</code>.
	@see #contains(Object)
	*/
	public synchronized boolean containsAll(final Collection<?> collection) {return values.containsAll(collection);}

	/**Appends all of the values in the specified collection to the end of this model, in the order that they are returned by the specified collection's iterator.
	@param collection The collection the values of which are to be added to this model.
	@return <code>true</code> if this model changed as a result of the call.
	@exception NullPointerException if the specified collection is <code>null</code>.
	@see #add(Object)
	*/
	public synchronized boolean addAll(final Collection<? extends V> collection)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final boolean modified=values.addAll(collection);	//add all the values
		if(modified)	//if the list was modified
		{
			fireListModified(-1, null, null);	//indicate a general list modification
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		}
		return modified;	//indicate whether the list was modified
	}

	/**Inserts all of the values in the specified collection into this model at the specified position.
	@param index The index at which to insert first value from the specified collection.
	@param collection The values to be inserted into this model.
	@return <code>true</code> if this model changed as a result of the call.
	@exception NullPointerException if the specified collection is <code>null</code>.
	@exception IndexOutOfBoundsException if the index is out of range (<var>index</var> &lt; 0 || <var>index</var> &gt; <code>size()</code>).
	*/
	public synchronized boolean addAll(final int index, final Collection<? extends V> collection)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final boolean modified=values.addAll(index, collection);	//add the values
		if(modified)	//if the list was modified
		{
			fireListModified(-1, null, null);	//indicate a general list modification
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		}
		return modified;	//indicate whether the list was modified
}

	/**Removes from this model all the values that are contained in the specified collection.
	@param collection The collection that defines which values will be removed from this model.
	@return <code>true</code> if this model changed as a result of the call.
	@exception NullPointerException if the specified collection is <code>null</code>.
	@see #remove(Object)
	@see #contains(Object)
	*/
	public synchronized boolean removeAll(final Collection<?> collection)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final boolean modified=values.removeAll(collection);	//remove the values
		if(modified)	//if the list was modified
		{
			fireListModified(-1, null, null);	//indicate a general list modification
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		}
		return modified;	//indicate whether the list was modified
	}

	/**Retains only the values in this model that are contained in the specified collection.
	@param collection The collection that defines which values this model will retain.
	@return <code>true</code> if this model changed as a result of the call.
	@exception NullPointerException if the specified collection is <code>null</code>.
	@see #remove(Object)
	@see #contains(Object)
	*/
	public synchronized boolean retainAll(final Collection<?> collection)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final boolean modified=values.retainAll(collection);	//remove values if needed
		if(modified)	//if the list was modified
		{
			fireListModified(-1, null, null);	//indicate a general list modification
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		}
		return modified;	//indicate whether the list was modified		
	}

	/**Removes all of the values from this model.*/
	public synchronized void clear()
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		values.clear();	//clear the list
		fireListModified(-1, null, null);	//indicate a general list modification (without more intricate synchornization, we can't know for sure if the list was modified, even checking the size beforehand, because of thread race conditions)
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Returns the value at the specified position in this model.
	@param index The index of the value to return.
	@return The value at the specified position in this model.
	@throws IndexOutOfBoundsException if the index is out of range (<var>index</var> &lt; 0 || <var>index</var> &gt;= <code>size()</code>).
	*/
	public synchronized V get(final int index) {return values.get(index);}

	/**Replaces the value at the specified position in this model with the specified value.
	@param index The index of the value to replace.
	@param value The value to be stored at the specified position.
	@return The value at the specified position.
	@exception IndexOutOfBoundsException if the index is out of range (<var>index<var> &lt; 0 || <var>index</var> &gt;= <code>size()</code>).
	*/
	public synchronized V set(final int index, final V value)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final V oldValue=values.set(index, value);	//set the value at the given index
		fireListModified(index, oldValue, value);	//indicate that the value at the given index was replaced
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		return oldValue;	//return the old value
	}

	/**Inserts the specified value at the specified position in this model.
	@param index The index at which the specified value is to be inserted.
	@param value The value to be inserted.
	@throws IndexOutOfBoundsException if the index is out of range (<var>index</var> &lt; 0 || <var>index</var> &gt; <code>size()</code>).
	*/
	public synchronized void add(final int index, final V value)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		values.add(index, value);	//add the value at the requested index
		fireListModified(index, value, null);	//indicate the value was added at the given index
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Removes the value at the specified position in this model.
	@param index The index of the value to removed.
	@return The value previously at the specified position.
	@exception IndexOutOfBoundsException if the index is out of range (<var>index</var> &lt; 0 || <var>index</var> &gt;= <code>size()</code>).
	*/
	public synchronized V remove(final int index)
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		final V value=values.remove(index);	//remove the value at this index	
		fireListModified(index, null, value);	//indicate the value was removed from the given index
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
		return value;	//return the value that was removed
	}

  /**Returns the index in this model of the first occurrence of the specified value, or -1 if this model does not contain this value.
	@param value The value for which to search.
	@return The index in this model of the first occurrence of the specified value, or -1 if this model does not contain this value.
	*/
	public synchronized int indexOf(final Object value) {return values.indexOf(value);}

	/**Returns the index in this model of the last occurrence of the specified value, or -1 if this model does not contain this value.
	@param value The value for which to search.
	@return The index in this model of the last occurrence of the specified vale, or -1 if this model does not contain this value.
	*/
	public synchronized int lastIndexOf(final Object value) {return values.lastIndexOf(value);}

	/**@return A list iterator of the values in this model (in proper sequence).*/
	public synchronized ListIterator<V> listIterator() {return values.listIterator();}

	/**Returns a list iterator of the values in this model (in proper sequence), starting at the specified position in this model.
	@param index The index of first value to be returned from the list iterator (by a call to the <code>next()</code> method).
	@return A list iterator of the values in this model (in proper sequence), starting at the specified position in this model.
	@exception IndexOutOfBoundsException if the index is out of range (<var>index</var> &lt; 0 || <var>index</var> &gt; <code>size()</code>).
	*/
	public synchronized ListIterator<V> listIterator(final int index) {return values.listIterator(index);}

	/**Returns a view of the portion of this model between the specified <var>fromIndex</var>, inclusive, and <var>toIndex</var>, exclusive.
	@param fromIndex The low endpoint (inclusive) of the sub-list.
	@param toIndex The high endpoint (exclusive) of the sub-list.
	@return A view of the specified range within this model.
	@throws IndexOutOfBoundsException for an illegal endpoint index value (<var>fromIndex</var> &lt; 0 || <var>toIndex</var> &gt; <code>size()</code> || <var>fromIndex</var> &gt; <var>toIndex</var>).
	*/
	public synchronized List<V> subList(final int fromIndex, final int toIndex) {return values.subList(fromIndex, toIndex);}

	/**Replaces the first occurrence in the of the given value with its replacement.
	This method ensures that another thread does not change the model while the search and replace operation occurs.
	@param oldValue The value for which to search.
	@param newValue The replacement value.
	@return Whether the operation resulted in a modification of the model.
	*/
	public synchronized boolean replace(final V oldValue, final V newValue)
	{
		final int index=indexOf(oldValue);	//get the index of the old value
		if(index>=0)	//if the value is in the model
		{
			final V oldSelectedValue=getSelectedValue();	//get the old selected value
			set(index, newValue);	//change the value at the given index, which will fire the appropriate event
			firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
			return true;	//indicate that we modified the model
		}
		else	//if the value is not in the model
		{
			return false;	//report that the old value could not be found
		}
	}

	/**The selection strategy for this model.*/
	private ListSelectionStrategy<V> selectionStrategy;

		/**@return The selection strategy for this model.*/
		public ListSelectionStrategy<V> getSelectionStrategy() {return selectionStrategy;}

	/**Determines the selected index.
	This method delegates to the selection strategy.
	If more than one index is selected, the lead selected index will be returned.
	@return The index currently selected, or -1 if no index is selected.
	@see #getSelectedValue()
	*/
	public int getSelectedIndex()
	{
		return getSelectionStrategy().getSelectedIndex(this);	//delegate to the selection strategy		
	}

	/**Determines the selected indices.
	This method delegates to the selection strategy.
	@return The indices currently selected.
	@see #getSelectedValues()
	*/
	public int[] getSelectedIndices()
	{
		return getSelectionStrategy().getSelectedIndices(this);	//delegate to the selection strategy
	}

	/**Determines the selected value.
	This method delegates to the selection strategy.
	If more than one value is selected, the lead selected value will be returned.
	@return The value currently selected, or <code>null</code> if no value is currently selected.
	@see #getSelectedIndex()
	*/
	public V getSelectedValue()
	{
		return getSelectionStrategy().getSelectedValue(this);	//delegate to the selection strategy		
	}

	/**Determines the selected values.
	This method delegates to the selection strategy.
	@return The values currently selected.
	@see #getSelectedIndices()
	*/
	public V[] getSelectedValues()
	{
		return getSelectionStrategy().getSelectedValues(this);	//delegate to the selection strategy
	}

	/**Sets the selected indices.
	Invalid and duplicate indices will be ignored.
	This method delegates to the selection strategy.
	@param indices The indices to select.
	@exception ValidationException if the provided value is not valid.
	@see #setSelectedValues(V[])
	@see #addSelectedIndex(int)
	*/
	public void setSelectedIndices(final int... indices) throws ValidationException
	{
int validIndexCount=0;	//TODO fix validation hack
for(int i=indices.length-1; i>=0; --i)
{
	if(indices[i]>=0)
	{
		++validIndexCount;
	}
}
if(validIndexCount==0)	//TODO add more thorough validation throughout; right now we only check for null not being valid; also take into consideration that some of the indices may be invalid and therefore ignored
{
	final Validator<V> validator=getValidator();	//get the currently installed validator, if there is one
	if(validator!=null)	//if a validator is installed, always validate the value, even if it isn't changing, so that an initial value that may not be valid will throw an error when it's tried to be set to the same, but invalid, value
	{
		validator.validate(null);	//validate the new value, throwing an exception if anything is wrong
	}
}
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		getSelectionStrategy().setSelectedIndices(this, indices);	//delegate to the selection strategy
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Sets the selected values.
	If a value occurs more than one time in the model, the first occurrence of the value will be selected.
	Values that do not occur in the select model will be ignored.
	This method delegates to the selection strategy.
	@param values The values to select.
	@exception ValidationException if the provided value is not valid.
	@see #setSelectedIndices(int[])
	*/
	public void setSelectedValues(final V... values) throws ValidationException
	{
if(values.length==0)	//TODO add more thorough validation throughout; right now we only check for null not being valid
{
	final Validator<V> validator=getValidator();	//get the currently installed validator, if there is one
	if(validator!=null)	//if a validator is installed, always validate the value, even if it isn't changing, so that an initial value that may not be valid will throw an error when it's tried to be set to the same, but invalid, value
	{
		validator.validate(null);	//validate the new value, throwing an exception if anything is wrong
	}
}
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		getSelectionStrategy().setSelectedValues(this, values);	//delegate to the selection strategy
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Adds a selection at the given index.
	An invalid index will be ignored.
	This method delegates to the selection strategy.
	@param index The index to add as a selection.
	@exception ValidationException if the provided value is not valid.
	@see #setSelectedIndices(int[])
	*/
	public void addSelectedIndex(final int index) throws ValidationException
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		getSelectionStrategy().addSelectedIndex(this, index);	//delegate to the selection strategy
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Removes a selection at the given index.
	An invalid index will be ignored.
	This method delegates to the selection strategy.
	@param index The index to remove as a selection.
	@exception ValidationException if the provided value is not valid.
	@see #setSelectedIndices(int[])
	*/
	public void removeSelectedIndex(final int index) throws ValidationException
	{
		final V oldSelectedValue=getSelectedValue();	//get the old selected value
		getSelectionStrategy().removeSelectedIndex(this, index);	//delegate to the selection strategy
		firePropertyChange(VALUE_PROPERTY, oldSelectedValue, getSelectedValue());	//indicate that the value changed if needed		
	}

	/**Adds a list listener.
	@param listListener The list listener to add.
	*/
	public void addListListener(final ListListener<ListSelectModel<V>, V> listListener)
	{
		getEventListenerManager().add(ListListener.class, listListener);	//add the listener
	}

	/**Removes a list listener.
	@param listListener The list listener to remove.
	*/
	public void removeListListener(final ListListener<ListSelectModel<V>, V> listListener)
	{
		getEventListenerManager().remove(ListListener.class, listListener);	//remove the listener
	}

	/**Fires an event to all registered list listeners indicating the list was modified.
	This method first manually notifies its selection strategy that the list has changed.
	@param index The index at which an element was added and/or removed, or -1 if the index is unknown.
	@param addedElement The element that was added to the list, or <code>null</code> if no element was added or it is unknown whether or which elements were added.
	@param removedElement The element that was removed from the list, or <code>null</code> if no element was removed or it is unknown whether or which elements were removed.
	@see ListListener
	@see ListEvent
	*/
	protected void fireListModified(final int index, final V addedElement, final V removedElement)
	{
		final ListEvent<ListSelectModel<V>, V> listEvent=new ListEvent<ListSelectModel<V>, V>(getSession(), this, index, addedElement, removedElement);	//create a new event
		getSelectionStrategy().listModified(listEvent);	//manually notify the selection strategy, because the queued event might be delayed and reported out of order
		if(getEventListenerManager().hasListeners(ListListener.class))	//if there are appropriate listeners registered
		{
			getSession().queueEvent(new PostponedListEvent<ListSelectModel<V>, V>(getEventListenerManager(), listEvent));	//tell the Guise session to queue the event
		}
	}

	/**Constructs a list select model indicating the type of values it can hold, using a default multiple selection strategy.
	@param session The Guise session that owns this model.
	@param valueClass The class indicating the type of values held in the model.
	@exception NullPointerException if the given session and/or class object is <code>null</code>.
	*/
	public DefaultListSelectModel(final GuiseSession session, final Class<V> valueClass)
	{
		this(session, valueClass, new MultipleListSelectionStrategy<V>());	//construct the class with a multiple selection strategy
	}

	/**Constructs a list select model indicating the type of values it can hold.
	The selection strategy is not added as a listener to this model but is rather notified manually so that the event won't be delayed and/or sent out of order
	@param session The Guise session that owns this model.
	@param valueClass The class indicating the type of values held in the model.
	@param listSelectionStrategy The strategy for selecting values in the model.
	@exception NullPointerException if the given session, class object, and/or selection strategy is <code>null</code>.
	*/
	public DefaultListSelectModel(final GuiseSession session, final Class<V> valueClass, final ListSelectionStrategy<V> listSelectionStrategy)
	{
		super(session, valueClass);	//construct the parent class
		this.selectionStrategy=checkNull(listSelectionStrategy, "Selection strategy cannot be null.");
	}
}
