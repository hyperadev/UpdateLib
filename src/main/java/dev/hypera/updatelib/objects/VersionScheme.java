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

package dev.hypera.updatelib.objects;

import java.util.regex.Pattern;

public enum VersionScheme {

	BASIC("MAJOR.MINOR", "^(v?)(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?$"), SEMANTIC("MAJOR.MINOR.PATCH - https://semver.org/", "^(v?)(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$"), CALENDAR("YYYY-MM-DD - https://calver.org", "^(v?)(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})$");

	private final String description;
	private final Pattern pattern;

	VersionScheme(String description, String regex) {
		this.description = description;
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * Get human readable description of the Version Scheme.
	 *
	 * @return Description.
	 * @since 3.0.0-SNAPSHOT
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the regex pattern of the Version Scheme.
	 *
	 * @return Regex pattern.
	 * @since 3.0.0-SNAPSHOT
	 */
	public Pattern getPattern() {
		return pattern;
	}

}
