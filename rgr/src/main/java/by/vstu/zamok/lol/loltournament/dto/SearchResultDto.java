package by.vstu.zamok.lol.loltournament.dto;

import by.vstu.zamok.lol.loltournament.entity.Player;
import by.vstu.zamok.lol.loltournament.entity.Team;
import by.vstu.zamok.lol.loltournament.entity.Tournament;
import by.vstu.zamok.lol.loltournament.entity.User;

import java.util.List;

public class SearchResultDto {
    private List<User> users;
    private List<Tournament> tournaments;
    private List<Team> teams;
    private List<Player> players;
    public SearchResultDto() {}

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public List<Tournament> getTournaments() { return tournaments; }
    public void setTournaments(List<Tournament> tournaments) { this.tournaments = tournaments; }

    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
}
