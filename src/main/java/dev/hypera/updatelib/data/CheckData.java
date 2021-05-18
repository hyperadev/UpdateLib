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

package dev.hypera.updatelib.data;

import dev.hypera.updatelib.annotations.Internal;

@Internal
public class CheckData {

	private final long resourceId;
	private final String currentVersion;
	private final int timeout;

	/**
	 * CheckData Constructor - Used internally by UpdateLib.
	 *
	 * @param resourceId     Resource ID.
	 * @param currentVersion Current resource version.
	 * @param timeout        Connection timeout.
	 *
	 * @since 3.0.0-SNAPSHOT
	 */
	public CheckData(long resourceId, String currentVersion, int timeout) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.timeout = timeout;
	}

	/**
	 * Get resource ID.
	 *
	 * @return Resource ID.
	 * @since 3.0.0-SNAPSHOT
	 */
	public long getResourceId() {
		return resourceId;
	}

	/**
	 * Get current version.
	 *
	 * @return Current version.
	 * @since 3.0.0-SNAPSHOT
	 */
	public String getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Get connection timeout.
	 *
	 * @return Connection timeout in milliseconds.
	 * @since 3.0.0-SNAPSHOT
	 */
	public int getTimeout() {
		return timeout;
	}


}
