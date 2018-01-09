/**
 * @Tiltle: 异常列表页面 Description: 异常列表页面 Author:Krik.Yang
 * 
 * 修改日期 修改人 修改内容 说明 2015-09-07 Krik.Yang Create 新增
 */
var ErrorList = function() {
	// 前后翻页变量
	var vForward = 0;
	// 前翻索引
	var preIndex = 0;
	// 后翻索引
	var nextIndex = 0;
	// host全局变量，用于日志界面上下翻页时查询使用
	var hostTemp = "";
	return {

		/**
		 * 主机监控列表查询
		 */
		query : function() {
			var url = $.modulePath + "/exception/getErrorList.shtml";
			$.ajax({
				url : url,
				data : ErrorList.queryParams(),
				dataType : 'json',
				success : function(data) {
					$("#errorTable").bootstrapTable('load', data);
				}
			});
		},

		/**
		 * 查询条件参数
		 */
		queryParams : function() {
			var params = {
				'startDate' : $("#startDate").val(),
				'endDate' : $("#endDate").val(),
				'errorName' : $("#errorName").val()
			};
			return params;
		},

		/**
		 * 生异常数量超链接
		 */
		nameFormatter : function(value, row, index) {
			var appName = row.appName;
			var errorName = row.errorName;
			return '<a href="javascript:ErrorList.toServices(\'' + appName + '\',\'' + errorName + '\',\'' + $("#startDate").val()
					+ '\',\'' + $("#endDate").val() + '\')">' + value + '</a>';
		},

		/**
		 * div设置及初始化異常明細頁面
		 */
		toServices : function(appName, errorName, startDate, endDate) {
			$('#mainDiv')[0].style.display = "none";
			$('#detailErrorDiv')[0].style.display = "block";
			var url = $.modulePath + "/exception/getErrorHostList.shtml";
			$("#detail_errorName").html(errorName);
			$("#detail_appName").html(appName);
			$("#detail_startDate").html(startDate);
			$("#detail_endDate").html(endDate);
			$.ajax({
				async : false,
				url : url,
				data : {
					'appName' : appName,
					'errorName' : errorName,
					'startDate' : startDate,
					'endDate' : endDate
				},
				dataType : 'json',
				success : function(data) {
					$("#detailErrorTable").bootstrapTable('load', data);
				}
			});
		},

		/**
		 * 返回
		 */
		backHandler : function() {
			$('#mainDiv')[0].style.display = "block";
			$('#detailErrorDiv')[0].style.display = "none";
		},

		/**
		 * 生异常明细超链接
		 */
		detailFormatter : function(value, row, index) {
			var errorId = row.errorId;
			var host = row.host;
			return '<a href="javascript:ErrorList.showErrorModalHandler(\'' + errorId + '\',\'' + host + '\',0)">明细</a>';
		},

		/**
		 * 异常明细查询
		 */
		showErrorModalHandler : function(id, host, forward, oData, indexValue) {
			hostTemp = "";
			vForward = forward;
			if (forward == 0) {
				$('#myModal').modal('show');
				preIndex = 0;
				nextIndex = 0;
			}
			if (indexValue == null) {
				indexValue = 0;
			}
			var url = $.modulePath + "/exception/queryErrorById.shtml";
			$.ajax({
				url : url,
				data : {
					'id' : id,
					'host' : host,
					'forward' : forward,
					'index' : indexValue
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					var newData = null;
					if (vForward == -1) {
						newData = data.concat(oData);
					} else if (vForward == 1) {
						newData = oData.concat(data);
					} else {
						newData = data;
					}
					$("#detailTable tbody").html("");
					for ( var i = 0; i < newData.length; i++) {
						var tr = document.getElementById("detailTable").insertRow();
						tr.style.backgroundColor = newData[i].color;// 颜色
						tr.id = newData[i].id;
						; // 单元格的id
						tr.align = "left"; // 对齐方式
						tr.innerHTML = escapeChars(newData[i].logInfo); // 内容
					}
					// 滚动条跟随页面移动
					var div = document.getElementById('myDiv');
					if (vForward == -1) {
						div.scrollTop = 0;
					} else if (vForward == 1) {
						div.scrollTop = div.scrollHeight;
					}
					hostTemp = host;
				}
			});
		},

		/**
		 * 查询前15条记录
		 */
		previousErrorHandler : function() {
			preIndex++;
			var oData = [];
			var tr = $("#detailTable tbody tr");
			if (tr.length == 0) {
				return false;
			}
			var id = tr[0].id;
			for ( var i = 0; i < tr.length; i++) {
				var object = {
					'id' : tr[i].id,
					'logInfo' : tr[i].innerHTML,
					'color' : tr[i].style['background-color']
				};

				oData.push(object);
			}
			if (id == null) {
				$('#myErrorModal').modal('show');
				return false;
			}
			ErrorList.showErrorModalHandler(id, hostTemp, -1, oData, preIndex);

		},

		/**
		 * 查询后15条记录
		 */
		nextErrorHandler : function() {
			nextIndex++;
			var oData = [];
			var tr = $("#detailTable tbody tr");
			if (tr.length == 0) {
				return false;
			}
			var id = tr[tr.length - 1].id;
			// 将原数据缓存到oData
			for ( var i = 0; i < tr.length; i++) {
				var object = {
					'id' : tr[i].id,
					'logInfo' : tr[i].innerHTML,
					'color' : tr[i].style['background-color']
				};
				oData.push(object);
			}

			if (id == null) {
				$('#myErrorModal').modal('show');
				return false;
			}
			ErrorList.showErrorModalHandler(id, hostTemp, 1, oData, nextIndex);
		}
	};
}();

$(document).ready(function() {
	$('[data-toggle="sino-grid"]').sinoGrid();

	// 导出控件挪位，仍调用原先的导出控件，页面显示的控件并不真正执行操作。
	$("#importDiv").html($(".fixed-table-toolbar")[0].innerHTML);
	$($(".fixed-table-toolbar")[0]).attr("id", "exportToolDiv");
	$($(".fixed-table-toolbar")[0]).hide();
	$("#importDiv button").prepend("<span style='margin-right:5px;'>导出</span>");

	var exportHrefShowArray = $("#importDiv a");
	var exportHrefHideArray = $("#exportToolDiv a");
	for ( var index = 0; index < exportHrefShowArray.length; index++) {
		$(exportHrefShowArray[index]).on("click", {
			tempIndex : index
		}, function(event) {
			exportHrefHideArray[event.data.tempIndex].click();
		});
	}
});

function escapeChars(str) {
	str = str.replace(/\r?\n/g, "<br>");
	str = str.replace(/\r?\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	return str;
}