# OMatch!
## Fall 2023 Software Engineering Final Project
*Jonathan Zhou, Michael Tu, Andrew Boyaciliger, Jake Stifelman*
<br><br>
<b>Repository Link:</b> https://github.com/jonathanzhou1/OMatch
<hr>

## OMatch! Description
OMatch! is a platform dedicated to creating fun and competitive 5 v 5 basketball games at the OMAC. The frontend interface allows users to create persistent profiles to store their information such as name, basketball position, and keep track of win-loss records, and find 5 v 5 matches with people of similar skill levels to them based on ELO rating and matchmaking algorithms on the backend. Through this, we hope that students and community members in Providence will be enabled to have optimized, fun games that they can plan out ahead of time with players that are less intimidating because of similar skill levels.

## How to Run Program
Currently, you will have to start the backend server by running the main program in 'Server.java'. You should see a message saying that `Server started at http://localhost:3232`. Then, run the frontend interface by going into the 'OMatch/omatch' directory and running `npm run dev` and going to `http://localhost:5173`.
<br><br>
*Note: For matchmaking, you will need a minimum of 10 users queued.*

## Backend Handlers

### Query Guide

1. `profile-add <name> <position>`
    1. The profile addition handler manages the addition of profiles to the backend database. Any players with exactly the same name and position will be treated as different players with a different ID, so it is imperative that you use the editing handler for editing or deleting players.
    2. The `name` query has to do with the player's name, taken in as a UTF-8 String.
    3. The `position` query has to do with the player's position, and it can be one of five possibile strings. Any other string put here will result in an error:
          1. `POINT_GUARD`
          2. `SHOOTING_GUARD`
          3. `SMALL_FORWARD`
          4. `POWER_FORWARD`
          5. `CENTER`
2. `profile-view <id>`
   1. The profile view handler retrieves all relevant data to the player, including, name, position, ELO ranking, and W/L ratio. 
   2. The `id` keyword is a randomized, unique ID for each player. 20 characters long. If no `id` keyword is present, the handler will return the list of all players
3. `profile-edit <action> <id> (name) (position)`
   1. The profile edit handler manages the editing and/or deleting of players.
   2. The `action` query has to do with whether the player in question is being edited or deleted. As of such, it can be one of two strings, "edit" or "delete". These strings are not case-sensitive
   3. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
   4. The `name` query is the player's new name, only recognized when the action is set to edit. This keyword is optional and the player's name will not be updated if it is not present.
   5. The `position` query is the player's updated position. These keywords are the same as for the `profile-add` handler. As with the name, this keyword is optional and will not be updated if it is not present.
4. `match-add <id>`
   1. The match addition handler manages player's being assigned to matches. It returns the match that the player was added to, which in turn contains all the team details.
   2. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
5. `match-view`
   1. Returns a list of match objects, each containing details about the court the match is taking place on, as well as teams and their respective players.
5. `queue-view`
   1. Returns either the queue or the position of a player within the queue
6. `queue-add <id>`
   1. Adds a player to the matchmaking queue. When the queue length is 10, the first 10 players in the queue will be dequeued into a match. Whether this has happened will be represented by the NewCourtAdded boolean, which will be true if a new court has been added, and false otherwise.
   2. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
     
### Example Queries

For the sake of brevity, some example queries will have the following two aliases instead of the entire object. This is because these objects are either very large, repititious, or both; and, therefore need to be aliased under a smaller name. They are the following:


1. `playerN`

```
    {
        "id":{ID},
        "losses":{Losses},
        "name":{name},
        "position":{Position},
        "skillLevel":{Skill Level},
        "wins":{Wins}
    }
```

2. `matchN`

```
{
    "outcome":"ONGOING",
    "team1":
        {
            "avgSkill":10.0,
            "players":
                [
                    {player1},
                    {player2},
                    {player3},
                    {player4},
                    {player5}
                ],
            "size":5
        },
    "team2":
        {
            "avgSkill":10.0,
            "players":
                [
                    {player6},
                    {player7},
                    {player8},
                    {player9},
                    {player10}
                ],
        "size":5
    }
```

#### `profile-add <name> <position> <id>`

<details>
    <summary>Successful requests</summary>
    <br>
1. profile-add?name=Josh Joshington&position=SMALL_FORWARD&id=a0Xd2wuFh9oGb3aJuv4K


