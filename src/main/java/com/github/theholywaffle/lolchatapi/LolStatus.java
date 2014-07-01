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

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.custommonkey.xmlunit.Diff;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

public class LolStatus {

	public enum Division {
		NONE,
		I,
		II,
		III,
		IV,
		V;
	}

	public enum GameStatus {
		TEAM_SELECT("teamSelect"),
		HOSTING_NORMAL_GAME("hostingNormalGame"),
		HOSTING_PRACTICE_GAME("hostingPracticeGame"),
		HOSTING_RANKED_GAME("hostingRankedGame"),
		IN_QUEUE("inQueue"),
		SPECTATING("spectating"),
		OUT_OF_GAME("outOfGame"),
		CHAMPION_SELECT("championSelect"),
		IN_GAME("inGame"),
		IN_TEAMBUILDER("inTeamBuilder");

		private String internal;

		GameStatus(String internal) {
			this.internal = internal;
		}

		public String internal() {
			return internal;
		}
	}

	public enum Queue {
		NONE,
		NORMAL,
		NORMAL_3x3,
		ODIN_UNRANKED,
		ARAM_UNRANKED_5x5,
		BOT,
		BOT_3x3,
		RANKED_SOLO_5x5,
		RANKED_TEAM_3x3,
		RANKED_TEAM_5x5,
		ONEFORALL_5x5,
		FIRSTBLOOD_1x1,
		FIRSTBLOOD_2x2;
	}

	public enum Tier {
		UNRANKED,
		BRONZE,
		SILVER,
		GOLD,
		PLATINUM,
		DIAMOND,
		CHALLENGER;
	}

	private enum XMLProperty {

		level,
		rankedLeagueDivision,
		rankedLosses,
		rankedRating,
		leaves,
		gameQueueType,
		skinname,
		profileIcon,
		rankedLeagueQueue,
		tier,
		rankedLeagueName,
		queueType,
		timeStamp,
		rankedWins,
		odinLeaves,
		dropInSpectateGameId,
		statusMsg,
		rankedLeagueTier,
		featuredGameData,
		odinWins,
		wins,
		gameStatus,
		isObservable;

		@Override
		public String toString() {
			return name();
		}

	}

	private static final XMLOutputter outputter = new XMLOutputter();

	private Document doc;

	/**
	 * Generate a default LoLStatus that can later be modified and be used to
	 * change the current LolStatus ({@link LolChat#setStatus(LolStatus)})
	 * 
	 */
	public LolStatus() {
		outputter
				.setFormat(outputter.getFormat().setExpandEmptyElements(false));
		doc = new Document(new Element("body"));
		for (XMLProperty p : XMLProperty.values()) {
			doc.getRootElement().addContent(new Element(p.toString()));
		}
	}

