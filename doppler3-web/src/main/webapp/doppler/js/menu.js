/** 菜单 */
var Menu = function() {
	return {

		forward : function(url) {
			var path = window.location.origin + url;
			$.post(path, function(data) {
				$("#page-content").empty();
				$("#page-content").append(data);
			});
			Menu.menuBlurHandler();
		},

		menuBlurHandler : function() {
			$("#index").blur();

			$("#errorList").blur();
			$("#exception").blur();
			$("#problemList").blur();

			$("#timerTask").blur();
			$("#queryLogger").blur();

			$("#requestCount").blur();
			$("#app").blur();
			$("#ipMonitor").blur();

			$("#recordCount").blur();
			$("#queryWebOper").blur();
			$("#logShow").blur();
		},

		showChildMenu : function(id, menuId) {
			var menuDisplay = $('#' + id);
			if (menuDisplay.css('display') === 'none') {
				menuDisplay.css('display', 'block');
			} else {
				menuDisplay.css('display', 'none');
			}

			$('#' + menuId).blur();
		},

		menuClickHandler : function(url) {
			if ($("#requestCountLi").css('display') !== 'none') {
				url += "?status=open";
			} else if ($("#exceptionLi").css('display') !== 'none') {
				url += "?status=open";
			} else if ($("#recordLi").css('display') !== 'none') {
				url += "?status=open";
			}
			window.location.href = url;
		},

		menuInit : function() {
			var type = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);
			if (type.match(/&|=/g) != null) {
				return;
			}

			var hrefResult = location.href;
			var secondLevel = location.search.replace(/[?]status=/g, '');
			if ((hrefResult.indexOf("ipMonitor.shtml") != -1 || hrefResult.indexOf("app.shtml") != -1) && secondLevel === 'open') {
				Menu.showChildMenu('requestCountLi', 'requestCount');
			}
			if ((hrefResult.indexOf("errorList.shtml") != -1 || hrefResult.indexOf("problemList.shtml") != -1) && secondLevel === 'open') {
				Menu.showChildMenu('exceptionLi', 'exception');
			}
			if ((hrefResult.indexOf("webOperation.shtml") != -1 || hrefResult.indexOf("logShow.shtml") != -1) && secondLevel === 'open') {
				Menu.showChildMenu('recordLi', 'record');
			}

			var pageName = type.split(".")[0];
			$("#" + pageName + "Page").css("background-color", "#F5F5F5");
			$("#" + pageName + "Page").css("color", "black");
		}
	};
}();

$(document).ready(function() {
	Menu.menuInit();
});