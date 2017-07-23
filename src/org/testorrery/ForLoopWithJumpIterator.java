package org.testorrery;

import java.util.Iterator;

/**
 * java.util.Iterator used to iterate over all generated values in a foreach
 * loop: <code>for(ValueType value : generator) {...}</code>
 */
public class ForLoopWithJumpIterator<ValueType> implements Iterator<ValueType> {

	private int currentPosition = 0;
	private int jump = 10;

	private IGenerator<ValueType> generator;

	public ForLoopWithJumpIterator(IGenerator<ValueType> generator) {
		this.generator = generator;
	}

	public boolean hasNext() {
		currentPosition++;

		if (currentPosition == 1)
			return generator.hasNext();
		else {
			while (currentPosition % jump != 0) {
				generator.hasNext();
				currentPosition++;
				
			}

		}

		return generator.hasNext();
	}

	public ValueType next() {

		return generator.next();

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
