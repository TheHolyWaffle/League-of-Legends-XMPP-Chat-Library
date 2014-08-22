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


import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class CustomPresence extends Presence {

	public CustomPresence(Type type, String status, int priority, Mode mode) {
		super(type, status, priority, mode);
	}

	private boolean invisible;

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	@Override
	public XmlStringBuilder toXML() {
		final XmlStringBuilder buf = new XmlStringBuilder();
		buf.halfOpenElement("presence");
		buf.xmlnsAttribute(getXmlns());
		buf.xmllangAttribute(getLanguage());
		addCommonAttributes(buf);
		if (invisible) {
			buf.attribute("type", "invisible");
		} else if (getType() != Type.available) {
			buf.attribute("type", getType());
		}
		buf.rightAngelBracket();

		buf.optElement("status", getStatus());
		if (getPriority() != Integer.MIN_VALUE) {
			buf.element("priority", Integer.toString(getPriority()));
		}
		if (getMode() != null && getMode() != Mode.available) {
			buf.element("show", getMode());
		}
		buf.append(getExtensionsXML());

		// Add the error sub-packet, if there is one.
		final XMPPError error = getError();
		if (error != null) {
			buf.append(error.toXML());
		}
		buf.closeElement("presence");

		return buf;
	}

}
