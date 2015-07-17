package cdolly.generator.core;

import java.util.Iterator;

/**
 * The interface for a generator object
 * @param <ValueType> The type of value that the generator generates
 */
public interface IGenerator<ValueType> extends Iterable<ValueType>  {
	
	/**
	 * Returns true if the generator can generate another value; 
	 * false if the generator has exhausted all possibilities
	 */
	boolean hasNext();
	
	/**
	 * Returns true if the generator is in a reset state; false otherwise. 
	 * The generator can be put in a reset state by calling reset(). 
	 * The generator is no longer in a reset state after next() is called.  
	 */
	boolean isReset();
	
	/**
	 * Generates and returns the next value. Behavior is undefined when 
	 * hasNext() == false.
	 */
	ValueType next();
	
	/**
	 * Returns the generator to a reset state "before" the first generatable value.
	 */
	void reset();	
	
	/**
	 * Returns the current value of the generator.  That is, the last value that 
	 * next() returned or, in the case of modifiable objects, a new copy of the value.
	 */
	ValueType current();

	/**
	 * java.util.Iterator used to iterate over all generated values in a foreach loop:
	 * <code>for(ValueType value : generator) {...}</code>
	 */
	Iterator<ValueType> iterator();
	
	
}
