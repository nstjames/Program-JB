package org.goldrenard.jb.model;
/* Program AB Reference AIML 2.0 implementation
        Copyright (C) 2013 ALICE A.I. Foundation
        Contact: info@alicebot.org

        This library is free software; you can redistribute it and/or
        modify it under the terms of the GNU Library General Public
        License as published by the Free Software Foundation; either
        version 2 of the License, or (at your option) any later version.

        This library is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        Library General Public License for more details.

        You should have received a copy of the GNU Library General Public
        License along with this library; if not, write to the
        Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
        Boston, MA  02110-1301, USA.
*/

import lombok.Getter;
import lombok.Setter;
import org.goldrenard.jb.Bot;
import org.goldrenard.jb.Sraix;
import org.goldrenard.jb.configuration.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * implements AIML Sets
 */
@Getter
@Setter
public class AIMLSet extends HashSet<String> implements NamedEntity {

    private static final Logger log = LoggerFactory.getLogger(AIMLSet.class);

    private static final Pattern DIGITS_PATTERN = Pattern.compile("[0-9]+");

    private String setName;

    private int maxLength = 1; // there are no empty sets

    private String host; // for external sets
    private String botId; // for external sets
    private boolean isExternal = false;
    private Bot bot;
    private HashSet<String> inCache = new HashSet<>();
    private HashSet<String> outCache = new HashSet<>();

    /**
     * constructor
     *
     * @param name name of set
     */
    public AIMLSet(String name, Bot bot) {
        super();
        this.bot = bot;
        this.setName = name.toLowerCase();
        if (setName.equals(Constants.natural_number_set_name)) {
            maxLength = 1;
        }
    }

    public boolean contains(String s) {
        if (isExternal && bot.getConfiguration().isEnableExternalSets()) {
            if (inCache.contains(s)) {
                return true;
            }
            if (outCache.contains(s)) {
                return false;
            }
            String[] split = s.split(" ");
            if (split.length > maxLength) {
                return false;
            }
            String query = Constants.set_member_string + setName.toUpperCase() + " " + s;
            String response = Sraix.sraix(null, bot, query, "false", null, host, botId, null, "0");
            if ("true".equals(response)) {
                inCache.add(s);
                return true;
            } else {
                outCache.add(s);
                return false;
            }
        } else if (setName.equals(Constants.natural_number_set_name)) {
            return DIGITS_PATTERN.matcher(s).matches();
        }
        return super.contains(s);
    }

    public void writeAIMLSet() {
        log.info("Writing AIML Set {}", setName);
        try (FileWriter stream = new FileWriter(bot.getSetsPath() + "/" + setName + ".txt")) {
            try (BufferedWriter out = new BufferedWriter(stream)) {
                for (String p : this) {
                    out.write(p.trim());
                    out.newLine();
                }
            }
        } catch (Exception e) {
            log.error("Write error", e);
        }
    }

    @Override
    public String getName() {
        return setName;
    }
}
