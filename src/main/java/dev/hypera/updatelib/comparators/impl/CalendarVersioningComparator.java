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

package dev.hypera.updatelib.comparators.impl;

import dev.hypera.updatelib.comparators.IVersionComparator;
import dev.hypera.updatelib.exceptions.VersionComparisonFailureException;
import dev.hypera.updatelib.objects.enums.Status;
import org.jetbrains.annotations.NotNull;

/**
 * Calendar version comparator.
 *
 * @author Joshua Sing <joshua@hypera.dev>
 */
public class CalendarVersioningComparator implements IVersionComparator {

	/**
	 * Compares two versions.
	 * @param currentVersion Current version.
	 * @param distributedVersion Distributed version.
	 * @return Version status.
	 * @throws VersionComparisonFailureException if something went wrong while comparing the two versions.
	 */
	@Override
	public @NotNull Status compareVersions(@NotNull String currentVersion, @NotNull String distributedVersion) throws VersionComparisonFailureException {
		try {
			return currentVersion.compareTo(distributedVersion) <= 0 ? Status.UNAVAILABLE : Status.AVAILABLE;
		} catch (Exception ex) {
			throw new VersionComparisonFailureException(ex);
		}
	}

	public static void main(String[] args) {
		System.out.println();
	}

}
