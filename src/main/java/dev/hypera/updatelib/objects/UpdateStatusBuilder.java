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

import dev.hypera.updatelib.annotations.Builder;
import dev.hypera.updatelib.annotations.Internal;
import dev.hypera.updatelib.exceptions.VersionSchemeException;
import dev.hypera.updatelib.utils.Check;
import dev.hypera.updatelib.utils.VersionUtils;

import java.util.Optional;

@Builder
@Internal
public class UpdateStatusBuilder {

	private final String distributedVersion;
	private final String currentVersion;

	private VersionScheme scheme;

	/**
	 * UpdateStatusBuilder Constructor - Used internally by UpdateLib.
	 *
	 * @param distributedVersion Latest distributed resource version.
	 * @param currentVersion     Current resource version.
	 *
	 * @since 3.0.0-SNAPSHOT
	 */
	private UpdateStatusBuilder(String distributedVersion, String currentVersion) {
		Check.notNull("currentVersion", currentVersion);
		this.distributedVersion = distributedVersion;
		this.currentVersion = currentVersion;
	}

	/**
	 * Create a new instance of {@link UpdateStatusBuilder}
	 *
	 * @param distributedVersion Latest distributed resource version.
	 * @param currentVersion     Current resource version.
	 *
	 * @return {@link UpdateStatusBuilder}
	 * @since 3.0.0-SNAPSHOT
	 */
	public static UpdateStatusBuilder create(String distributedVersion, String currentVersion) {
		return new UpdateStatusBuilder(distributedVersion, currentVersion);
	}

	/**
	 * Set version scheme.
	 *
	 * @param scheme {@link VersionScheme} to use.
	 *
	 * @return {@link UpdateStatusBuilder}
	 * @since 3.0.0-SNAPSHOT
	 */
	public UpdateStatusBuilder setVersionScheme(VersionScheme scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * Builds a new instance of {@link UpdateStatus}
	 *
	 * @return Instance of {@link UpdateStatus}
	 * @since 3.0.0-SNAPSHOT
	 */
	public UpdateStatus build() {
		if(null == distributedVersion)
			return new UpdateStatus(null, currentVersion, UpdateStatus.Status.FAILED);

		if(null == scheme) {
			Optional<VersionScheme> versionScheme = VersionUtils.detectScheme(distributedVersion);
			Optional<VersionScheme> currentVersionScheme = VersionUtils.detectScheme(currentVersion);

			if(!versionScheme.isPresent() || !currentVersionScheme.isPresent())
				throw new VersionSchemeException("Cannot find version scheme for '" + distributedVersion + "'/'" + currentVersion + "'");
			if(!versionScheme.get().equals(currentVersionScheme.get()))
				throw new VersionSchemeException("Current and distributed version schemes must match.");

			scheme = versionScheme.get();
		}

		VersionUtils.VersionChange change = VersionUtils.compare(scheme, distributedVersion, currentVersion);

		return new UpdateStatus(distributedVersion, currentVersion, changeToStatus(change));
	}

	/**
	 * Converts {@link VersionUtils.VersionChange} to {@link UpdateStatus.Status}
	 *
	 * @param change VersionChange.
	 *
	 * @return Status.
	 * @since 3.0.0-SNAPSHOT
	 */
	private UpdateStatus.Status changeToStatus(VersionUtils.VersionChange change) {
		if(change.equals(VersionUtils.VersionChange.NONE))
			return UpdateStatus.Status.UNAVAILABLE;

		if(change.equals(VersionUtils.VersionChange.MAJOR))
			return UpdateStatus.Status.MAJOR_AVAILABLE;

		if(change.equals(VersionUtils.VersionChange.MINOR))
			return UpdateStatus.Status.MINOR_AVAILABLE;

		return UpdateStatus.Status.AVAILABLE;
	}

}
