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

package dev.hypera.updatelib.internal.tasks;

import dev.hypera.updatelib.internal.UpdateResponse;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

	private static final String URL_BASE = "https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=";

	/**
	 * Check for an update. - Used internally by UpdateLib.
	 *
	 * @param resourceId     The ID of the SpigotMC resource.
	 * @param currentVersion The current version of the plugin.
	 * @param timeout        How long we should wait before timing out the connection or read.
	 *
	 * @return Update response.
	 * @throws Exception Any errors that occur while checking for an update.
	 * @since 2.0.0-SNAPSHOT
	 */
	public UpdateResponse check(long resourceId, String currentVersion, int timeout) throws Exception {
		URL url = new URL(URL_BASE + resourceId);

		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
		httpsURLConnection.setConnectTimeout(timeout);
		httpsURLConnection.setReadTimeout(timeout);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
		JSONObject json = (JSONObject) JSONValue.parse(readAll(bufferedReader));
		String spigotVersion = (String) json.get("current_version");

		if(null == spigotVersion)
			return null;
		return new UpdateResponse(new ComparableVersion(currentVersion).compareTo(new ComparableVersion(spigotVersion)) < 0, currentVersion, spigotVersion);
	}

	/**
	 * Read all from a BufferedReader and return it as a string.
	 *
	 * @param bufferedReader BufferedReader to read from.
	 *
	 * @return Contents of reader.
	 * @throws Exception Any errors that occur during execution.
	 * @since 2.0.0-SNAPSHOT
	 */
	private String readAll(BufferedReader bufferedReader) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while((line = bufferedReader.readLine()) != null)
			stringBuilder.append(line).append("\n");
		return stringBuilder.toString();
	}

}
