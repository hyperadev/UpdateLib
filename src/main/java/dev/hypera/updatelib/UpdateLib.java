/*
 * UpdateLib - A simple update checking library for Minecraft Plugins.
 * Copyright (c) 2021 Joshua Sing <joshua@hypera.dev>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.hypera.updatelib;

import dev.hypera.updatelib.comparators.IVersionComparator;
import dev.hypera.updatelib.exceptions.UpdateLibException;
import dev.hypera.updatelib.objects.UpdateStatus;
import dev.hypera.updatelib.objects.enums.Status;
import dev.hypera.updatelib.resolvers.IVersionResolver;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * UpdateLib main class
 *
 * @author Joshua Sing <joshua@hypera.dev>
 */
public class UpdateLib {

	private static final String VERSION = "4.0.0";

	private final long resourceId;
	private final String currentVersion;
	private final int timeout;
	private final IVersionResolver versionResolver;
	private final IVersionComparator versionComparator;
	private final Consumer<UpdateStatus> statusHandler;

	private UpdateStatus lastStatus = UpdateStatus.DEFAULT;
	private long lastCheck = 0L;

	@Internal
	protected UpdateLib(long resourceId, String currentVersion, int timeout, boolean repeatingChecks, long interval, IVersionResolver versionResolver, IVersionComparator versionComparator, Consumer<UpdateStatus> statusHandler) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.timeout = timeout;
		this.versionResolver = versionResolver;
		this.versionComparator = versionComparator;
		this.statusHandler = statusHandler;

		check();

		if (repeatingChecks) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					check();
				}
			}, interval);
		}
	}

	/**
	 * Create a new {@link UpdateLibBuilder} instance.
	 * @return New {@link UpdateLibBuilder} instance.
	 */
	public static UpdateLibBuilder builder() {
		return UpdateLibBuilder.create();
	}

	/**
	 * Get the current version of UpdateLib.
	 * @return Current UpdateLib version.
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Checks for an update.
	 * @return {@link CompletableFuture<UpdateStatus>}
	 */
	public CompletableFuture<UpdateStatus> check() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				String distributedVersion = versionResolver.getVersion(this, resourceId);
				Status comparison = versionComparator.compareVersions(currentVersion, distributedVersion);

				lastStatus = new UpdateStatus(currentVersion, distributedVersion, comparison);
				lastCheck = Instant.now().toEpochMilli();

				statusHandler.accept(lastStatus);
				return lastStatus;
			} catch (UpdateLibException ex) {
				throw new IllegalStateException(ex);
			}
		});
	}

	/**
	 * Get the last update status.
	 * @return Last update status.
	 */
	public UpdateStatus getLastStatus() {
		return lastStatus;
	}

	/**
	 * Get the last time UpdateLib checked for an update.
	 * @return Last check time.
	 */
	public long getLastCheck() {
		return lastCheck;
	}

	/**
	 * Get the http connection timeout.
	 * @return Connection timeout.
	 */
	@Internal
	public int getTimeout() {
		return timeout;
	}

}
