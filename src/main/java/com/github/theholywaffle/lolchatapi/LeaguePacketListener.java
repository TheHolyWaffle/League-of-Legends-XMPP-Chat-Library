package com.github.theholywaffle.lolchatapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
		System.out.println("Friend Request Packet Arrived"); // TODO
		if (presence.getType().equals(Presence.Type.subscribe)) {
			if (requestListener.onFriendRequest(presence.getFrom())) {
				Presence newp = new Presence(Presence.Type.subscribed);
				newp.setTo(presence.getFrom());
				connection.sendPacket(newp);
				Presence subscription = new Presence(Presence.Type.subscribe);
				subscription.setTo(presence.getFrom());
				connection.sendPacket(subscription);
				System.out.println("Added friend");// TODO remove
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
