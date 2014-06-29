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
