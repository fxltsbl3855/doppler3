/**
 * @Tiltle: 问题汇总列表页面 Description: 问题汇总列表页面 Author:Bill.Fu
 * 
 * 修改日期 修改人 修改内容 说明 2017-08-07 Bill.Fu Create 新增
 */
var ProblemList = function() {
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
			var url = $.modulePath + "/exception/getProblemList.shtml";
			$.ajax({
				url : url,
				data : ProblemList.queryParams(),
				dataType : 'json',
				success : function(data) {
					$("#exTable").bootstrapTable('load', data);
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
				'exName' : $("#exName").val(),
				'appName' : $("#appName").val()
			};
			return params;
		},

		/**
		 * 生异常数量超链接
		 */
		nameFormatter : function(value, row, index) {
			var appName = row.appName;
			var exName = row.exName;
			return '<a href="javascript:ProblemList.toServices(\'' + appName + '\',\'' + exName + '\',\'' + $("#startDate").val() + '\',\''
					+ $("#endDate").val() + '\',\'' + row.className + '\',\'' + row.methodName + '\',\'' + row.lineNum + '\')">' + value
					+ '</a>';
		},

		/**
		 * 生异常数量超链接
		 */
		titleFormatter : function(value, row, index) {
			return '<span title=' + row.className + ' style="font-weight:normal;">' + row.classNameSim + '</span>';
		},

		/**
		 * div设置及初始化異常明細頁面
		 */
		toServices : function(appName, exName, startDate, endDate, className, methodName, lineNum) {
			$('#mainDiv')[0].style.display = "none";
			$('#detailProblemDiv')[0].style.display = "block";
			var url = $.modulePath + "/exception/getProblemDetailList.shtml";
			$("#detail_exName").html(exName);
			$("#detail_appName").html(appName);
			$("#detail_startDate").html(startDate);
			$("#detail_endDate").html(endDate);
			$.ajax({
				async : false,
				url : url,
				data : {
					'appName' : appName,
					'exName' : exName,
					'startDate' : startDate,
					'endDate' : endDate,
					'className' : className,
					'methodName' : methodName,
					'lineNum' : lineNum
				},
				dataType : 'json',
				success : function(data) {
					$("#detailProblemTable").bootstrapTable('load', data);
				}
			});
		},

		/**
		 * 返回
		 */
		backHandler : function() {
			$('#mainDiv')[0].style.display = "block";
			$('#detailProblemDiv')[0].style.display = "none";
		},

		/**
		 * 生异常明细超链接
		 */
		detailFormatter : function(value, row, index) {
			var problemId = row.id;
			var host = row.host;
			return '<a href="javascript:ProblemList.showProblemModalHandler(\'' + problemId + '\',\'' + host + '\',0)">明细</a>';
		},

		/**
		 * 格式化时间
		 */
		formatterTime : function(value, row, index) {
			if (row.timestamp != null) {
				var str = new Date(row.timestamp).Format("yyyy-MM-dd hh:mm:ss");
				return str;
			}
			return null;
		},

		/**
		 * 异常明细查询
		 */
		showProblemModalHandler : function(id, host, forward, oData, indexValue) {
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
						tr.align = "left"; // 对齐方式
						tr.innerHTML = escapeChars(newData[i].logInfo);// 内容

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
		previousProblemHandler : function() {
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
			ProblemList.showProblemModalHandler(id, hostTemp, -1, oData, preIndex);

		},

		/**
		 * 查询后15条记录
		 */
		nextProblemHandler : function() {
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
			ProblemList.showProblemModalHandler(id, hostTemp, 1, oData, nextIndex);
		}
	};
}();

$(document).ready(function() {
	$('[data-toggle="sino-grid"]').sinoGrid();

	// 导出控件挪位，仍调用原先的导出控件，页面显示的控件并不真正执行操作。
	$("#importDiv").html($(".fixed-table-toolbar")[0].innerHTML);
	$($(".fixed-table-toolbar")[0]).attr("id", "exportToolDiv");
	$($(".fixed-table-toolbar")[0]).hide();
	$("#importDiv button").prepend("<span style='width:80px;'>导出</span>");

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

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function escapeChars(str) {
	str = str.replace(/\r?\n/g, "<br>");
	str = str.replace(/\r?\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	return str;
}