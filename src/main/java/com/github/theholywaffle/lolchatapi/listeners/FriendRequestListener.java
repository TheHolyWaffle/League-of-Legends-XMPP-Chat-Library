package com.github.theholywaffle.lolchatapi.listeners;

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


public interface FriendRequestListener {

	/**
	 * Gets called when a user tries to add you to his friendslist.
	 * 
	 * @param userId
	 *            The user who wants to add you (e.g. sum12345678@pvp.net)
	 * @return True if you want to add this user, false if you want to ignore
	 *         his request
	 */
	public boolean onFriendRequest(String userId);

}
