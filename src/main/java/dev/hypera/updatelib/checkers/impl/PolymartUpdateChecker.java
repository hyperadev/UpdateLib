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

package dev.hypera.updatelib.checkers.impl;

import dev.hypera.updatelib.checkers.UpdateChecker;
import dev.hypera.updatelib.data.CheckData;
import dev.hypera.updatelib.exceptions.InvalidResourceException;
import dev.hypera.updatelib.objects.UpdateStatus;
import dev.hypera.updatelib.objects.UpdateStatusBuilder;
import dev.hypera.updatelib.utils.ReaderUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PolymartUpdateChecker implements UpdateChecker {

	private static final String URL_FORMAT = "https://api.polymart.org/v1/getResourceInfo/?resource_id=%s";

	/**
	 * Check for an update using Polymart's API.
	 *
	 * @param data Check data.
	 *
	 * @return {@link UpdateStatus}
	 * @throws Exception Any exceptions that occur while checking for updates.
	 * @since 3.0.0-SNAPSHOT
	 */
	@Override
	public UpdateStatus check(CheckData data) throws Exception {
		URL url = new URL(String.format(URL_FORMAT, data.getResourceId()));

		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
		httpsURLConnection.setConnectTimeout(data.getTimeout());
		httpsURLConnection.setReadTimeout(data.getTimeout());

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
		JSONObject json = (JSONObject) JSONValue.parse(ReaderUtils.readAll(bufferedReader));
		bufferedReader.close();

		JSONObject response = (JSONObject) json.get("response");
		if(!((boolean) response.get("success")))
			throw new InvalidResourceException("Cannot find Polymart resource with id '" + data.getResourceId() + "'");

		String distributedVersion = ((JSONObject) ((JSONObject) ((JSONObject) response.get("resource")).get("updates")).get("latest")).get("version").toString();

		return UpdateStatusBuilder.create(distributedVersion, data.getCurrentVersion()).build();
	}

}
