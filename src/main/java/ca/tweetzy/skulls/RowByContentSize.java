package ca.tweetzy.skulls;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: October 15 2021
 * Time Created: 1:01 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class RowByContentSize {

	public int get(@NonNull final List<Object> content) {
		final int size = content.size();
		if (size >= 1 && size <= 9) return 1;
		if (size >= 10 && size <= 18) return 2;
		if (size >= 19 && size <= 27) return 3;
		if (size >= 28 && size <= 36) return 4;
		if (size >= 37 && size <= 45) return 5;
		if (size >= 46) return 6;
		return 1;
	}

	public int get(final int size) {
		if (size >= 1 && size <= 9) return 1;
		if (size >= 10 && size <= 18) return 2;
		if (size >= 19 && size <= 27) return 3;
		if (size >= 28 && size <= 36) return 4;
		if (size >= 37 && size <= 45) return 5;
		if (size >= 46) return 6;
		return 1;
	}
}
