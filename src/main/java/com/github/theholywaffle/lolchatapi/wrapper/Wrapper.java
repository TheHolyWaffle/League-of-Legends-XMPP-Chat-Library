package com.github.theholywaffle.lolchatapi.wrapper;

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

import org.jivesoftware.smack.XMPPConnection;

import com.github.theholywaffle.lolchatapi.LolChat;

public class Wrapper<E> {

	protected XMPPConnection con;
	private E object;
	protected LolChat api;

	protected Wrapper(LolChat api, XMPPConnection con, E object) {
		this.con = con;
		this.object = object;
		this.api = api;
		if (object == null) {
			System.err.println("Object " + object + " is null");
		}
	}

	protected E get() {
		return object;
	}

}
