
package acme.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.CollectionHelper;
import acme.client.helpers.ConversionHelper;

public class SelectChoicesHelper {

	public static final <E extends AbstractEntity> SelectChoices from(final Collection<E> source, final Function<E, ?> valueMapper, final E selected) {
		assert !CollectionHelper.someNull(source);
		assert valueMapper != null;
		// HINT: selected can be null

		SelectChoices result;
		Iterator<E> iterator;

		result = new SelectChoices();
		result.add("0", "----", selected == null);
		iterator = source.iterator();
		while (iterator.hasNext()) {
			E choice;
			String key;
			Object value;
			String label;

			choice = iterator.next();
			key = Integer.toString(choice.getId());
			value = valueMapper.apply(choice);
			assert ConversionHelper.canConvert(value, String.class) : String.format("Cannot convert mapped value '%s' to a string.", value);
			label = ConversionHelper.convert(value, String.class);
			result.add(key, label, choice.equals(selected));
		}
		assert result.getSelected() != null : "There is no selected choice in source.";

		return result;
	}
}