```
    {
        "result":"success",
        "playerID":"a0Xd2wuFh9oGb3aJuv4K",
        "queries":
            ["name","position"]
    }
```

2. profile-add?name=Baka Sussy&position=CENTER&id=awWo0cH6jw4Ib8fD9Fgi

```
    {
        "result":"success",
        "playerID":"awWo0cH6jw4Ib8fD9Fgi",
        "queries":
            ["name","position"]
    }
```
</details>

#### `profile-view <id>`

<details>
    <summary>Successful requests</summary>
    <br>
1. profile-view?id=a0Xd2wuFh9oGb3aJuv4K

```
    {
        "result":"success",
        "player":
            {
                "id":"a0Xd2wuFh9oGb3aJuv4K",
                "name":"Josh Joshington",
                "skillLevel":9001.112,
                "wins":12,
                "losses":0,
                "Position":"SMALL_FORWARD"
            },
        "queries":
            ["id"]
    }
```

2. profile-view?id=awWo0cH6jw4Ib8fD9Fgi

```
    {
        "result":"success",
        "player":
            {
                "id":"awWo0cH6jw4Ib8fD9Fgi",
                "name":"Baka Sussy",
                "skillLevel":235,
                "wins":6,
                "losses":29,
                "Position":"CENTER"
            },
        "queries": ["id"]
    }
```

3. profile-view

```
    {
        "result":"success",
        "players":
            {
                "a0Xd2wuFh9oGb3aJuv4K":
                    {
                        "id":"a0Xd2wuFh9oGb3aJuv4K",
                        "losses":0,
                        "name":"Josh Joshington",
                        "position":"SMALL_FORWARD",
                        "skillLevel":10.0,
                        "wins":0},
                "awWo0cH6jw4Ib8fD9Fgi":
                    {
                        "id":"awWo0cH6jw4Ib8fD9Fgi",
                        "losses":0,
                        "name":Baka Sussy",
                        "position":"CENTER",
                        "skillLevel":10.0,"wins":0
                    }
            },
        "queries":[]
    }
```
</details>

#### `profile-edit <action> <id> (name) (position)`

<details>
    <summary>Successful requests</summary>
    <br>
1. profile-edit?action=delete&id=a0Xd2wuFh9oGb3aJuv4K

```
    {
        "result":"success",
        "queries":
            ["action","id"]
    }
```

2. profile-edit?action=eDiT&id=awWo0cH6jw4Ib8fD9Fgi&name=Sussy Baka&position=SHOOTING_GUARD

```
    {
        "result":"success",
        "queries":
            ["action","id","name","position"]
    }
```

3. profile-edit?action=EDIT&id=awWo0cH6jw4Ib8fD9Fgi&position=SHOOTING_GUARD

```
    {
        "result":"success",
        "queries":["action","id","position"]
    }
```

4. profile-edit?action=edit&id=a0Xd2wuFh9oGb3aJuv4K&name=Bosh Boshington

```
    {
        "result":"success",
        "queries":["action","id",'name"]
    }
```

5. profile-edit?action=edit&id=definitelynotaplayerid&name=Bames Jond&position=SHOOTING_GUARD

```
failure condition. the result is `error_bad_request` and the corresponding exception sent back is a `NoItemFoundException`
```

6. profile-edit?id=definitelynotaplayerid&name=Jond, Bames Jond&position=SHOOTING_GUARD

```
    {
        "result":"error_bad_request",
        "details":"action keyword must contain the word 'edit' or 'delete'. Any other word will result in an error",
        "queries":["id","name","position"]
    }
```
</details>

#### `match-add <id>`

<details>
    <summary>Successful requests</summary>
    <br>
1. match-add?id=a0Xd2wuFh9oGb3aJuv4K

```
    {
        "result":"success",
        "matchAdded":
            {match1},
        "queries":["id"]
    }
```
</details>

#### `match-view`

<details>
    <summary>Successful requests</summary>
    <br>
1. match-view (Shown here with 3 matches but this number is configurable)

```
    {
        "result":"success",
        "matches":
            [
                {match1},
                {match2},
                {match3}
            ],
        "queries":[]
    }
```
</details>

#### `queue-view`

<details>
    <summary>Successful requests</summary>
    <br>
1. queue-view

