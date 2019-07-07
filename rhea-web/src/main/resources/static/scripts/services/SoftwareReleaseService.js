services.factory('SoftwareReleaseService',
    [ '$http',
    function($http) {
        function find(page, filter) {
            return $http.post('/api/rhea/software-releases/find', {
                // pagination
                pageIndex: page.pageIndex,
                itemsPerPage: page.itemsPerPage,
                // sorting
                sortDirection: page.sort.direction,
                sortField: page.sort.property,
                // filter
                enabled: filter.enabled,
                softwareProductIds: filter.softwareProductIds,
                deviceTypeIds: filter.deviceTypeIds,
                upgradeableFromIds: filter.upgradeableFromIds
            });
        }

        function save(softwareRelease) {
            if (softwareRelease.id) {
                return $http.put('/api/rhea/software-releases/'+softwareRelease.id, softwareRelease);
            } else {
                return $http.post('/api/rhea/software-releases', softwareRelease);
            }
        }

        function saveWithFile(model) {
            var fd = new FormData();
            fd.append('file', model.file);
            fd.append("model", JSON.stringify(model.data));

            var postOptions = {
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            };

            if (model.data.id) {
                return $http.post('/api/rhea/software-releases/'+model.data.id, fd, postOptions);
            } else {
                return $http.post('/api/rhea/software-releases/', fd, postOptions);
            }
        }

        function filterOptions() {
            return $http.get('/api/rhea/software-releases/filter-options');
        }

        function options(selection) {
            return $http.post('/api/rhea/software-releases/options', selection);
        }

        return {
            find: find,
            save: save,
            saveWithFile: saveWithFile,
            filterOptions: filterOptions,
            options: options
        };
    }
]);
