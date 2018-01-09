/**
 * @Tiltle: 异常监控页面 Description: 异常监控页面
 * 
 * Author:Krik.Yang
 * 
 * 修改日期 修改人 修改内容 说明 2016-08-10 Krik.Yang Create 新增 2016-08-25 Krik.Yang Modify 修改: 增加注释 2016-09-09 Krik.Yang Modify 修改: 列表页面日志内容过长增加自动换行
 */

var ExceptionMonitor = function() {

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
		 * 初始化Table
		 */
		initTable : function() {
			$("#exceptionTable>tbody>tr").mouseout(function(e) {
				$(this).removeClass("active");
			});
			$("#exceptionTable>tbody>tr").mouseover(function(e) {
				$(this).addClass("active");
			});
		},

		/**
		 * 获取模块名下拉框数据
		 */
		initSelect : function() {
			$('#appName').on('change', function() {
				$.post($.modulePath + "/components/options/getServiceList.shtml", {
					'appName' : $('#appName').val()
				}, function(data) {
					$('#moduleName').empty();
					var html = '<option value="-1">请选择</option>';
					for ( var i in data) {
						var serviceId = data[i].serviceId;
						var name = data[i].name;
						html += '<option value="' + serviceId + '">' + name + '</option>';
					}
					$('#moduleName').html(html);
				});
			});

		},

		/**
		 * 查询条件参数
		 */
		queryParams : function() {
			var params = {
				'startDate' : $("#startDate").val() + ':00',
				'endDate' : $("#endDate").val() + ':00',
				'appName' : $("#appName").val(),
				'errorLevel' : $("#errorLevel").val(),
				'addr' : $("#addr").val(),
				'keys' : $("#keys").val()
			};
			return params;
		},

		/**
		 * 列表查询
		 */
		queryHandler : function() {
			var url = $.modulePath + "/exception/queryErrorByKey.shtml";
			$.ajax({
				url : url,
				data : ExceptionMonitor.queryParams(),
				dataType : 'json',
				success : function(data) {
					$("#exceptionTable").bootstrapTable('load', data);
					ExceptionMonitor.initTable();
				}
			});
		},

		/**
		 * 生成单选框
		 */
		radioFormatter : function(value, row, index) {
			$("#detailTable>thead>tr>th")[0].style.width = '12px';
			return '<input type="radio" id="id" value="' + value + '"/>';
		},

		/**
		 * 生成明细链接
		 */
		detailNameFormatter : function(value, row, index) {
			var id = row.id;
			var title = row.title;
			var host = row.host;
			var logInfo = row.logInfo;
			return '<table style="TABLE-LAYOUT: fixed" cellspacing="0" cellpadding="0" width="100%"><tr onmouseover="ExceptionMonitor.showLogInfoDetail(\''
					+ id
					+ '\',\''
					+ host
					+ '\',event)"><td style="word-wrap:break-word;" width="100%" title="'
					+ title
					+ '">'
					+ escapeChars(logInfo) + '</td></tr></table>';
		},

		showLogInfoDetail : function(id, host, e) {
			$("#floatDiv").show().css({
				"top" : e.pageY,
				"left" : e.pageX
			});

			var href = '<a href="javascript:ExceptionMonitor.showErrorModalHandler(\'' + id + '\',\'' + host + '\',0)">显示前后15条记录</a>';
			$("#floatDiv").empty();

			$("#floatDiv").html(href);
		},

		hideFloatDiv : function() {
			$("#floatDiv").hide();
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
					'forward' : forward,
					'index' : indexValue,
					'host' : host
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
				},
				error : function(response) {
					debugger;
					$("#detailTable tbody").html("");
					alert("系统异常！\n" + response.statusText);
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
			ExceptionMonitor.showErrorModalHandler(id, hostTemp, -1, oData, preIndex);

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
			ExceptionMonitor.showErrorModalHandler(id, hostTemp, 1, oData, nextIndex);
		}
	};
}();
/**
 * 初始化加载
 */
$(document).ready(function() {
	ExceptionMonitor.initSelect();
	$('[data-toggle="sino-grid"]').sinoGrid();
	$('.sino-select').sinoSelect();
});

function escapeChars(str) {
	str = str.replace(/\r?\n/g, "<br>");
	str = str.replace(/\r?\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	return str;
}
