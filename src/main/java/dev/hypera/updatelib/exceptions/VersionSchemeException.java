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

package dev.hypera.updatelib.exceptions;

/**
 * Version Scheme Exception.
 *
 * @since 3.0.0-SNAPSHOT
 */
public class VersionSchemeException extends IllegalStateException {

	public VersionSchemeException() {
		super();
	}

	public VersionSchemeException(String s) {
		super(s);
	}

	public VersionSchemeException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public VersionSchemeException(Throwable throwable) {
		super(throwable);
	}

}
