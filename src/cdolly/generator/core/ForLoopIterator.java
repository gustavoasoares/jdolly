package cdolly.generator.core;

import java.util.Iterator;

/**
 * java.util.Iterator used to iterate over all generated values in a foreach
 * loop: <code>for(ValueType value : generator) {...}</code>
 */
public class ForLoopIterator<ValueType> implements Iterator<ValueType> {

	private IGenerator<ValueType> generator;
	private int currentPosition = 0;
	private int jump = 10;
	private final boolean optimized;

	public ForLoopIterator(Generator<ValueType> generator, boolean optimized,
			int jump) {
		this.generator = generator;
		this.optimized = optimized;
		this.jump = jump;
	}

	public boolean hasNext() {

		if (optimized) {
			currentPosition++;

			if (currentPosition == 1)
				return generator.hasNext();
			else {
				while (currentPosition % jump != 0) {
					generator.hasNext();
					currentPosition++;

				}

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
