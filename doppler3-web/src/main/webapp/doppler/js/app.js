/**
 * @Tiltle: 应用监控页面
 * Description: 应用监控页面
 * 
 * Author:Krik.Yang
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2015-08-10 	Krik.Yang 	Create 	新增 
 * 2015-08-25 	Krik.Yang 	Modify 	修改: 增加注释
 */

/**
 * 初始化加载
 */
$(document).ready(function(){
	$('#appTable').bootstrapTable();
});
var App = function(){
	
	return {
		
		/**
		 * 应用监控列表查询
		 */
		query : function(){
			var url = $.modulePath + "/app/appDetailList.shtml";
			$.ajax({
				url:url,
				data: App.queryParams(),
				dataType:'json',
				success:function(data){
					$("#appTable").bootstrapTable('load',data);
				}
			});
		},

		/**
		 * 生成应用名超链接
		 */
		nameFormatter : function(value, row, index) {
			var appName = row.appName;
		    return '<a href="javascript:App.toServices(\'' + appName +'\')">' + value + '</a>';
		},
		
		/**
		 * div设置及初始化模块页面下拉框
		 */
		toServices : function(appName){
			$("#appName").val(appName);
			$("#appNameCondition").html(appName);
			$('#mainDiv')[0].style.display = "none";
			$('#serviceDiv')[0].style.display = "block";
			$('#topReqDiv').css('display', 'none');
			Services.initSelect();
		},
		
		/**
		 * 查询条件参数
		 */
		queryParams : function(){
			var params = {'startDate' : $("#startDate").val(), 'endDate' : $("#endDate").val()};
			return params;
		},
		
		/**
		 * 百分比格式化
		 */
		toPercent : function(value){
			return value.toPercent();
		},
		
		/**
		 * 生成最大耗时超链接
		 */
		maxTimeFormatter : function(value, row, index) {
			var appName = row.appName;
			var linkStatus = row.linkStatus;
			if (isEmpty(linkStatus) || linkStatus !== 'Y'){
				return value;
			}
		    return '<a href="javascript:App.toTopReq(\'' + appName +'\')">' + value + '</a>';
		},
		
		/**
		 * 跳转最大耗时
		 */
		toTopReq : function(appName){
			$('#mainDiv').css('display', 'none');
			$('#serviceDiv').css('display', 'none');
			$('#topReqDiv').css('display', 'block');
			MaxTimeDetail.init("应用统计", App.showSecondLevelTitle, $("#startDate").val(), $("#endDate").val());
			MaxTimeDetail.queryApp(appName);
		},
		
		showSecondLevelTitle : function(){
			$('#mainDiv').css('display', 'block');
			$('#serviceDiv').css('display', 'none');
			$('#topReqDiv').css('display', 'none');
		}
	};
}();