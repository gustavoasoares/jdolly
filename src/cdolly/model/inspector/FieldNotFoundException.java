package cdolly.model.inspector;

import cdolly.model.Entity;
import cdolly.model.Relation;

/**
 * @author Jeanderson Candido<br>
 *         <a href="http://jeandersonbc.github.io"
 *         target="_blank">http://jeandersonbc.github.io</a>
 */
public class FieldNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FieldNotFoundException(Entity entity, Relation relation) {
		super("Could not find the field specified by \"" + relation.label()
				+ "\" in \"" + entity.label() + "\"");
	}

}
