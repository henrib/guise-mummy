package com.guiseframework.converter;

import static java.text.MessageFormat.*;

/**A converter that converts a {@link Long} from and to a string literal with no delimiters.
@author Garret Wilson
@see Long
*/
public class PlainLongStringLiteralConverter extends AbstractStringLiteralConverter<Long>
{

	/**Converts a literal representation of a value from the lexical space into a value in the value space.
	@param literal The literal value in the lexical space to convert.
	@return The converted value in the value space, or <code>null</code> if the given literal is <code>null</code>.
	@exception ConversionException if the literal value cannot be converted.
	*/ 
	public Long convertLiteral(final String literal) throws ConversionException
	{
		try
		{
			return literal!=null && literal.length()>0 ? Long.valueOf(Long.parseLong(literal)) : null;	//if there is a literal, convert it to a Long			
		}
		catch(final NumberFormatException numberFormatException)	//if the string does not contain a valid Long
		{
			throw new ConversionException(format(getSession().dereferenceString(getInvalidValueMessage()), literal), literal);
		}
	}
}