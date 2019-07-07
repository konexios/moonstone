del lib\pegasus-dashboard-web-api-*
cd ..\pegasus\pegasus-dashboard-web-api
cd src\main\resources
call gulp build
cd ..\..\..\
call gradle webjar
copy build\libs\pegasus-dashboard-web-api-* ..\..\apollo\lib
cd ..\..\apollo