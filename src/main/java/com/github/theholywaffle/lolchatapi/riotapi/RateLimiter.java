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

import java.util.LinkedList;

public class RateLimiter {

	private final LinkedList<Long> list = new LinkedList<Long>();

	private final int amount;
	private final int timespan;

	public RateLimiter(int amount, int timespan) {
		this.amount = amount;
		this.timespan = timespan;
	}

	public void acquire() {
		remove();
		while (list.size() >= amount) {
			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
			}
			remove();
		}
	}

	public void enter() {
		list.addFirst(System.currentTimeMillis() + timespan);
	}

	private void remove() {
		boolean searching = true;
		while (searching && list.size() > 0) {
			final long element = list.getLast();
			if (element < System.currentTimeMillis()) {
				list.removeLast();
			} else {
				searching = false;
			}
		}
	}

}
