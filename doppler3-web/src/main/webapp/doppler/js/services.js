/**
 * @Tiltle: 模块页面
 * Description: 模块页面
 * 
 * Author:Krik.Yang
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2015-08-10 	Krik.Yang 	Create 	新增 
 * 2015-08-25 	Krik.Yang 	Modify 	修改: 增加注释
 */
var Services = function(){
	
	return {
		/**
		 * 获取模块名下拉框数据
		 */
		initSelect : function() {
			$.post($.modulePath + "/components/options/getServiceList.shtml", {'appName' : $('#appName').val()}, function(data) {
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
		 * 查询模块方法信息
		 */
		query : function(){
			var url = $.modulePath + "/service/getServiceDetailList.shtml";
			$.ajax({
				url:url,
				data: Services.queryParams(),
				dataType:'json',
				success:function(data){
					$("#servicesTable").bootstrapTable('load',data);
				}
			});
		},
		
		/**
		 * 生成模块方法超链接
		 */
		nameFormatter : function(value, row, index) {
			 var moduleName = row.moduleName;
			 var methodName = row.methodName;
			 var displayValue = moduleName;
			 if (!isEmpty(methodName)){
				 displayValue += "." + methodName;
			 }
			 return '<a href="javascript:Services.toChars(\''+ moduleName +'\',\''+ methodName +'\')">' + displayValue + '</a>';
		},
	
		/**
		 * 页面设置及方法调用
		 */
		toChars : function(moduleName,methodName){
			// 1.div隐藏与现实
			$('#chartDiv')[0].style.display = "block";
			$('#serviceDiv')[0].style.display = "none";
			// 2.获取相关内容并赋值
			var appName = $("#appName").val();
			$("#serviceChartStartDate").val($("#serviceStartDate").val());
			$("#serviceChartEndDate").val($("#serviceEndDate").val());
			$("#moduleChartName").val(moduleName);
			$("#methodName").val(methodName);
			$("#appName").val(appName);
			// 3.chartDiv设置legend
			$("#legendName").html('<a href="javascript:ServiceChart.toServer(\''+ appName +'\')">' + appName + '</a>' + '  >  ' + moduleName + '.' + methodName);
			// 4.清空chart1内容
			$('#chart1').html("");
			// 5.绘制曲线图
			ServiceChart.generateLineChart();
		},
		
		/**
		 * 获取services页面查询参数
		 */
		queryParams : function(){
			var params={
				'startDate': $("#serviceStartDate").val(),
				'endDate': $("#serviceEndDate").val(),
				'appName' :  $.trim($("#appName").val()),
				'moduleName' : $.trim($("#moduleName").val())
			};
			return params;
		},
		
		/**
		 * 百分比现实设置
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
			
			var moduleName = row.moduleName;
			var methodName = row.methodName;
		    return '<a href="javascript:Services.toTopReq(\''+moduleName+'\', \''+methodName+'\')">' + value + '</a>';
		},
		
		/**
		 * 跳转最大耗时
		 */
		toTopReq : function(moduleName, methodName){
			$('#mainDiv').css('display', 'none');
			$('#serviceDiv').css('display', 'none');
			$('#topReqDiv').css('display', 'block');
			MaxTimeDetail.init("模块统计", Services.showSecondLevelTitle, $("#serviceStartDate").val(), $("#serviceEndDate").val());
			MaxTimeDetail.queryServices($("#appName").val(), moduleName, methodName);
		},
		
		showSecondLevelTitle : function(){
			$('#mainDiv').css('display', 'none');
			$('#serviceDiv').css('display', 'block');
			$('#topReqDiv').css('display', 'none');
		}
	};
}();