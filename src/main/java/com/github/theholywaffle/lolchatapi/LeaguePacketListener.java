package com.github.theholywaffle.lolchatapi;

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


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;

public class LeaguePacketListener implements PacketListener {

	private XMPPConnection connection;
	private FriendRequestListener requestListener;
	private LolChat api;

	public LeaguePacketListener(LolChat api, XMPPConnection connection) {
		this.api = api;
		this.connection = connection;
	}

	public void processPacket(Packet packet) throws NotConnectedException {
		if (api.getFriendRequestPolicy() != FriendRequestPolicy.MANUAL) {
			return;
		}
		Presence presence = (Presence) packet;
		if (presence.getType().equals(Presence.Type.subscribe)) {
			if (requestListener.onFriendRequest(presence.getFrom())) {
				Presence newp = new Presence(Presence.Type.subscribed);
				newp.setTo(presence.getFrom());
				connection.sendPacket(newp);
				Presence subscription = new Presence(Presence.Type.subscribe);
				subscription.setTo(presence.getFrom());
				connection.sendPacket(subscription);
			} else {
				Presence newp = new Presence(Presence.Type.unsubscribed);
				newp.setTo(presence.getFrom());
				connection.sendPacket(newp);
			}
		}
	}

	public void setFriendRequestListener(FriendRequestListener requestListener) {
		this.requestListener = requestListener;
	}

}
