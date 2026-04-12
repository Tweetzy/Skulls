/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads build-time expiration from build.properties (injected by Maven).
 * 0 = never expire.
 */
public final class BuildExpiration {

	private static final long EXPIRATION_EPOCH_MS = loadExpirationEpochMs();

	private static long loadExpirationEpochMs() {
		try (InputStream in = Skulls.class.getResourceAsStream("/build.properties")) {
			if (in == null) return 0L;
			Properties props = new Properties();
			props.load(in);
			String value = props.getProperty("build.expiration", "0").trim();
			if (value.isEmpty()) return 0L;
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * @return true if a non-zero expiration is set and current time is past it
	 */
	public static boolean isExpired() {
		return EXPIRATION_EPOCH_MS != 0L && System.currentTimeMillis() >= EXPIRATION_EPOCH_MS;
	}

	/**
	 * @return expiration timestamp in epoch ms, or 0 if no expiration
	 */
	public static long getExpirationEpochMs() {
		return EXPIRATION_EPOCH_MS;
	}

	private BuildExpiration() {
	}
}
