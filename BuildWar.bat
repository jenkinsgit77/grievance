@ECHO OFF
@ECHO
@ECHO **** BUILDING ANT BUILD ****
@ECHO ********CUSTOMER Build***********
set srcpath=%cd%\CustPortalWebcenter\deploy
echo  %srcpath%
@ECHO ******** in progress***********
set filejws=%cd%\CustomerPortalApp.jws
echo  %filejws%
set oraclepath=%ORACLE_HOME%
echo  %oraclepath%
set jwpath=%cd%
@ECHO ******** in progress2***********
cd  CustPortalWebcenter
echo %cd%
echo %ANT_HOME%
call %ANT_HOME%/bin/ant clean
call %ANT_HOME%/bin/ant -f build.xml  -Darg0=%srcpath%  -Darg1=%filejws% -DJENKIN_WORKSPACE=%jwpath% -DORACLE_HOME=%oraclepath%
@ECHO ********Build completed***********
@ECHO ********Check CUSTOMER war size(40M), if any error, size reduces.***********
@ECHO Enter to exit
pause
exit