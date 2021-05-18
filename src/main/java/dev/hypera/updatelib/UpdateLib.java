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

import dev.hypera.updatelib.annotations.RunAsync;
import dev.hypera.updatelib.checkers.UpdateChecker;
import dev.hypera.updatelib.data.CheckData;
import dev.hypera.updatelib.objects.UpdateStatus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UpdateLib {

	private final static String VERSION = "3.1.0-SNAPSHOT"; // Current UpdateLib version.

	private final long resourceId;
	private final String currentVersion;
	private final int connectionTimeout;

	private final UpdateChecker updateChecker;

	private final Consumer<UpdateStatus> completeAction;
	private final Consumer<Throwable> errorHandler;

	private UpdateStatus lastStatus = null;
	private long lastCheck = 0L;

	/**
	 * UpdateLib Constructor - Used internally by UpdateLib.
	 *
	 * @param resourceId             Resource ID.
	 * @param currentVersion         Current version of the Resource.
	 * @param repeatingChecksEnabled If UpdateLib should check for updates periodically.
	 * @param interval               How often UpdateLib should check for updates.
	 * @param connectionTimeout      The amount of milliseconds UpdateLib should wait before timing out on requests.
	 * @param updateChecker          {@link UpdateChecker} to be used by UpdateLib to check for updates.
	 * @param completeAction         Action to run after UpdateLib has checked for an update.
	 * @param errorHandler           Consumer to run if UpdateLib encounters an exception.
	 */
	protected UpdateLib(long resourceId, String currentVersion, boolean repeatingChecksEnabled, long interval, int connectionTimeout, UpdateChecker updateChecker, Consumer<UpdateStatus> completeAction, Consumer<Throwable> errorHandler) {
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.connectionTimeout = connectionTimeout;
		this.updateChecker = updateChecker;
		this.completeAction = completeAction;
		this.errorHandler = errorHandler;

		Thread thread = new Thread(this::checkNow);
		thread.setName("UpdateLib-" + thread.getId());
		thread.start();

		if(repeatingChecksEnabled) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					checkNow();
				}
			}, interval);
		}
	}


	/**
	 * Start an update check. It's recommended to only run this asynchronously as it may take time to fetch data from
	 * the API.
	 *
	 * @return Response - Instance of {@link UpdateStatus}
	 * @since 2.0.0-SNAPSHOT
	 */
	@RunAsync
	public UpdateStatus checkNow() {
		try {
			lastStatus = updateChecker.check(new CheckData(resourceId, currentVersion, connectionTimeout));
			lastCheck = System.currentTimeMillis();
			if(null != completeAction)
				completeAction.accept(lastStatus);
		} catch (Exception ex) {
			if(null == errorHandler)
				ex.printStackTrace();
			else
				errorHandler.accept(ex);
		}

		return lastStatus;
	}

	/**
	 * Get last stored {@link UpdateStatus}. If UpdateLib hasn't checked for an update or the last check failed, this
	 * will return null.
	 *
	 * @return Last stored {@link UpdateStatus}
	 * @since 3.0.0-SNAPSHOT
	 */
	public UpdateStatus getLastStatus() {
		return lastStatus;
	}

	/**
	 * Get the last time UpdateLib checked for updates.
	 *
	 * @return Last check time in milliseconds.
	 * @since 2.0.0-SNAPSHOT
	 */
	public long getLastCheck() {
		return lastCheck;
	}

	/**
	 * Get the current version of UpdateLib.
	 *
	 * @return Current version of UpdateLib.
	 * @since 2.1.0-SNAPSHOT
	 */
	public static String getVersion() {
		return VERSION;
	}

}
