/**
 * @Tiltle: IP监控页面
 * Description: IP监控页面
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
	$('#ipTable').bootstrapTable();
	$('[data-toggle="sino-grid"]').sinoGrid();
});
var IpMonitor = function(){
	
	return {
		
		/**
		 * 主机监控列表查询
		 */
		query : function(){
			var url = $.modulePath + "/app/getHostDetailList.shtml";
			$.ajax({
				url:url,
				data: IpMonitor.queryParams(),
				dataType:'json',
				success:function(data){
					$("#ipTable").bootstrapTable('load',data);
				}
			});
		},
		
		/**
		 * 查询参数
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
			var linkStatus = row.linkStatus;
			if (isEmpty(linkStatus) || linkStatus !== 'Y'){
				return value;
			}
			
			var host = row.host;
		    return '<a href="javascript:IpMonitor.toTopReq(\'' + host +'\')">' + value + '</a>';
		},
		
		/**
		 * 跳转最大耗时
		 */
		toTopReq : function(host){
			$('#mainDiv').css('display', 'none');
			$('#topReqDiv').css('display', 'block');
			MaxTimeDetail.init("HOST统计", IpMonitor.showSecondLevelTitle, $("#startDate").val(), $("#endDate").val());
			MaxTimeDetail.queryHost(host);
			
		},
		
		showSecondLevelTitle : function(){
			$('#mainDiv').css('display', 'block');
			$('#topReqDiv').css('display', 'none');
		}
	};
}();