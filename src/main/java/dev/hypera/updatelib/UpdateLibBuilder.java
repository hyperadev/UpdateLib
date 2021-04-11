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

package dev.hypera.updatelib;

import dev.hypera.updatelib.internal.UpdateLib;
import dev.hypera.updatelib.internal.UpdateResponse;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class UpdateLibBuilder {

	private final long resourceId;
	private final String currentVersion;

	private boolean repeatingChecksEnabled = true;
	private long checkInterval = 2 * (60 * (60 * 1000)); // 2 Hours
	private int connectionTimeout = 5000; // 5 Seconds

	private Consumer<UpdateResponse> consumer = null;

	private UpdateLibBuilder(String currentVersion, long resourceId) {
		this.currentVersion = currentVersion;
		this.resourceId = resourceId;
	}

	/**
	 * Creates a new instance of {@link UpdateLibBuilder}.
	 *
	 * @param currentVersion Current version of the resource.
	 * @param resourceId     SpigotMC Resource Id.
	 *
	 * @return Instance of {@link UpdateLibBuilder}
	 * @since 2.0.0-SNAPSHOT
	 */
	public static UpdateLibBuilder create(String currentVersion, long resourceId) {
		return new UpdateLibBuilder(currentVersion, resourceId);
	}

	/**
	 * Should UpdateLib keep checking for updates? (Time defined by checkInterval)
	 *
	 * @param enabled Repeating checks enabled.
	 *
	 * @see #setCheckInterval(long)
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setRepeatingChecksEnabled(boolean enabled) {
		this.repeatingChecksEnabled = enabled;
		return this;
	}

	/**
	 * How often should UpdateLib check for updates? (Only works if repeatingChecksEnabled is true)
	 *
	 * @param interval Interval in milliseconds.
	 *
	 * @see #setRepeatingChecksEnabled(boolean)
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setCheckInterval(long interval) {
		this.checkInterval = interval;
		return this;
	}

	/**
	 * After how many milliseconds should we timeout the request to SpigotMC's API?
	 *
	 * @param timeout Timeout in milliseconds.
	 *
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setConnectionTimeout(int timeout) {
		this.connectionTimeout = timeout;
		return this;
	}

	/**
	 * Sets a consumer to run after UpdateLib has checked for an update.
	 *
	 * @param consumer Consumer to run after checking for an update.
	 *
	 * @since 2.1.0-SNAPSHOT
	 */
	public UpdateLibBuilder setConsumer(Consumer<UpdateResponse> consumer) {
		this.consumer = consumer;
		return this;
	}

	/**
	 * Builds a new instance of {@link UpdateLib}.
	 *
	 * @return Instance of {@link UpdateLib}
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLib build() {
		return new UpdateLib(resourceId, currentVersion, repeatingChecksEnabled, checkInterval, connectionTimeout, consumer);
	}

}
