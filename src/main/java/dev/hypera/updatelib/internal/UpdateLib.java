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

package dev.hypera.updatelib.internal;

import dev.hypera.updatelib.internal.tasks.UpdateChecker;
import dev.hypera.updatelib.internal.tasks.UpdateTask;

import java.util.Timer;
import java.util.function.Consumer;

public class UpdateLib {

	private final static String version = "2.1.1";

	private final long resourceId;
	private final String currentVersion;
	private final int connectionTimeout;
	private final Consumer<UpdateResponse> consumer;

	private long lastCheck = 0L;
	private UpdateResponse lastResponse = null;

	public UpdateLib(long resourceId, String currentVersion, boolean repeatingChecksEnabled, long checkInterval, int connectionTimeout, Consumer<UpdateResponse> consumer) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.connectionTimeout = connectionTimeout;
		this.consumer = consumer;

		Thread thread = new Thread(() -> {
			try {
				checkNow();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		thread.setName("UpdateLib-" + thread.getId());
		thread.start();

		if(repeatingChecksEnabled) {
			new Timer().schedule(new UpdateTask(resourceId, currentVersion, connectionTimeout, consumer, this), checkInterval);
		}
	}

	/**
	 * Checks for a update now.
	 *
	 * @return Response (instance of {@link UpdateResponse}).
	 * @throws Exception Any errors that occurred while checking.
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateResponse checkNow() throws Exception {
		lastCheck = System.currentTimeMillis();
		lastResponse = new UpdateChecker().check(resourceId, currentVersion, connectionTimeout);
		if(null != consumer)
			consumer.accept(getLastResponse());
		return lastResponse;
	}

	/**
	 * Get last {@link UpdateResponse} stored - If UpdateLib hasn't checked for updates yet, this will return null.
	 *
	 * @return Last {@link UpdateResponse} stored.
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateResponse getLastResponse() {
		return lastResponse;
	}

	/**
	 * Set last response. - Used internally by UpdateLib.
	 *
	 * @param lastResponse Last response.
	 *
	 * @since 2.0.0-SNAPSHOT
	 */
	public void setLastResponse(UpdateResponse lastResponse) {
		this.lastResponse = lastResponse;
	}

	/**
	 * Get the last time UpdateLib checked for an update.
	 *
	 * @return Last time in milliseconds.
	 * @since 2.0.0-SNAPSHOT
	 */
	public long getLastCheckTime() {
		return lastCheck;
	}

	/**
	 * Set last check time. - Used internally by UpdateLib.
	 *
	 * @param lastCheck Last check time in milliseconds.
	 *
	 * @since 2.0.0-SNAPSHOT
	 */
	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}

	/**
	 * Get UpdateLib version.
	 *
	 * @return Version.
	 * @since 2.1.0-SNAPSHOT
	 */
	public static String getVersion() {
		return version;
	}

}
