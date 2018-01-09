/**
 * @Tiltle: 定时任务
 * Description: 定时任务
 * 
 * Author:Nec.Shen
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2017-02-20 	Nec.Shen 	Create 	新增 
 */
var TimerTask = function() {
	
	// 前后翻页变量
	var vForward = 0;

	return {
		
		/**
		 * 初始化Table
		 */
		initTable : function(){
			$("#timerTaskTable>tbody>tr").mouseout(function(e){
				$(this).removeClass("active");
				});
			$("#timerTaskTable>tbody>tr").mouseover(function(e){
				$(this).addClass("active");
			});
		},

		/**
		 * 查询条件参数
		 */
		queryParams : function() {
			var params = {
				'startDate' : $("#startDate").val(),
				'endDate' : $("#endDate").val(),
				'appName' : $("#appName").val(),
				'moduleName' : $("#moduleName").val(),
				'result' : $("#result").val()
			};
			return params;
		},

		/**
		 * 列表查询
		 */
		queryHandler : function() {
			var url = $.modulePath + "/job/queryJob.shtml";
			$.ajax({
				url : url,
				data : TimerTask.queryParams(),
				dataType : 'json',
				success : function(data) {
					$("#timerTaskTable").bootstrapTable('load', data);
					TimerTask.initTable();
				}
			});
		},
		
		/**
		 * 生成链接(任务名)
		 */
		taskNameFormatter : function(value, row, index) {
			var className = row.className;
			var linkStatus = row.linkStatus;
			if (isEmpty(linkStatus) || linkStatus !== 'Y'){
				return className;
			}
			return '<a class="link-info" href="javascript:TimerTask.showDetailHandler(\'' + row.appName + '\', \'' + row.host + '\', \'' + row.className + '\', \'\')">' + className + '</a>';
		},
		
		/**
		 * 生成链接(执行结果)
		 */
		resultFormatter : function(value, row, index) {
			var result = TimerTask.getResultFromCode(row.result);
			var linkStatus = row.linkStatus;
			if (isEmpty(linkStatus) || linkStatus !== 'Y'){
				return result;
			}
			return '<a class="link-info" href="javascript:TimerTask.showDetailHandler(\'' + row.appName + '\', \'' + row.host + '\', \'' + row.className + '\', \'' + row.result + '\')">' + result + '</a>';
		},
		
		/**
		 * 改变明细页面执行结果显示，修改为中文
		 */
		detailResultFormatter : function(value, row, index) {
			return TimerTask.getResultFromCode(row.result);
		},
		
		/**
		 * 根据编码获取执行结果中文描述
		 */
		getResultFromCode : function(result){
			if (result === 'SUCCESS'){
				return '成功';
			}
			
			if (result === 'EXCEPTION'){
				return '异常';
			}
			
			if (result === 'UNKNOWN'){
				return '未知';
			}
			
			return result;
		},
		
		/**
		 * 应用select改变
		 */
		appSelectChangeHandler : function() {
			TimerTask.initSelect();
		},
		
		/**
		 * 获取模块名下拉框数据
		 */
		initSelect : function() {
			$.post($.modulePath + "/job/getModuleList.shtml", {'appName' : $('#appName').val()}, function(data) {
				$('#moduleName').empty();
				var html = '<option value="-1">请选择</option>';
				for (var i in data) {
					var serviceId = data[i].serviceId;
					var name = data[i].name;
					html += '<option value="' + serviceId + '">'
							+ name + '</option>';
				}
				$('#moduleName').html(html);
			});
		},

		/**
		 * 返回主页面(定时任务)
		 */
		showMainHandler : function(){
			$("#mainDiv").show();
			$("#detailTimerTaskDiv").hide();
		},
		
		showDetailCondition : function(appName, className, result){
			$("#detailStartDate").html($("#startDate").val());
			$("#detailEndDate").html($("#endDate").val());
			$("#detailAppName").html(appName);
			$("#detailClassName").html(className);
			$("#detailResult").html(TimerTask.getResultFromCode(result));
		},
		
		/**
		 * 明细查询
		 */
		showDetailHandler : function(appName, host, className, result) {
			$("#mainDiv").hide();
			$("#detailTimerTaskDiv").show();
			if (result === ""){
				$("#titleValue").html(className);
				$("#detailResultDiv").hide();
			}else{
				$("#titleValue").html(TimerTask.getResultFromCode(result));
				$("#detailResultDiv").show();
			}
			
			TimerTask.showDetailCondition(appName, className, result);

			$.ajax({
				url : $.modulePath + "/job/queryJobDetail.shtml",
				data : {
					startDate : $("#startDate").val(),
					endDate : $("#endDate").val(),
					appName : appName,
					host : host,
					moduleName : className,
					result : result
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					$("#timerTaskDetailTable").bootstrapTable('load', data);
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
});
