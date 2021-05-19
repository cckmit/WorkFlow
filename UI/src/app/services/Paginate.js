app.service('Paginate', function($http, $q, appSettings, Flash, $location, $rootScope) {

    var Paginate = {};
    var TableConfig = function() {
        return {
            currentPage: $rootScope.paginateValue + 1,
            pageSize: $rootScope.paginateDefaultValue,
            totalItems: 0,
            lastLoadedPage: null
        }
    }

    Paginate.tableConfig = TableConfig


    var TableAttributes = function(pageSize) {
        return {
            "offset": $rootScope.paginateValue,
            "limit": pageSize,
            "filter": ""
        }
    }

    Paginate.initTableAttr = TableAttributes

    var PageSizeList = function() {
        return [50, 100, 150];
    }

    Paginate.pageSizeList = PageSizeList
	
	var PageSizeList1 = function() {
        return [10, 20, 30];
    }

    Paginate.pageSizeList1 = PageSizeList1
	
	
	var repoNamePageSizeList = function() {
        return [100, 200, 500];
    }

    Paginate.repoNamePageSizeList = repoNamePageSizeList

	
	var defaultPageValue = function(pageSize) {
      return {
        "defaultValue": 50,
		"paginateValue": 0,
      }
	}

	Paginate.defaultPageValue = defaultPageValue

	// var switchPageSize = function($scope, tableAttr){
	// 	tableAttr.offset = 0
	// 	tableAttr.limit = $scope.tableConfig.pageSize;
	// 	$scope.tableConfig.currentPage = 1
	// 	localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
	// 	tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
	// 	$rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))

	// 	return tableAttr
	// };

	// Paginate.switchPageSize = switchPageSize
	

    var ResetSortColumn = function(columnsToBeSorted) {
        var lSort = {},
            lSortType = ["asc", "desc"];
        for (colIndex in columnsToBeSorted) {
            lSort[columnsToBeSorted[colIndex]] = {}
            for (lST in lSortType) {
                lSort[columnsToBeSorted[colIndex]][lSortType[lST]] = false
            }
        }
        return lSort;
    }

    Paginate.resetSortColumn = ResetSortColumn


    var SortSpecificColumn = function(columnName, tableAttr, pTableConfig, sortColumn, columnsToBeSorted) {
        tableAttr.offset = $rootScope.paginateValue
        pTableConfig.currentPage = $rootScope.paginateValue + 1
        if (!sortColumn[columnName]["asc"] && !sortColumn[columnName]["desc"]) {
            sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
            sortColumn[columnName]["asc"] = true;
            tableAttr["orderBy"] = {}
            tableAttr["orderBy"][columnName] = "asc"
        } else if (sortColumn[columnName]["asc"]) {
            sortColumn[columnName]["asc"] = false;
            sortColumn[columnName]["desc"] = true;
            // delete tableAttr["orderBy"]["modifiedDt"]
            tableAttr["orderBy"][columnName] = "desc"
        } else if (sortColumn[columnName]["desc"]) {
            sortColumn[columnName]["desc"] = false;
            sortColumn[columnName]["asc"] = false;
            delete tableAttr["orderBy"][columnName]
                // tableAttr["orderBy"]["modifiedDt"] = "desc"
        }
        return {
            "tableAttr": tableAttr,
            "tableConfig": pTableConfig,
            "sortColumn": sortColumn
        };
    }

    Paginate.sort = SortSpecificColumn

	var RefreshScrolling = function() {
		$(".scrollingFunction").scroll(function() { 
			refreshPage();
			if ( window.location.href.indexOf('page_y') != -1 ) {
				var match = window.location.href.split('?')[1].split("&")[0].split("=");
				var scrolling_position = match[1];
				localStorage.setItem("scrolling_position", scrolling_position);
			}
		})
	}

	Paginate.refreshScrolling = RefreshScrolling

	function refreshPage () {
		var page_y = $(".scrollingFunction").scrollTop();
		window.location.href = window.location.href.split('?')[0] + '?page_y=' + page_y;
	}

	$rootScope.saveformData = function() {
		var elmnt = document.getElementById("scroll_function");
		if(JSON.parse(localStorage.getItem('scrolling_position'))) {
			elmnt.scrollTop = JSON.parse(localStorage.getItem('scrolling_position'))
		}
	}

    return Paginate;

});