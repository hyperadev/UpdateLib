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

import dev.hypera.updatelib.annotations.Builder;
import dev.hypera.updatelib.checkers.UpdateChecker;
import dev.hypera.updatelib.checkers.impl.SpigotUpdateChecker;
import dev.hypera.updatelib.objects.UpdateStatus;
import dev.hypera.updatelib.utils.Check;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Builder
public class UpdateLibBuilder {

	private final long resourceId;
	private final String currentVersion;

	private boolean repeatingChecksEnabled = true;
	private long checkInterval = 2 * (60 * (60 * 1000)); // Defaults to 2 hours.
	private int connectionTimeout = 10000; // Defaults to 10 seconds.

	/**
	 * Defaults to using {@link SpigotUpdateChecker}.
	 */
	private UpdateChecker updateChecker = new SpigotUpdateChecker();

	private Consumer<UpdateStatus> completeAction = null;
	private Consumer<Throwable> errorHandler = null; // If error handler is not provided, stack traces will be printed by UpdateLib.


	/**
	 * UpdateLibBuilder Constructor - Used internally by UpdateLib.
	 *
	 * @param currentVersion Current version of the resource.
	 * @param resourceId     Resource ID.
	 */
	private UpdateLibBuilder(String currentVersion, long resourceId) {
		Check.notNull("currentVersion", currentVersion);
		this.currentVersion = currentVersion;
		this.resourceId = resourceId;
	}

	/**
	 * Create a new instance of {@link UpdateLibBuilder}
	 *
	 * @param currentVersion Current version of the resource.
	 * @param resourceId     Resource ID.
	 *
	 * @return Instance of {@link UpdateLibBuilder}
	 * @since 2.0.0-SNAPSHOT
	 */
	public static UpdateLibBuilder create(String currentVersion, long resourceId) {
		return new UpdateLibBuilder(currentVersion, resourceId);
	}

	/**
	 * Sets if UpdateLib should check for updates periodically.
	 *
	 * @param enabled If UpdateLib should check for updates periodically.
	 *
	 * @return {@link UpdateLibBuilder}
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setRepeatingChecksEnabled(boolean enabled) {
		this.repeatingChecksEnabled = enabled;
		return this;
	}

	/**
	 * Sets how often UpdateLib should check for updates? This is only needed if 'repeatingChecksEnabled' is true.
	 *
	 * @param time Time.
	 * @param unit TimeUnit.
	 *
	 * @return {@link UpdateLibBuilder}
	 * @see #setRepeatingChecksEnabled(boolean)
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setCheckInterval(long time, TimeUnit unit) {
		this.checkInterval = unit.toMillis(time);
		return this;
	}

	/**
	 * Sets the amount of milliseconds UpdateLib should wait before timing out on requests.
	 *
	 * @param timeout Timeout in milliseconds.
	 *
	 * @return {@link UpdateLibBuilder}
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setConnectionTimeout(int timeout) {
		this.connectionTimeout = timeout;
		return this;
	}

	/**
	 * Sets the {@link UpdateChecker} to be used by UpdateLib to check for updates.
	 *
	 * @param updateChecker Instance of an {@link UpdateChecker}
	 *
	 * @return {@link UpdateLibBuilder}
	 * @since 3.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setUpdateChecker(UpdateChecker updateChecker) {
		this.updateChecker = updateChecker;
		return this;
	}

	/**
	 * Sets an action to run after UpdateLib has checked for an update.
	 *
	 * @param action Consumer to run after checking for an update.
	 *
	 * @return {@link UpdateLibBuilder}
	 * @since 2.1.0-SNAPSHOT
	 */
	public UpdateLibBuilder setCompleteAction(Consumer<UpdateStatus> action) {
		this.completeAction = action;
		return this;
	}

	/**
	 * Sets an consumer to run if UpdateLib encounters an exception.
	 *
	 * @param handler Error handler.
	 *
	 * @return {@link UpdateLibBuilder}
	 * @since 3.0.0-SNAPSHOT
	 */
	public UpdateLibBuilder setErrorHandler(Consumer<Throwable> handler) {
		this.errorHandler = handler;
		return this;
	}

	/**
	 * Builds a new instance of {@link UpdateLib}
	 *
	 * @return Instance of {@link UpdateLib}
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateLib build() {
		return new UpdateLib(resourceId, currentVersion, repeatingChecksEnabled, checkInterval, connectionTimeout, updateChecker, completeAction, errorHandler);
	}

}
