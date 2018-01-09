/**
 * @Tiltle: 曲线图页面
 * Description: 曲线图页面
 * 
 * Author:Krik.Yang
 * 
 * 修改日期 		修改人 		修改内容 	说明 
 * 2015-08-10 	Krik.Yang 	Create 	新增 
 * 2015-08-25 	Krik.Yang 	Modify 	修改: 增加注释
 */
var ServiceChart = function() {
	
	return {

		/**
		 * 绘制曲线图
		 */
		generateLineChart : function() {
			
			$.post($.modulePath + "/service/serviceChart.shtml", {
				"startDate" : $("#serviceChartStartDate").val(),
				"endDate" : $("#serviceChartEndDate").val(),
				"appName" : $("#appName").val(),
				"moduleName" : $("#moduleChartName").val(),
				"methodName" : $("#methodName").val()
			}, function(data) {
				
				var xAxisDatas = new Array();
				var seriesDatas = new Array();
				// 动态加载数据
				for ( var i in data) {
					xAxisDatas.push(data[i].date);
					seriesDatas.push(data[i].reqNum == 0 ? '0' : data[i].reqNum);
				}

				// 指定图表的配置项和数据
				var chart1 = echarts.init(document.getElementById('chart1'));
				options = {
					tooltip : {
						trigger : 'axis'
					},
					legend : {},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : xAxisDatas
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : [ {
						name : '调用量',
						type : 'line',
						data : seriesDatas
					} ]
				};
				chart1.setOption(options);
			});
		},

		/**
		 * 返回模块页面
		 */
		toServer : function(appName) {
			// 1.初始化参数
			$("#appName").val(appName);
			$("#moduleName").val(-1);
			// 2.设置div
			$('#chartDiv')[0].style.display = "none";
			$('#serviceDiv')[0].style.display = "block";
			// 3.下拉框加载
			Services.initSelect();
			// 4.模块列表查询
			Services.query();

		}
	};
}();
