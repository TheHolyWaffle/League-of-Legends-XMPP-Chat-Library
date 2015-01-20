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

import static com.github.theholywaffle.lolchatapi.ChatServer.LoginMethod.GARENA;
import static com.github.theholywaffle.lolchatapi.ChatServer.LoginMethod.RIOT;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Represents a regions chatserver.
 *
 */
public enum ChatServer {

	/**
	 * Brazil
	 */
	BR("chat.br.lol.riotgames.com", "br.api.pvp.net", RIOT),
	/**
	 * Europe Nordic and East
	 */
	EUNE("chat.eun1.lol.riotgames.com", "eune.api.pvp.net", RIOT),
	/**
	 * Europe West
	 */
	EUW("chat.euw1.lol.riotgames.com", "euw.api.pvp.net", RIOT),
	/**
	 * Indonesia
	 */
	ID("chatid.lol.garenanow.com", null, GARENA),
	/**
	 * Korea
	 */
	KR("chat.kr.lol.riotgames.com", "kr.api.pvp.net", RIOT),
	/**
	 * Latin America North
	 */
	LAN("chat.la1.lol.riotgames.com", "lan.api.pvp.net", RIOT),
	/**
	 * Latin America South
	 */
	LAS("chat.la2.lol.riotgames.com", "las.api.pvp.net", RIOT),
	/**
	 * North-America
	 */
	NA("chat.na1.lol.riotgames.com", "na.api.pvp.net", RIOT),	
	/**
	 * North-America #2
	 */
	NA2("chat.na2.lol.riotgames.com", "na.api.pvp.net", RIOT),
	/**
	 * Oceania
	 */
	OCE("chat.oc1.lol.riotgames.com", "oce.api.pvp.net", RIOT),
	/**
	 * Public Beta Environment
	 */
	PBE("chat.pbe1.lol.riotgames.com", null, RIOT),
	/**
	 * Phillipines
	 */
	PH("chatph.lol.garenanow.com", null, GARENA),
	/**
	 * Russia
	 */
	RU("chat.ru.lol.riotgames.com", "ru.api.pvp.net", RIOT),
	/**
	 * South-East Asia
	 */
	SEA("chat.lol.garenanow.com", null, GARENA),
	/**
	 * Thailand
	 */
	TH("chatth.lol.garenanow.com", null, GARENA),
	/**
	 * Turkey
	 */
	TR("chat.tr.lol.riotgames.com", "tr.api.pvp.net", RIOT),
	/**
	 * Taiwan
	 */
	TW("chattw.lol.garenanow.com", null, GARENA),
	/**
	 * Vietnam
	 */
	VN("chatvn.lol.garenanow.com", null, GARENA);

	public String api;
	public String host;
	public LoginMethod loginMethod;

	ChatServer(String host, String api, LoginMethod loginMethod) {
		this.host = host;
		this.api = api;
		this.loginMethod = loginMethod;
	}

	public String getApiRegion()
	{
		if (api == null)
			throw new NullPointerException("Riot API is not supported for this region.");

		return api.split("\\.")[0].toLowerCase();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public enum LoginMethod {

		GARENA(new GarenaLogin()),
		RIOT(new RiotLogin());

		public ILoginMethod method;

		LoginMethod(ILoginMethod method) {
			this.method = method;
		}

		public void login(XMPPConnection connection, String username,
				String password, boolean replaceLeague) {
			method.login(connection, username, password, replaceLeague);
		}
	}

}
