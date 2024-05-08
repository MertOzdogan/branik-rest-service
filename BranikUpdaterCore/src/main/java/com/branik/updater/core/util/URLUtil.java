package com.branik.updater.core.util;

import com.branik.updater.core.model.rest.LeagueQueryModel;
import org.apache.commons.lang3.StringUtils;

public class URLUtil {
    public static String getTeamNameInUrlVersion(String teamName) {
        if (teamName == null) {
            return "";
        }
        String teamUrl = StringUtils.stripAccents(teamName).replace(".", "-").replace(",","-");
        return teamUrl.endsWith("-") ? teamUrl.substring(0, teamUrl.length() - 1).replace(" ", "-") : teamUrl.replace(" ", "-");
    }

    public static LeagueQueryModel getLeagueQueryFromURL(String url) {
        String[] splittedURL = url.split("/");
        String[] yearAndSeasonArray = splittedURL[4].split("-");
        String year = yearAndSeasonArray[0];
        String season = yearAndSeasonArray[3];
        String leagueNumber = splittedURL[5].substring(0, 1);
        String leagueGroup = splittedURL[5].substring(2, 3);
        return LeagueQueryModel.builder().year(year).season(season).league(leagueNumber).group(leagueGroup).build();
    }
}
