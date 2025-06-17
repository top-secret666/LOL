package by.vstu.zamok.lol.loltournament.controller;

import by.vstu.zamok.lol.loltournament.dto.SearchResultDto;
import by.vstu.zamok.lol.loltournament.entity.Player;
import by.vstu.zamok.lol.loltournament.entity.Team;
import by.vstu.zamok.lol.loltournament.entity.Tournament;
import by.vstu.zamok.lol.loltournament.entity.User;
import by.vstu.zamok.lol.loltournament.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/global")
    public ResponseEntity<SearchResultDto> globalSearch(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "5") int limit) {

        SearchResultDto result = new SearchResultDto();

        if (query == null || query.trim().isEmpty()) {
            result.setUsers(new ArrayList<>());
            result.setTournaments(new ArrayList<>());
            result.setTeams(new ArrayList<>());
            result.setPlayers(new ArrayList<>());
            return ResponseEntity.ok(result);
        }

        Pageable pageable = PageRequest.of(0, limit);

        List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, pageable).getContent();
        result.setUsers(users);

        List<Tournament> tournaments = tournamentRepository.findByNameContainingIgnoreCase(query, pageable);
        result.setTournaments(tournaments);

        List<Team> teams = teamRepository.findByNameContainingIgnoreCaseOrTagContainingIgnoreCase(
                query, query, pageable);
        result.setTeams(teams);

        List<Player> players = playerRepository.findByNicknameContainingIgnoreCaseOrRealNameContainingIgnoreCase(
                query, query, pageable);
        result.setPlayers(players);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, List<String>>> getSearchSuggestions(
            @RequestParam(required = false) String query) {
        Map<String, List<String>> suggestions = new HashMap<>();

        // Return empty suggestions if query is null or empty
        if (query == null || query.trim().isEmpty()) {
            suggestions.put("tournaments", new ArrayList<>());
            suggestions.put("teams", new ArrayList<>());
            suggestions.put("players", new ArrayList<>());
            return ResponseEntity.ok(suggestions);
        }

        if (query.length() >= 2) {
            Pageable limit = PageRequest.of(0, 5);

            List<String> tournamentNames = tournamentRepository.findByNameContainingIgnoreCase(query, limit)
                    .stream().map(Tournament::getName).toList();
            suggestions.put("tournaments", tournamentNames);

            List<String> teamNames = teamRepository.findByNameContainingIgnoreCaseOrTagContainingIgnoreCase(
                    query, query, limit).stream().map(Team::getName).toList();
            suggestions.put("teams", teamNames);

            List<String> playerNames = playerRepository.findByNicknameContainingIgnoreCaseOrRealNameContainingIgnoreCase(
                    query, query, limit).stream().map(Player::getNickname).toList();
            suggestions.put("players", playerNames);
        } else {
            // Return empty lists for queries shorter than 2 characters
            suggestions.put("tournaments", new ArrayList<>());
            suggestions.put("teams", new ArrayList<>());
            suggestions.put("players", new ArrayList<>());
        }

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/advanced")
    public ResponseEntity<Map<String, Object>> advancedSearch(
            @RequestParam(required = false) String tournaments,
            @RequestParam(required = false) String teams,
            @RequestParam(required = false) String players,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String rank,
            @RequestParam(defaultValue = "10") int limit) {

        Map<String, Object> results = new HashMap<>();
        Pageable pageable = PageRequest.of(0, limit);

        // Всегда инициализируем пустые списки
        results.put("tournaments", new ArrayList<>());
        results.put("players", new ArrayList<>());
        results.put("teams", new ArrayList<>());

        if (tournaments != null && !tournaments.isEmpty()) {
            List<Tournament> tournamentResults = new ArrayList<>();
            if (status != null && !status.isEmpty()) {
                try {
                    Tournament.TournamentStatus tournamentStatus = Tournament.TournamentStatus.valueOf(status.toUpperCase());
                    tournamentResults = tournamentRepository.findByNameContainingIgnoreCaseAndStatus(
                            tournaments, tournamentStatus, pageable);
                } catch (IllegalArgumentException e) {
                    tournamentResults = tournamentRepository.findByNameContainingIgnoreCase(tournaments, pageable);
                }
            } else {
                tournamentResults = tournamentRepository.findByNameContainingIgnoreCase(tournaments, pageable);
            }
            results.put("tournaments", tournamentResults);
        }

        if (players != null && !players.isEmpty()) {
            List<Player> playerResults = new ArrayList<>();
            if (role != null && !role.isEmpty()) {
                try {
                    Player.PlayerRole playerRole = Player.PlayerRole.valueOf(role.toUpperCase());
                    playerResults = playerRepository.findByNicknameContainingIgnoreCaseAndRole(
                            players, playerRole, pageable);
                } catch (IllegalArgumentException e) {
                    playerResults = playerRepository.findByNicknameContainingIgnoreCaseOrRealNameContainingIgnoreCase(
                            players, players, pageable);
                }
            } else {
                playerResults = playerRepository.findByNicknameContainingIgnoreCaseOrRealNameContainingIgnoreCase(
                        players, players, pageable);
            }
            results.put("players", playerResults);
    }

        if (teams != null && !teams.isEmpty()) {
            List<Team> teamResults = teamRepository.findByNameContainingIgnoreCaseOrTagContainingIgnoreCase(
                    teams, teams, pageable);
            results.put("teams", teamResults);
        }

        return ResponseEntity.ok(results);
    }


    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getSearchFilters() {
        Map<String, List<String>> filters = new HashMap<>();

        // Tournament statuses
        List<String> statuses = List.of("REGISTRATION", "ONGOING", "COMPLETED", "CANCELLED");
        filters.put("tournamentStatuses", statuses);

        // Player roles
        List<String> roles = List.of("Top", "Jungle", "Mid", "Adc", "Support");
        filters.put("playerRoles", roles);

        // Available ranks (from existing players)
        List<String> ranks = playerRepository.findDistinctRanks();
        filters.put("playerRanks", ranks);

        return ResponseEntity.ok(filters);
    }
}
