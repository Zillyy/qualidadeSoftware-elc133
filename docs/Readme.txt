============================================================================
         Diamond Crush - The "I-hate-setup-next-next-next" Release
                  First Playable Version aka YAGNI (v0.1)

 Released by the Diamonds Team (temporary name) under Public Domain License
============================================================================



Legal Disclaimer:
-----------------
USE THIS PROGRAM AT YOUR OWN RISK, WE CANNOT BE HELD RESPONSIBLE FOR ANY LOSS
OR DAMAGE CAUSED BY ANY FILE CONTAINED IN THE GAME ARCHIVE.

Diamond Crush is the outcome of the free and non-profit Diamonds Project.
It is released for free under Public Domain license. If somebody made you pay money
for this package, you might have been ripped off.

You can receive further informations and the source code by contacting us at:
support@diamondcrush.net



What can you do to help us:
---------------------------
Play, play and play. And, please, report us any bug, error or incoherence you might
find by mailing us your game logs (the "playerOne.txt" and/or "playerTwo.txt" files
found in the "data" folder) at support@diamondcrush.net. If the game crashes while you
are playing, or it simply refuses to start, remember to send us the bug-report.txt
file found in the game directory.



System Requirements:
--------------------
- CPU: Intel Pentium II 400MHz or equivalent
- RAM: 128 MB
- 8 MB video card (OpenGL support IS necessary)
- Any operating system with Java 5.0 Runtime Environment installed



Game Details:
-------------
Diamond Crush is a classic puzzle game, with both single player and multiplayer game
modes. This First Playable Version is multiplayer-based, and offers only offline single
matches for two players at a time.
Both players will control falling pairs of differently colored and shaped gems, and
their purpose will be strategically placing them inside their own grid-based play fields,
by matching their colors. Building up rectangles composed by many gems of the same color
will make them turn into agglomerates, bigger gems whose score values change according
to their size.
Some colored treasure boxes, falling together with the gems, will allow the players to
delete all the adjacent gems of the same color, preventing their play fields to fill up
completely (this event would lead to a Game Over). Any other gem placed above the cells
whose content has got deleted will fall down, filling up them. This event can trigger
chain reactions (Crushes) if any gem falls over a chest of the same color, or vice versa.
Deletions and Crushes, according to the number of deleted gems, will make a different
number of stones, whose fall scheme follows some strict patterns and who turn into gems
after some turns, fall into the opponent's play field, filling up his grid and making
his strategies harder to accomplish. This feature encourages both players to try to
build up longer Crushes and bigger agglomerates: a strategic approach towards this
objective is the key to win a game.



Default Keys:
-------------
Player 1:
W - Not used yet
A - Move the pair left
S - Increase the falling speed
D - Move the pair right
R - 90° anti-clockwise rotation
T - 90° clockwise rotation

Player 2:
Up arrow - Not used yet
Left arrow - Move the pair left
Down arrow - Increase the falling speed
Right arrow - Move the pair right
Ins - 90° anti-clockwise rotation
Home - 90° clockwise rotation



Known Problems/Bugs:
--------------------
- On some GNU/Linux systems the game refuses to start because our libraries rely to an
  old version of libtiff. If you have a newer version of this library installed, you can
  start the game by creating a symbolic link like in this example:
  # ln -s /usr/lib/libtiff.so.4 /usr/lib/libtiff.so.3
- Under GNU/Linux systems with amd 64 and xfree as x server, the game may not run.
- Iconising the game window might cause slight visualization flaws in certain graphics elements.
- On some Windows configurations (Win2000/Win98), the DiamondCrush.exe file might not work. 
  If this happens onto your PC, use run-diamonds.bat to launch the game anyway.
- Making too many Stones fall could not trigger a Game Over, even if the opponent's play
  field is totally full.
- To solve audio related problems on linux just follow this easy steps:
    1. Copy the files openalrc.example and asoundrc.example from the docs
       directory to you home directory.
    2. rename "openalrc.example" to ".openalrc"
    3. rename "asoundrc.example" to ".asoundrc"


FAQ:
----
N/A



Contact:
--------
Support requests: support@diamondcrush.net
Site URL: http://www.diamondcrush.net



Special Thanks to:
------------------
- Hardware Upgrade (http://www.hwupgrade.it) for the developers forum and support
- The Night Sun Network (http://www.nsn3.net) for hosting our web site and repository
- everybody who helped us in any way!
