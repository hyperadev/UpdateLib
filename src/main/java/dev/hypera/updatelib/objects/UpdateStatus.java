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

public class UpdateStatus {

	private final String distributedVersion;
	private final String currentVersion;
	private final Status status;

	/**
	 * UpdateStatus Constructor - Used internally by UpdateLib.
	 *
	 * @param distributedVersion Latest distributed resource version.
	 * @param currentVersion     Current resource version.
	 * @param status             Update status.
	 *
	 * @since 3.0.0-SNAPSHOT
	 */
	protected UpdateStatus(String distributedVersion, String currentVersion, Status status) {
		this.distributedVersion = distributedVersion;
		this.currentVersion = currentVersion;
		this.status = status;
	}

	/**
	 * Get latest distributed version.
	 *
	 * @return Distributed version.
	 * @since 3.0.0-SNAPSHOT
	 */
	public String getDistributedVersion() {
		return distributedVersion;
	}

	/**
	 * Get current resource version.
	 *
	 * @return Current version.
	 * @since 3.0.0-SNAPSHOT
	 */
	public String getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Get update status.
	 *
	 * @return Update status.
	 * @since 3.0.0-SNAPSHOT
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * If an update is available.
	 * @return Update available.
	 * @since 3.0.0-SNAPSHOT
	 */
	public boolean isAvailable() {
		return status.equals(Status.AVAILABLE) || status.equals(Status.MAJOR_AVAILABLE) || status.equals(Status.MINOR_AVAILABLE);
	}

	public enum Status {
		AVAILABLE, MAJOR_AVAILABLE, MINOR_AVAILABLE, UNAVAILABLE, FAILED
	}

}
