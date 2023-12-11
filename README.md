# OMatch
## Fall 2023 Software Engineering Final Project
*Jonathan Zhou, Michael Tu, Andrew Boyaciliger, Jake Stifelman*
<hr>

## Backend Handlers

1. `profile-add <name> <position>`
    1. The profile addition handler manages the addition of profiles to the backend database. Any players with exactly the same name and position will be treated as different players with a different ID, so it is imperative that you use the editing handler for editing or deleting players.
    2. The `name` query has to do with the player's name, taken in as a UTF-8 String.
    3. The `position` query has to do with the player's position, and it can be one of five possibile strings. Any other string put here will result in an error:
          1. `POINT_GUARD`
          2. `SHOOTING_GUARD`
          3. `SMALL_FORWARD`
          4. `POWER_FORWARD`
          5. `CENTER`
    5. Example Queries:
          1. `profile-add?name=Josh Joshington&position=SMALL_FORWARD` --> {"result":"success","playerID":"a0Xd2wuFh9oGb3aJuv4K","queries":["name","position"]}
          2. `profile-add?name=Baka Sussy&position=CENTER` --> {"result":"success","playerID":"awWo0cH6jw4Ib8fD9Fgi","queries":["name","position"]}
2. `profile-view <id>`
   1. The profile view handler retrieves all relevant data to the player, including, name, position, ELO ranking, and W/L ratio.
   2. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
   3. Example Queries:
        1. `profile-view?id=a0Xd2wuFh9oGb3aJuv4K` --> {"result":"success","player":{"id":"a0Xd2wuFh9oGb3aJuv4K","name":"Josh Joshington","skillLevel":9001.112,"wins":12,"losses":0,"Position":"SMALL_FORWARD"},"queries":["id"]}
        2. `profile-view?id=awWo0cH6jw4Ib8fD9Fgi` --> {"result":"success","player":{"id":"awWo0cH6jw4Ib8fD9Fgi","name":"Baka Sussy","skillLevel":235,"wins":6,"losses":29,"Position":"CENTER"},"queries":["id"]}
3. `profile-edit <action> <id> (name) (position)`
   1. The profile edit handler manages the editing and/or deleting of players.
   2. The `action` query has to do with whether the player in question is being edited or deleted. As of such, it can be one of two strings, "edit" or "delete". These strings are not case-sensitive
   3. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
   4. The `name` query is the player's new name, only recognized when the action is set to edit. This keyword is optional and the player's name will not be updated if it is not present.
   5. The `position` query is the player's updated position. These keywords are the same as for the `profile-add` handler. As with the name, this keyword is optional and will not be updated if it is not present.
   6. Example Queries:
      1. `profile-edit?action=delete&id=a0Xd2wuFh9oGb3aJuv4K` --> {"result":"success","queries":["action","id"]}
      2. `profile-edit?action=eDiT&id=awWo0cH6jw4Ib8fD9Fgi&name=Sussy Baka&position=SHOOTING_GUARD` --> {"result":"success","queries":["action","id","name","position"]}
      3. `profile-edit?action=EDIT&id=awWo0cH6jw4Ib8fD9Fgi&position=SHOOTING_GUARD` --> {"result":"success","queries":["action","id","position"]}
      4. `profile-edit?action=edit&id=a0Xd2wuFh9oGb3aJuv4K&name=Bosh Boshington` --> {"result":"success","queries":["action","id",'name"]}
4. `match-add <id>`
   1. The match addition handler manages player's being assigned to matches. It returns the match that the player was added to, which in turn contains all the team details.
   2. The `id` keyword is a randomized, unique ID for each player. 20 characters long.
   3. Example Queries:
      1. `match-add?id=a0Xd2wuFh9oGb3aJuv4K` --> {"result":"success","matchAdded":{Note: this is a very large object and I am not entirely sure yet what of it I should realistically send to the frontend, as of such I am holding off on setting in stone what is returned here},"queries":["id"]}
5. `match-view`
   1. Returns a list of match objects, each containing details about the court the match is taking place on, as well as teams and their respective players.
   2. Example Queries:
      1. `match-view` --> {"result":"success","matches":[{filler},{filler},{filler},{filler},{filler},{filler}],"queries":[]}
