/**
 * @Tiltle: 日志展示 Description: 日志展示
 * 
 * Author:Nec.Shen
 * 
 * 修改日期 修改人 修改内容 说明 2017-02-20 Nec.Shen Create 新增
 */
var LogShow = function() {

	return {

		/**
		 * 查询条件参数
		 */
		queryParams : function() {
			var params = {
				'startDate' : $("#startDate").val(),
				'endDate' : $("#endDate").val(),
				'appName' : $('#appName').val(),
				'opType' : $("#opType").val(),
				'opObj' : $("#opObj").val(),
				'busField' : $("#busField").val()
			};
			return params;
		},

		/**
		 * 列表查询
		 */
		queryHandler : function() {
			var url = $.modulePath + "/log/getOpBoList.shtml";
			$.ajax({
				url : url,
				data : LogShow.queryParams(),
				dataType : 'json',
				type : 'POST',
				success : function(data) {
					$("#LogShowTable").bootstrapTable('load', data);
				}
			});
		},

		/**
		 * 生成链接(任务名)
		 */
		detailHandler : function(value, row, index) {
			return '<a class="link-info" href="javascript:LogShow.showDetailHandler(\'' + row.id + '\')">明细</a>';
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
		 * 获取模块名下拉框数据
		 */
		appSelectChangeHandler : function() {
			$.post($.modulePath + "/log/queryOpTypeObj.shtml", {}, function(data) {
				if (data == null) {
					return;
				}
				$('#opType').empty();
				var opTypeHtml = '<option value="-1">请选择</option>';
				for ( var i in data.opTypeList) {
					opTypeHtml += '<option value="' + data.opTypeList[i] + '">' + data.opTypeList[i] + '</option>';
				}
				$('#opType').html(opTypeHtml);

				$('#opObj').empty();
				var opObjHtml = '<option value="-1">请选择</option>';
				for ( var j in data.opObjList) {
					opObjHtml += '<option value="' + data.opObjList[j] + '">' + data.opObjList[j] + '</option>';
				}
				$('#opObj').html(opObjHtml);

				$('#appName').empty();
				var appHtml = '<option value="-1">请选择</option>';
				for ( var j in data.appList) {
					appHtml += '<option value="' + data.appList[j] + '">' + data.appList[j] + '</option>';
				}
				$('#appName').html(appHtml);
			});
		},

		/**
		 * 返回主页面(定时任务)
		 */
		showMainHandler : function() {
			$("#mainDiv").show();
			$("#detailLogShowDiv").hide();
		},

		/**
		 * 明细查询
		 */
		showDetailHandler : function(id) {
			$("#mainDiv").hide();
			$("#detailLogShowDiv").show();

			$.ajax({
				url : $.modulePath + "/log/queryOpDetail.shtml",
				data : {
					id : id
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					if (data == null) {
						return;
					}
					$('#timestampD').html(new Date(data.timestamp).Format("yyyy-MM-dd hh:mm:ss"));
					$('#opObjD').html(data.opObj);
					$('#hostD').html(data.host);
					$('#appNameD').html(data.appName);
					$('#opTypeD').html(data.opType);
					$("#requstParam").empty().html("&nbsp;&nbsp;&nbsp;&nbsp;" + data.param);
					$("#busParam").empty().html("&nbsp;&nbsp;&nbsp;&nbsp;" + data.busParam);
				}
			});
		}
	};
}();
/**
 * 初始化加载
 */
$(document).ready(function() {
	$('[data-toggle="sino-grid"]').sinoGrid();
	$('.sino-select').sinoSelect();
	LogShow.appSelectChangeHandler();
});
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
