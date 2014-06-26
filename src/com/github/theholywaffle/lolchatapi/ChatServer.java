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
package com.github.theholywaffle.lolchatapi;

/**
 * Represents a regions chatserver
 *
 */
public enum ChatServer {

	EUW("chat.euw1.lol.riotgames.com"),
	NA("chat.na1.lol.riotgames.com"),
	EUNE("chat.eun1.riotgames.com"),
	PBE("chat.pbe1.lol.riotgames.com"),
	OCE("chat.oc1.lol.riotgames.com"),
	TW("chattw.lol.garenanow.com"),
	TH("chatth.lol.garenanow.com"),
	PH("chatph.lol.garenanow.com"),
	VN("chatvn.lol.garenanow.com");

	String host;

	ChatServer(String host) {
		this.host = host;
	}

}
