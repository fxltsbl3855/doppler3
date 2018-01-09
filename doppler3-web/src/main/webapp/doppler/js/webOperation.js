/**
 * @Tiltle: 定时任务
 * Description: 定时任务
 * 
 * Author:Nec.Shen
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2017-02-20 	Nec.Shen 	Create 	新增 
 */
var WebOperation = function() {

	return {
		
		/**
		 * 查询条件参数
		 */
		queryParams : function() {
			var params = {
				'startDate' : $("#startDate").val() + ":00",
				'endDate' : $("#endDate").val() + ":00",
				'appName' : $("#appName").val(),
				'moduleName' : $("#moduleName").val(),
				'username' : $("#username").val()
			};
			return params;
		},

		/**
		 * 列表查询
		 */
		queryHandler : function() {
			var url = $.modulePath + "/web/queryWeb.shtml";
			$.ajax({
				url : url,
				data : WebOperation.queryParams(),
				dataType : 'json',
				type : 'POST',
				success : function(data) {
					$("#WebOperationTable").bootstrapTable('load', data);
				}
			});
		},
		
		/**
		 * 生成链接(任务名)
		 */
		detailHandler : function(value, row, index) {
			var linkStatus = row.linkStatus;
			if (isEmpty(linkStatus) || linkStatus !== 'Y'){
				return "";
			}
			return '<a class="link-info" href="javascript:WebOperation.showDetailHandler(\'' + row.id + '\')">明细</a>';
		},
		
		/**
		 * 获取模块名下拉框数据
		 */
		appSelectChangeHandler : function() {
			$.post($.modulePath + "/service/queryUrlList.shtml", {'appName' : $('#appName').val()}, function(data) {
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
			$("#detailWebOperationDiv").hide();
		},
		
		/**
		 * 明细查询
		 */
		showDetailHandler : function(id) {
			$("#mainDiv").hide();
			$("#detailWebOperationDiv").show();

			$.ajax({
				url : $.modulePath + "/web/queryWebDetail.shtml",
				data : {
					id : id
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					$("#reqParam").empty().html(data.param.replace(/#/g, "<br>"));
					$("#reqHeader").empty().html(data.header.replace(/#/g, "<br>"));
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
