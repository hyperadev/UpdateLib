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

public class UpdateLibBuilder {

    private final long resourceId;
    private final String currentVersion;

    private boolean repeatingChecksEnabled = true;
    private long checkInterval = 2 * (60 * (60 * 1000)); // 2 Hours
    private int connectionTimeout = 5000; // 5 Seconds

    private UpdateLibBuilder(String currentVersion, long resourceId) {
        this.currentVersion = currentVersion;
        this.resourceId = resourceId;
    }

    /**
     * Creates a new instance of {@link UpdateLibBuilder}.
     *
     * @param currentVersion Current version of the resource.
     * @param resourceId SpigotMC Resource Id.
     * @return Instance of {@link UpdateLibBuilder}
     */
    public static UpdateLibBuilder create(String currentVersion, long resourceId) {
        return new UpdateLibBuilder(currentVersion, resourceId);
    }

    /**
     * Should UpdateLib keep checking for updates? (Time defined by checkInterval)
     *
     * @param enabled Repeating checks enabled.
     * @see #setCheckInterval(long)
     */
    public void setRepeatingChecksEnabled(boolean enabled) {
        this.repeatingChecksEnabled = enabled;
    }

    /**
     * How often should UpdateLib check for updates? (Only works if repeatingChecksEnabled is true)
     *
     * @param interval Interval in milliseconds.
     * @see #setRepeatingChecksEnabled(boolean)
     */
    public void setCheckInterval(long interval) {
        this.checkInterval = interval;
    }

    /**
     * After how many milliseconds should we timeout the request to SpigotMC's API?
     *
     * @param timeout Timeout in milliseconds.
     */
    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    /**
     * Builds a new instance of {@link UpdateLib}.
     *
     * @return Instance of {@link UpdateLib}
     */
    public UpdateLib build() {
        return new UpdateLib(resourceId, currentVersion, repeatingChecksEnabled, checkInterval, connectionTimeout);
    }

}
