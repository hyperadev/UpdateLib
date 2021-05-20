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

package dev.hypera.updatelib.utils;

import dev.hypera.updatelib.annotations.Internal;

@Internal
public class Check {

	/**
	 * Check if object is null, if so, throw an IllegalArgumentException.
	 *
	 * @param name   Name of the object.
	 * @param object Object.
	 *
	 * @since 3.0.0-SNAPSHOT
	 */
	public static void notNull(String name, Object object) {
		if(null == object)
			fail(name + " cannot be null");
	}

	/**
	 * Check if objects are null, if so, throw an IllegalArgumentException.
	 *
	 * @param names   Object names.
	 * @param objects Objects to check.
	 *
	 * @since 3.1.2-SNAPSHOT
	 */
	public static void notNull(String[] names, Object... objects) {
		for(int i = 0; i < objects.length; i++) {
			if(null == objects[i])
				fail(names[i] + " cannot be null.");
		}
	}

	/**
	 * Throw an IllegalArgumentException.
	 *
	 * @param message Message.
	 *
	 * @since 3.0.0-SNAPSHOT
	 */
	private static void fail(String message) {
		throw new IllegalArgumentException(message);
	}

}
