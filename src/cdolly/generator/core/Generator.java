package cdolly.generator.core;

import java.util.Iterator;

/**
 * Base class for generators. Handles reset bookkeeping and delegation to
 * derived classes.
 * 
 * @param <ValueType>
 *            The type of object the derived generator creates
 * 
 */
public abstract class Generator<ValueType> implements IGenerator<ValueType> {
	private boolean isReset;
	private ValueType current;
	private boolean optimized = false;
	private int jump = 10;
	protected long maximumPrograms = -1; 

	protected Generator() {
		isReset = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		isReset = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public ValueType next() {
		current = generateNext();
		isReset = false;
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isReset() {
		return isReset;
	}

	/**
	 * {@inheritDoc}
	 */
	public ValueType current() {
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean hasNext();

	/**
	 * Creates and returns the next object
	 */
	protected abstract ValueType generateNext();

	/**
	 * {@inheritDoc}
	 */
	public final Iterator<ValueType> iterator() {
		return new ForLoopIterator<ValueType>(this, isOptimized(), getJump());
	}

	public boolean isOptimized() {
		return optimized;
	}

	public void setOptimized(boolean optimized) {
		this.optimized = optimized;
	}

	public int getJump() {
		return jump;
	}

	public void setJump(int jump) {
		this.jump = jump;
	}

	public long getMaximumPrograms() {
		return maximumPrograms;
	}

	public void setMaximumPrograms(long maximumPrograms) {
		this.maximumPrograms = maximumPrograms;
	}



	
}
