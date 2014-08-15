package com.github.theholywaffle.lolchatapi.listeners;

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


public interface ConnectionListener {

	/**
	 * Notification that the connection was closed normally or that the
	 * reconnection process has been aborted.
	 */
	public void connectionClosed();

	/**
	 * Notification that the connection was closed due to an exception. When
	 * abruptly disconnected it is possible for the connection to try
	 * reconnecting to the server.
	 * 
	 * @param e
	 *            the exception.
	 */
	public void connectionClosedOnError(Exception e);

	/**
	 * The connection will retry to reconnect in the specified number of
	 * seconds.
	 * 
	 * @param seconds
	 *            remaining seconds before attempting a reconnection.
	 */
	public void reconnectingIn(int seconds);

	/**
	 * An attempt to connect to the server has failed. The connection will keep
	 * trying reconnecting to the server in a moment.
	 * 
	 * @param e
	 *            the exception that caused the reconnection to fail.
	 */
	public void reconnectionFailed(Exception e);

	/**
	 * The connection has reconnected successfully to the server. Connections
	 * will reconnect to the server when the previous socket connection was
	 * abruptly closed.
	 */
	public void reconnectionSuccessful();

}
