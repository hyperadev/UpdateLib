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
import dev.hypera.updatelib.comparators.impl.SemanticVersioningComparator;
import dev.hypera.updatelib.objects.UpdateStatus;
import dev.hypera.updatelib.resolvers.IVersionResolver;
import dev.hypera.updatelib.resolvers.impl.LegacySpigotVersionResolver;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * UpdateLib builder
 *
 * @author Joshua Sing <joshua@hypera.dev>
 */
public class UpdateLibBuilder {

	private long resourceId = -1;
	private String currentVersion = null;
	private boolean repeatingChecks = true;
	private long checkInterval = TimeUnit.HOURS.toMillis(2);
	private int connectionTimeout = 10000;
	private IVersionResolver versionResolver = new LegacySpigotVersionResolver();
	private IVersionComparator versionComparator = new SemanticVersioningComparator();
	private Consumer<UpdateStatus> statusHandler = status -> {};

	/**
	 * Creates a new {@link UpdateLibBuilder} instance.
	 * @return New {@link UpdateLibBuilder} instance.
	 */
	public static @NotNull UpdateLibBuilder create() {
		return new UpdateLibBuilder();
	}

	/**
	 * Sets the resource identifier. (required)
	 * @param resourceId Resource identifier.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder resource(long resourceId) {
		this.resourceId = resourceId;
		return this;
	}

	/**
	 * Sets the current version. (required)
	 * @param version Current version.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder version(@NotNull String version) {
		this.currentVersion = version;
		return this;
	}

	/**
	 * Disable repeating update checks.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder disableRepeatingChecks() {
		this.repeatingChecks = false;
		return this;
	}

	/**
	 * Sets the update check interval.
	 * @param interval Interval.
	 * @param unit Time unit.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder interval(long interval, @NotNull TimeUnit unit) {
		this.checkInterval = unit.toMillis(interval);
		return this;
	}

	/**
	 * Sets the http connection timeout for UpdateLib.
	 * @param timeout Connection/read timeout.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder timeout(int timeout) {
		this.connectionTimeout = timeout;
		return this;
	}

	/**
	 * Sets the version resolver to be used.
	 * @param versionResolver Version resolver.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder resolver(@NotNull IVersionResolver versionResolver) {
		this.versionResolver = versionResolver;
		return this;
	}

	/**
	 * Sets the version comparator to be used.
	 * @param versionComparator Version comparator.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder comparator(@NotNull IVersionComparator versionComparator) {
		this.versionComparator = versionComparator;
		return this;
	}

	/**
	 * Sets the status handler.
	 * @param statusHandler Status handler.
	 * @return Current {@link UpdateLibBuilder} instance.
	 */
	public @NotNull UpdateLibBuilder handler(@NotNull Consumer<UpdateStatus> statusHandler) {
		this.statusHandler = statusHandler;
		return this;
	}

	/**
	 * Builds a new {@link UpdateLib} instance using the provided settings.
	 * @return New {@link UpdateLib} instance.
	 * @throws IllegalStateException if resourceId or currentVersion are not provided.
	 */
	public @NotNull UpdateLib build() {
		if (resourceId == -1L || null == currentVersion) {
			throw new IllegalStateException("resourceId and currentVersion cannot be null.");
		} else {
			return new UpdateLib(resourceId, currentVersion, connectionTimeout, repeatingChecks, checkInterval, versionResolver, versionComparator, statusHandler);
		}
	}

}
