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