```
    {
        "result":"success",
        "PlayerQueue":
            [
                {
                    "id":"a0Xd2wuFh9oGb3aJuv4K",
                    "losses":12,
                    "name":"Josh Joshington",
                    "position":"POWER_FORWARD",
                    "skillLevel":10.0,
                    "wins":20
                }
            ],
        "queries":[]
    }
```
    
2. queue-view?id=a0Xd2wuFh9oGb3aJuv4K
```
    {
        "result":"success",
        "playerPosition":1,
        "queries":["id"]
    }
```
</details>

#### `queue-add <id>`

<details>
    <summary>Successful requests</summary>
    <br>
1. queue-add?id=1234567890

```
    {
        "result":"success",
        "Message":"Player added to queue",
        "newCourtMade":false,
        "addedID":"1234567890"
    }
```

2. queue-add?id=1234567890 (second time)

```
    {
        "result":"error_bad_request",
        "details":"Player has already been added to queue.",
        "newCourtMade":false,
        "queries":["id"]
    }
```

3. queue-add?id=incorrectID

```
    {
        "result":"error_bad_request",
        "details":"Player Not Found: No Player found with corresponding ID",
        "newCourtMade":false,
        "queries":["id"]
    }
```

5. queue-add?id=1

```
    {
        "result":"success",
        "Message":"Player added to queue",
        "newCourtMade":true,
        "addedID":"1",
        "court":
            {
                "match":{match1},
                "players":
                    [
                        {player1},
                        {player2},
                        {player3},
                        {player4},
                        {player5},
                        {player6},
                        {player7},
                        {player8},
                        {player9},
                        {player10}
                    ]
            }
    }
```
</details>

   5. Additionally, there exist the following errors:
       1. `matchmaking_error` --> There has been an internal matchmaker error. If the matchmaker is properly tested, this will not come up.
       2. `matchmaking_full` --> Every alotted court has been filled up, the player has successfully been added to the queue, but they need to wait until other people leave to free them a space.

## Backend Player, Team, and Matches
Each user in omatch will be assigned a Player class. This class will contain their specific user ID, skill level, W/L record, position, and other necessary information. 

Players are assigned to teams in our matchmaking algorithms (described below). The teams class contains information about the average skill of the team itself.

Two teams are assigned to a Match class. The match class keeps track of ongoing games and the outcome of said games. Based on the outcome, the skills will be adjusted accordingly.

## Matchmaking

The IMatchMaker interface implements the matchmaking method, which will take in a list of players, divide them into teams and return matches based on those teams. Based on the input, the matchmaking algorithm can divide players into any EVEN number of teams. More matchmaking algorithms can  be implemented with this interface in the future. 
1. SimpleMatchMaker - This algorithm does not take into account skill. Rather, it just splits players up 1 by 1 onto separate teams. It then combines teams two by two into matches.
2. SortSkillMatchmaker - This algorithm sorts each player based on skill level. Then, it splits players 1 by 1 onto separate teams. Once this is complete, the teams are sorted again based on average skill to ensure more parity. The teams most similar in skill are then paired together, from highest to lowest rank.
3. PositionMatchMaker - This algorithm sorts players on skill level, but prioritizes making sure each team has players from each position. First the players are split up into their respective positions, and then split up evenly amongst all teams to minimize the overlap of positions and ensure teams are as balanced as possibles. The remaining players are split up based on skill like in SortSkillMatchmaker.

## Skill Level

Skill-Level(SL) is set at 1500 when a player profile is created, and modulated based on their performance in future games. The SkillUpdater interface implements the skillupdater function, which takes in a completed match and updates the skill levels of the players in the match depending if they win or loss. Like with matchmaking, we implemented three algorithms of increasing complexity.
1. SimpleSkill - This algorithm substracts 1 point from all players that loss the game and adds 1 point to all players that win the game.
2. WLSKill - This algorithm sets the skill level of each player to their W/L ratio.
3. EloSkill - This algorithm implements a generalized version of the EloAlgorithm over a teams approach. The expected outcome is determined beforehand, and a team's skilllevel is adjusted depending on how likely or unlikely they were to win a game. For example if a weak-ranked team beats a much higher ranked team, they would see a dramatic increase in their collective skill levels. However, if a highly ranked team beats a team that is much lower ranked then them, there skill levels will only increase by a marginal amount. More information about the ELO algorithm can be found at this link: https://en.wikipedia.org/wiki/Elo_rating_system
