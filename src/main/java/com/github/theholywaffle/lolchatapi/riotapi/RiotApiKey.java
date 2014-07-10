package com.github.theholywaffle.lolchatapi.riotapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */


public class RiotApiKey {

	private final String key;
	private final RateLimit rateLimit;

	/**
	 * Represents a Riot API Key with the default RateLimit.
	 * 
	 * @param key
	 *            Your Riot API Key.
	 * 
	 * @see RateLimit
	 */
	public RiotApiKey(String key) {
		this(key, RateLimit.DEFAULT);
	}

	/**
	 * Represents a Riot API Key with a RateLimit.
	 * 
	 * @param key
	 *            Your Riot API Key.
	 * @param rateLimit
	 *            Determines how often you can send API requests.
	 * 
	 * @see RateLimit
	 */
	public RiotApiKey(String key, RateLimit rateLimit) {
		this.key = key;
		this.rateLimit = rateLimit;
	}

	/**
	 * Returns the Riot API Key.
	 * 
	 * @return The Riot API Key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the RateLimit with this key.
	 * 
	 * @return The RateLimit
	 */
	public RateLimit getRateLimit() {
		return rateLimit;
	}

}
