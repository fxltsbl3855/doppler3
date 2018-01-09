/**
 * @Tiltle: 最大耗时详情
 * Description: 最大耗时详情
 * 
 * Author:Nec.Shen
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2017-02-20 	Nec.Shen 	Create 	新增 
 */
var MaxTimeDetail = function(){
	
	return {
		/**
		 * 初始化
		 */
		init : function(title, fn, startDate, endDate) {
			$("#secondLevelTitle").html(title);
			$("#secondLevelTitle").on("click", fn);
			$("#maxTimeStartDate").html(startDate);
			$("#maxTimeEndDate").html(endDate);
		},
		
		/**
		 * 查询模块方法信息
		 */
		queryServices : function(appName, moduleName, methodName){
			var url = $.modulePath + "/app/topMaxTimeRequest.shtml";
			$.ajax({
				url:url,
				data: MaxTimeDetail.queryServicesParams(appName, moduleName, methodName),
				dataType:'json',
				success:function(data){
					$("#topRequestTable").bootstrapTable('load',data);
				}
			});
		},
		
		/**
		 * 查询App方法信息
		 */
		queryApp : function(appName){
			var url = $.modulePath + "/app/topMaxTimeRequest.shtml";
			$.ajax({
				url:url,
				data: MaxTimeDetail.queryAppParams(appName),
				dataType:'json',
				success:function(data){
					$("#topRequestTable").bootstrapTable('load',data);
				}
			});
		},
		
		/**
		 * 查询HOST方法信息
		 */
		queryHost : function(host){
			var url = $.modulePath + "/app/topMaxTimeRequest.shtml";
			$.ajax({
				url:url,
				data: MaxTimeDetail.queryHostParams(host),
				dataType:'json',
				success:function(data){
					$("#topRequestTable").bootstrapTable('load',data);
				}
			});
		},
		
		/**
		 * 获取services页面查询参数
		 */
		queryServicesParams : function(appName, moduleName, methodName){
			MaxTimeDetail.showQueryCondition("module", appName, moduleName, methodName);
			var params={
				'startDate': $("#maxTimeStartDate").html(),
				'endDate': $("#maxTimeEndDate").html(),
				'host' : "",
				'appName' :  $.trim(appName),
				'moduleName' : $.trim(moduleName),
				'methodName' : $.trim(methodName)
			};
			return params;
		},
		
		/**
		 * 获取App页面查询参数
		 */
		queryAppParams : function(appName){
			MaxTimeDetail.showQueryCondition("app", appName);
			var params={
				'startDate': $("#maxTimeStartDate").html(),
				'endDate': $("#maxTimeEndDate").html(),
				'host' : "",
				'appName' :  $.trim(appName),
				'moduleName' : "",
				'methodName' : ""
			};
			return params;
		},
		
		/**
		 * 获取HOST页面查询参数
		 */
		queryHostParams : function(host){
			MaxTimeDetail.showQueryCondition("host", host);
			var params={
				'startDate': $("#maxTimeStartDate").html(),
				'endDate': $("#maxTimeEndDate").html(),
				'host' :  $.trim(host),
				'appName' :  "",
				'moduleName' : "",
				'methodName' : ""
			};
			return params;
		},
		
		showQueryCondition : function(type, arg0, arg1, arg2){
			if (type === "app"){
				$("#maxTimeAppDiv").show();
				$("#maxTimeHostDiv").hide();
				$("#maxTimeModuleDiv").hide();
				$("#maxTimeAppName").html(arg0);
				return;
			}
			
			if (type === "module"){
				$("#maxTimeAppDiv").show();
				$("#maxTimeHostDiv").hide();
				$("#maxTimeModuleDiv").show();
				$("#maxTimeAppName").html(arg0);
				$("#maxTimeModuleName").html(arg1);
				$("#maxTimeMethodName").html(arg2);
				return;
			}
			
			if (type === "host"){
				$("#maxTimeAppDiv").hide();
				$("#maxTimeHostDiv").show();
				$("#maxTimeModuleDiv").hide();
				$("#maxTimeHost").html(arg0);
				return;
			}
			
		},
		
		/**
		 * “参数”列特殊处理：固定内容长度，多余隐藏，title中显示
		 */
		paramFormatter : function(value, row, index) {
			var param = row.param;
			if (param.length > 22){
				param = param.substring(0, 22) + "...";
			}
		    return '<span title="' + row.param + '">' + param + '</span>';
		}
	};
}();