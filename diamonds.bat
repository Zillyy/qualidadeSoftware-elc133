set LD_LIBRARY_PATH=lib/win32/
set CLASSPATH=.;bin/;lib/jar/lwjgl_devil.jar;lib/jar/lwjgl.jar;lib/jar/lwjgl_util.jar;lib/jar/jogg-0.0.7.jar;lib/jar/jorbis-0.0.15.jar;lib/jar/trb.jar;
java -Djava.library.path=%LD_LIBRARY_PATH% -cp %CLASSPATH% it.diamonds.Game
pause
