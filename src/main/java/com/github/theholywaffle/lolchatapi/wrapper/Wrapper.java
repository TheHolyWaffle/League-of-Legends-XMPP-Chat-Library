/*******************************************************************************
 * Copyright (c) 2014 Bert De Geyter (https://github.com/TheHolyWaffle).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Bert De Geyter (https://github.com/TheHolyWaffle)
 ******************************************************************************/
package com.github.theholywaffle.lolchatapi.wrapper;

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
