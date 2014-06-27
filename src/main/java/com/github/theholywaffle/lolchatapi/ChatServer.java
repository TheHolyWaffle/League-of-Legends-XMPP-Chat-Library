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

	/**
	 * North-America
	 */
	NA("chat.na1.lol.riotgames.com"),
	/**
	 * Europe West
	 */
	EUW("chat.euw1.lol.riotgames.com"),
	/**
	 * Europe Nordic and East
	 */
	EUNE("chat.eun1.riotgames.com"),
	/**
	 * Public Beta Environment
	 */
	PBE("chat.pbe1.lol.riotgames.com"),
	/**
	 * Oceania
	 */
	OCE("chat.oc1.lol.riotgames.com"),
	/**
	 * Brazil
	 */
	BR("chat.br.lol.riotgames.com"),
	/**
	 * Turkey
	 */
	TR("chat.tr.lol.riotgames.com"),
	/**
	 * Russia
	 */
	RU("chat.ru.lol.riotgames.com"),
	/**
	 * Latin America North
	 */
	LAN("chat.la1.lol.riotgames.com"),
	/**
	 * Latin America South
	 */
	LAS("chat.la2.lol.riotgames.com"),
	/**
	 * Taiwan
	 */
	TW("chattw.lol.garenanow.com"),
	/**
	 * Thailand
	 */
	TH("chatth.lol.garenanow.com"),
	/**
	 * Phillipines
	 */
	PH("chatph.lol.garenanow.com"),
	/**
	 * Vietnam
	 */
	VN("chatvn.lol.garenanow.com");

	String host;

	ChatServer(String host) {
		this.host = host;
	}

}
