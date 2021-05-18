/*
 * Copyright (c) 2021 Joshua Sing <joshua@hypera.dev>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package dev.hypera.updatelib.utils;

import dev.hypera.updatelib.annotations.Internal;
import dev.hypera.updatelib.exceptions.VersionSchemeException;
import dev.hypera.updatelib.objects.VersionScheme;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;

@Internal
public class VersionUtils {

	/**
	 * Detect {@link VersionScheme} from string. - Used internally by UpdateLib.
	 *
	 * @param version Version string.
	 *
	 * @return Optional {@link VersionScheme}
	 * @since 3.0.0-SNAPSHOT
	 */
	public static Optional<VersionScheme> detectScheme(String version) {
		return Arrays.stream(VersionScheme.values()).filter(scheme -> scheme.getPattern().matcher(version).find()).findFirst();
	}

	/**
	 * Compare two versions. - Used internally by UpdateLib.
	 *
	 * @param versionScheme  Version scheme.
	 * @param newVersion     New/Distributed version.
	 * @param currentVersion Current resource version.
	 *
	 * @return {@link VersionChange}
	 * @since 3.0.0-SNAPSHOT
	 */
	public static VersionChange compare(VersionScheme versionScheme, String newVersion, String currentVersion) {
		Check.notNull("version scheme", versionScheme);
		Check.notNull("new version", newVersion);
		Check.notNull("current version", currentVersion);

		if(newVersion.equals(currentVersion))
			return VersionChange.NONE;

		Matcher matcher = versionScheme.getPattern().matcher(newVersion);
		Matcher currentMatcher = versionScheme.getPattern().matcher(currentVersion);

		if(!matcher.find() || !currentMatcher.find())
			throw new VersionSchemeException("Version does not matcher version scheme.");

		switch(versionScheme) {
			case BASIC:
				if(safeCheck(currentMatcher.group("major"), matcher.group("major")))
					return VersionChange.MAJOR;
				if(safeCheck(currentMatcher.group("minor"), matcher.group("minor")))
					return VersionChange.MINOR;
				if(safeCheck(currentMatcher.group("prerelease"), matcher.group("prerelease")))
					return VersionChange.PRE_RELEASE;
				return VersionChange.NONE;

			case SEMANTIC:
				if(safeCheck(currentMatcher.group("major"), matcher.group("major")))
					return VersionChange.MAJOR;
				if(safeCheck(currentMatcher.group("minor"), matcher.group("minor")))
					return VersionChange.MINOR;
				if(safeCheck(currentMatcher.group("patch"), matcher.group("patch")))
					return VersionChange.PATCH;
				if(safeCheck(currentMatcher.group("prerelease"), matcher.group("prerelease")))
					return VersionChange.PRE_RELEASE;
				if(safeCheck(currentMatcher.group("buildmetadata"), matcher.group("buildmetadata")))
					return VersionChange.METADATA;
				return VersionChange.NONE;

			case CALENDAR:
				if(safeCheck(currentMatcher.group("year"), matcher.group("year")))
					return VersionChange.YEAR;
				if(safeCheck(currentMatcher.group("month"), matcher.group("month")))
					return VersionChange.MONTH;
				if(safeCheck(currentMatcher.group("day"), matcher.group("day")))
					return VersionChange.DAY;
				return VersionChange.NONE;

			default:
				throw new VersionSchemeException("Unknown version scheme");
		}

	}

	/**
	 * Safely check two integers. - Used internally by UpdateLib.
	 *
	 * @param currentGroup Group 1
	 * @param newGroup     Group 2
	 *
	 * @return If newGroup > currentGroup.
	 * @since 3.0.0-SNAPSHOT
	 */
	private static boolean safeCheck(String currentGroup, String newGroup) {
		if(null == currentGroup || currentGroup.equals("") || null == newGroup || newGroup.equals(""))
			return false;

		int currentInt;
		int newInt;

		try {
			currentInt = Integer.parseInt(currentGroup);
			newInt = Integer.parseInt(newGroup);
		} catch (Exception ex) {
			return !newGroup.equals(currentGroup);
		}

		return newInt > currentInt;
	}

	@Internal
	public enum VersionChange {
		NONE, MAJOR, MINOR, PATCH, PRE_RELEASE, METADATA, YEAR, MONTH, DAY
	}

}