	/**
	 * This constructor is not intended for usage.
	 * 
	 * @param xml
	 *            An XML string
	 * @throws JDOMException
	 *             Is thrown when the xml string is invalid
	 * @throws IOException
	 *             Is thrown when the xml string is invalid
	 */
	public LolStatus(String xml) throws JDOMException, IOException {
		outputter
				.setFormat(outputter.getFormat().setExpandEmptyElements(false));
		SAXBuilder saxBuilder = new SAXBuilder();
		doc = saxBuilder.build(new StringReader(xml));
		for (Element e : doc.getRootElement().getChildren()) {
			boolean found = false;
			for (XMLProperty p : XMLProperty.values()) {
				if (p.name().equals(e.getName())) {
					found = true;
				}
			}
			if (!found) {
				System.err.println("XMLProperty \"" + e.getName()
						+ "\" not implemented yet!");
			}
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof LolStatus))
			return false;
		LolStatus otherStatus = (LolStatus) other;
		Diff diff;
		try {
			diff = new Diff(otherStatus.toString(), toString());
			return diff.similar();
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String get(XMLProperty p) {
		Element child = getElement(p);
		if (child == null) {
			return "";
		}
		return child.getValue();
	}

	// ///////////
	// GETTERS //
	// ///////////

	public int getDominionLeaves() {
		return getInt(XMLProperty.odinLeaves);
	}

	public int getDominionWins() {
		return getInt(XMLProperty.odinWins);
	}

	private Element getElement(XMLProperty p) {
		return doc.getRootElement().getChild(p.toString());
	}

	// ///////////
	// SETTERS //
	// ///////////

	public String getFeaturedGameData() {
		return get(XMLProperty.featuredGameData);
	}

	public String getGameQueueType() {
		return get(XMLProperty.gameQueueType);
	}

	public GameStatus getGameStatus() {
		String status = get(XMLProperty.gameStatus);
		if (!status.isEmpty()) {
			for (GameStatus s : GameStatus.values()) {
				if (s.internal.equals(status)) {
					return s;
				}
			}
			System.err
					.println("GameStatus " + status + " not implemented yet!");
		}
		return null;
	}

	private int getInt(XMLProperty p) {
		String value = get(p);
		if (value.isEmpty()) {
			return -1;
		}
		return Integer.parseInt(value);
	}

	public int getLevel() {
		return getInt(XMLProperty.level);
	}

	private long getLong(XMLProperty p) {
		String value = get(p);
		if (value.isEmpty()) {
			return -1L;
		}
		return Long.parseLong(value);
	}

	public int getNormalLeaves() {
		return getInt(XMLProperty.leaves);
	}

	// ///////////
	// GETTERS //
	// ///////////

	public int getNormalWins() {
		return getInt(XMLProperty.wins);
	}

	public int getProfileIconId() {
		return getInt(XMLProperty.profileIcon);
	}

	/**
	 * Seems like an unused variable of Riot
	 * 
	 * @return Empty string
	 */
	@Deprecated
	public String getQueueType() {
		return get(XMLProperty.queueType);
	}

	public Division getRankedLeagueDivision() {
		String div = get(XMLProperty.rankedLeagueDivision);
		if (!div.isEmpty()) {
			return Division.valueOf(div);
		}
		return Division.NONE;
	}

	public String getRankedLeagueName() {
		return get(XMLProperty.rankedLeagueName);
	}

	public String getRankedLeagueQueue() {
		return get(XMLProperty.rankedLeagueQueue);
	}

	public Tier getRankedLeagueTier() {
		String tier = get(XMLProperty.rankedLeagueTier);
		if (!tier.isEmpty()) {
			return Tier.valueOf(tier);
		}
		return Tier.UNRANKED;
	}

	/**
	 * Seems like an unused variable of Riot
	 * 
	 * @return 0
	 */
	@Deprecated
	public int getRankedLosses() {
		return getInt(XMLProperty.rankedLosses);
	}

	/**
	 * Seems like an unused variable of Riot
	 * 
	 * @return 0
	 */
	@Deprecated
	public int getRankedRating() {
		return getInt(XMLProperty.rankedRating);
	}

	public int getRankedWins() {
		return getInt(XMLProperty.rankedWins);
	}

	public String getSkin() {
		return get(XMLProperty.skinname);
	}

	public String getSpectatedGameId() {
		return get(XMLProperty.dropInSpectateGameId);
	}

	public String getStatusMessage() {
		return get(XMLProperty.statusMsg);
	}

	public Tier getTier() {
		String tier = get(XMLProperty.tier);
		if (!tier.isEmpty()) {
			return Tier.valueOf(tier);
		}
		return Tier.UNRANKED;
	}

	public Date getTimestamp() {
		long l = getLong(XMLProperty.timeStamp);
		if (l > 0) {
			return new Date(l);
		}
		return null;
	}

	public boolean isObservable() {
		return get(XMLProperty.isObservable).equals("ALL");
	}

	public LolStatus setDominionLeaves(int leaves) {
		setElement(XMLProperty.odinLeaves, leaves);
		return this;
	}

	public LolStatus setDominionWins(int wins) {
		setElement(XMLProperty.odinWins, wins);
		return this;
	}

	private void setElement(XMLProperty p, int value) {
		setElement(p, String.valueOf(value));
	}

	private void setElement(XMLProperty p, long value) {
		setElement(p, String.valueOf(value));
	}

	// ///////////
	// SETTERS //
	// ///////////

	private void setElement(XMLProperty p, String value) {
		getElement(p).setText(value);
	}

	public LolStatus setFeaturedGameData(String data) {
		setElement(XMLProperty.featuredGameData, data);
		return this;
	}

	public LolStatus setGameQueueType(Queue q) {
		return setGameQueueType(q.name());
	}

	public LolStatus setGameQueueType(String q) {
		setElement(XMLProperty.gameQueueType, q);
		return this;
	}

	public LolStatus setGameStatus(GameStatus s) {
		setElement(XMLProperty.gameStatus, s.internal);
		return this;
	}

	public LolStatus setLevel(int level) {
		setElement(XMLProperty.level, level);
		return this;
	}

	public LolStatus setNormalLeaves(int leaves) {
		setElement(XMLProperty.leaves, leaves);
		return this;
	}

	public LolStatus setNormalWins(int wins) {
		setElement(XMLProperty.wins, wins);
		return this;
	}

	public LolStatus setObservable() {
		setElement(XMLProperty.isObservable, "ALL");
		return this;
	}

	public LolStatus setProfileIconId(int id) {
		setElement(XMLProperty.profileIcon, id);
		return this;
	}

	@Deprecated
	public LolStatus setQueueType(Queue q) {
		setElement(XMLProperty.queueType, q.name());
		return this;
	}

	public LolStatus setRankedLeagueDivision(Division d) {
		setElement(XMLProperty.rankedLeagueDivision, d.name());
		return this;
	}

	public LolStatus setRankedLeagueName(String name) {
		setElement(XMLProperty.rankedLeagueName, name);
		return this;
	}

	public LolStatus setRankedLeagueQueue(Queue q) {
		setElement(XMLProperty.rankedLeagueQueue, q.name());
		return this;
	}

	public LolStatus setRankedLeagueTier(Tier t) {
		setElement(XMLProperty.rankedLeagueTier, t.name());
		return this;
	}

	@Deprecated
	public LolStatus setRankedLosses(int losses) {
		setElement(XMLProperty.rankedLosses, losses);
		return this;
	}

	@Deprecated
	public LolStatus setRankedRating(int rating) {
		setElement(XMLProperty.rankedRating, rating);
		return this;
	}

	public LolStatus setRankedWins(int wins) {
		setElement(XMLProperty.rankedWins, wins);
		return this;
	}

	public LolStatus setSkin(String name) {
		setElement(XMLProperty.skinname, name);
		return this;
	}

	public LolStatus setSpectatedGameId(String id) {
		setElement(XMLProperty.dropInSpectateGameId, id);
		return this;
	}

	public LolStatus setStatusMessage(String message) {
		setElement(XMLProperty.statusMsg, message);
		return this;
	}

	public LolStatus setTier(Tier t) {
		setElement(XMLProperty.tier, t.name());
		return this;
	}

	public LolStatus setTimestamp(Date date) {
		return setTimestamp(date.getTime());
	}

	public LolStatus setTimestamp(long date) {
		setElement(XMLProperty.timeStamp, date);
		return this;
	}

	@Override
	public String toString() {
		return outputter.outputString(doc.getRootElement());
	}

}
