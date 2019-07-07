del lib\pegasus-dashboard-api-*
del lib\pegasus-dashboard-core-*
del lib\pegasus-dashboard-db-*
del lib\pegasus-dashboard-web-api-*
cd ..\pegasus\pegasus-dashboard-api
call gradle jar
REM copy build\libs\pegasus-dashboard-api-* ..\..\apollo\lib
cd ..\pegasus-dashboard-core
call gradle jar
REM copy build\libs\pegasus-dashboard-core-* ..\..\apollo\lib
cd ..\pegasus-dashboard-db
call gradle jar
REM copy build\libs\pegasus-dashboard-db-* ..\..\apollo\lib
cd ..\pegasus-dashboard-web-api
cd src\main\resources
call gulp build
cd ..\..\..\
call gradle webjar
REM copy build\libs\pegasus-dashboard-web-api-* ..\..\apollo\lib
cd ..\..\apollo

@pause
